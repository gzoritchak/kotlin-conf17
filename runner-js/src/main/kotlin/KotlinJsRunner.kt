package io.data2viz.presentation


import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.browser.window


data class PresConfiguration (val imgDir:String = "")


class KotlinJsRunner(presentation: Presentation, val conf: PresConfiguration = PresConfiguration() ) {

    var slideHeader: HEADER.() -> Unit = {}
    var slideFooter: DIV.() -> Unit = {}
    var afterRefreshFromState: (PresentationManager) -> Unit = {}
    val currentPres = PresentationManager(presentation)

    fun start() {
        println("start presentation")
        window.onload = { println("Window onload") }
        buildDomFromPresentation()
        window.onresize = { rescale()}
        rescale()
        retrieveStateFromLocation()
        listenToShortcuts()
        refreshFromState()
    }

    private fun rescale() {
        val ratio_16_9 = 16.0 / 9.0
        val ratio = window.innerWidth / window.innerHeight

        val scale =
                if (ratio < ratio_16_9)
                    window.innerWidth / 1280.0
                else
                    window.innerHeight / 720.0

        val slider = document.getElementById("slider") as HTMLElement
        slider.style.transform = "scale3d($scale, $scale, 1) translate3d(-50%, -50%, 0)"
    }

    private fun retrieveStateFromLocation() {
        val presState = window.location.hash.toCurrentState()
        if (presState != null) {
            println("Loaded state$presState")
            currentPres.synchronizeWithState(presState)
        }
    }


    private fun PageState.toId() = "page-${index}"
    private fun PageState.select(): HTMLElement = document.querySelector("#${toId()}")!! as HTMLElement

    private fun listenToShortcuts() {

        window.onclick = {
            currentPres.next()
            refreshFromState()
        }


        val keyEventListener = { e:Event ->
            val ke = e as KeyboardEvent
            if (ke.keyCode == 39) currentPres.next()
            if (ke.keyCode == 37) currentPres.previous()
            refreshFromState()
        }

        window.addEventListener("keydown", keyEventListener )
        println("Initial keylistener")

        window.onblur = {
            println("Remove keylistener")
            window.removeEventListener( "keydown", keyEventListener)

        }
        window.onfocus = {
            println("Adding keylistener again")
            window.addEventListener("keydown", keyEventListener )
        }

    }

    private fun refreshFromState() {
        if (currentPres.started) {
            document.querySelector("#slider")?.setAttribute("started", "true")
        }
        currentPres.pages.forEach { pageState ->
            pageState.select().setAttribute("page-state", pageState.state.name.toLowerCase())
            pageState.items.forEach { itemState ->
                document.querySelector("#page-${pageState.index}-item-${itemState.index + 1}")
                        ?.setAttribute("item-state", itemState.state.name.toLowerCase())
                        ?: console.log("Element #page-${pageState.index}-item-${itemState.index + 1} not found")
            }
        }
        val currentPage = currentPres.currentPageIndex
        if (currentPage >= 0)
            window.location.hash = "#$currentPage-0"

        afterRefreshFromState(currentPres)

    }

    private var currentPageIndex = 0
    private var currentItemIndex = 0

    private fun buildDomFromPresentation() {
        println("build dom from presentation")
        val pres = document.create.div("slider") {
            id = "slider"
            headerPres()
            sliderIntro()
            pages()
            footerPres()
        }
        document.querySelector("body")?.appendChild(pres)
    }

    private fun DIV.headerPres() {
        header("slider-header") {
            slideHeader(this)
        }
    }

    private fun DIV.footerPres() {
        slideFooter(this)
    }

    private fun DIV.sliderIntro() {
        section("slider-intro") { h1 { +currentPres.presentation.title } }
    }

    private fun DIV.pages() {
        section("slider-pages-container") {
            currentPres.pages.forEachIndexed { index:Int, p ->
                currentPageIndex = index
                currentItemIndex = 0
                toDom(p)
            }
        }
    }

    val contentGenerators: MutableMap<String, FlowContent.(PresFlowContent) -> Unit> = mutableMapOf()

    inline fun <reified T : PresFlowContent> addContentGeneration(noinline generation: FlowContent.(PresFlowContent) -> Unit) {
        val name = T::class.simpleName!!
        contentGenerators.put(name, generation)
    }


    private fun SECTION.toDom(p: PageState) {
        currentPres
        section("slider-page") {
            id = p.toId()
            attributes["page-state"] = p.state.name.toLowerCase()
            div("slider-page-box") {
                div("slider-text") {
                    p.page.contents.forEach {
                        toDom(it)
                    }
                }
            }
        }
    }

    fun PresFlowContent.idAndClasses(flowContent: FlowContent) {
        with(flowContent) {
            val item = this as CommonAttributeGroupFacade
            if (this@idAndClasses.selectable)
                item.id = "page-$currentPageIndex-item-${++currentItemIndex}"
            this@idAndClasses.classes?.let {
                val allClasses = item.classes.toMutableSet().apply { addAll(it.split(' '))}
                item.classes = allClasses
            }
        }
    }

    private fun FlowContent.toDom(content: PresFlowContent) {

        if (contentGenerators.containsKey(content::class.simpleName!!)) {
            val generator = contentGenerators.get(content::class.simpleName!!)!!
            generator(content)
        } else {

            when (content) {

                is ParagraphContent -> p {
                    content.idAndClasses(this)
                    +content.text
                }

                is TitleContent -> when (content.level) {
                    2 -> h2 { content.idAndClasses(this); +content.title }
                    3 -> h3 { content.idAndClasses(this); +content.title }
                    4 -> h4 { content.idAndClasses(this); +content.title }
                    5 -> h5 { content.idAndClasses(this); +content.title }
                }

                is ImageContent -> figure("image-large") {
                    content.idAndClasses(this)
                    img {
                        src = "${conf.imgDir}/${content.name}"
                    }
                    figcaption { }
                }
                is ListContent -> ul {
                    content.idAndClasses(this)
                    content.listItems.forEach {
                        li {
                            it.idAndClasses(this)
                            +it.text

                            it.contents.forEach {

                                toDom(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun PresState.toLocation() = "$page-$item"

fun CharSequence.toCurrentState(): PresState? {
    if (length == 0 || get(0) != '#') return null
    val split = drop(1).split('-')
    if (split.size != 2) return null
    val page = split[0].toIntOrNull()
    val item = split[1].toIntOrNull()
    if (page == null || item == null || page < 0 || item < 0) return null
    return PresState(page, item)
}
