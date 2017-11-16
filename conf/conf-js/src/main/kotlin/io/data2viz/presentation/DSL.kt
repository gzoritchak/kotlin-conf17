package io.data2viz.presentation


fun presentation(init: Presentation.() -> Unit) = Presentation().apply(init)


@DslMarker
annotation class PresentationTagMarker


class Presentation {

    val pages = mutableListOf<Page>()

    var title = ""

    fun page(init: Page.() -> Unit = {}) {
        val newPage = Page().apply(init)
        pages.add(newPage)
    }
}


class Page {

    val contents = mutableListOf<PresFlowContent>()


    fun getSelectableItems(): List<PresFlowContent> {
        val selectableItems = mutableListOf<PresFlowContent>()

        fun collectSelectable(contents: List<PresFlowContent>) {
            contents.forEach {
                if(it.selectable)
                    selectableItems.add(it)
                if (it is HasChildrenContent)
                    collectSelectable((it as HasChildrenContent).children())
            }
        }
        collectSelectable(contents)
        return selectableItems
    }

    @PresentationTagMarker
    fun p(init: ParagraphContent.() -> Unit){
        val element: PresFlowContent = ParagraphContent().apply(init)
        addContent(element)
    }

    @PresentationTagMarker
    fun ul(init: ListContent.() -> Unit){
        addContent(ListContent().apply(init))
    }

    fun title(level:Int = 3, init: TitleContent.() -> Unit) {
        addContent(TitleContent(level).apply(init))
    }

    fun image(init: ImageContent.() -> Unit){
        addContent(ImageContent().apply(init))
    }

    fun code(block: () -> String ) {
        addContent(CodeContent(block))
    }

    fun addContent(element: PresFlowContent) {
        contents.add(element)
    }

}

open class PresFlowContent {
    var classes:String? = null
    var selectable = false
}

interface HasChildrenContent {
    fun children():List<PresFlowContent>
}

class CodeContent(val block: () -> String) : PresFlowContent() {
}

class ParagraphContent() : PresFlowContent() {
    var text = ""
    operator fun String.unaryPlus() {
        this@ParagraphContent.text = this
    }
}


class TitleContent(val level: Int) : PresFlowContent() {
    var title = ""
    operator fun String.unaryPlus() {
        this@TitleContent.title = this
    }
}

class ImageContent : PresFlowContent() {
    var name: String = ""
}

class ListContent : PresFlowContent(), HasChildrenContent {
    override fun children(): List<PresFlowContent>  = listItems
    val listItems = mutableListOf<ListItemContent>()
    fun li(selectable: Boolean = true, init: ListItemContent.() -> Unit) {
        val element = ListItemContent().apply{
            this.selectable = selectable
            init(this)
        }
        listItems.add(element)
    }
}

class ListItemContent : PresFlowContent(), HasChildrenContent{

    override fun children(): List<PresFlowContent>  = contents

    val contents:MutableList<PresFlowContent> = mutableListOf<PresFlowContent>()
    var text = ""

    fun image(init: ImageContent.() -> Unit){
        contents.add(ImageContent().apply(init))
    }

    operator fun String.unaryPlus() {
        this@ListItemContent.text = this
    }
}
