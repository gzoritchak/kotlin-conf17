import io.data2viz.presentation.*
import io.data2viz.presentation.PresFlowContent
import kotlinx.html.*
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.window


val kotlinConf = presentation {

    val nobullet = "nobullet"

    title = "Frontend Kotlin from the trenches"

    page {
        title { +"context" }
        ul {
            classes = nobullet
            li (false) {
                twitter("gz_k", "Gaëtan Zoritchak")
            }
            li {
                classes = "side-by-side"
                selectable = true
                twitter("imtam5", "Pierre Mariac")
                twitter("azertypow", "Nicolas Baldran")
            }
            li {
                selectable = true
                twitter("data2viz_io", "Data2viz")
            }
        }
    }

    page {
        title { + "The need of an efficient dev workflow" }

        image {
            classes = "image-full"
            name = "check-list-0.png"
        }
    }


    page {
        ul {
            classes = "over nobullet"
            li {
                selectable = false
                image {
                    classes = "image-full"
                    name = "keywords-0.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "keywords-1.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "keywords-2.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "keywords-3.png"
                }
            }
        }
    }

    page {
        title { +"Build with Gradle..." }
        ul {
            classes = nobullet
            li { +"+++ Best pace for new features releases (DCE,... , IC)"}
            li { +"++ performances" }
            li { +"++ easier to adapt to a complex build" }
//            li { +"- IDE synchronization" }
            li { +"- lots of releases" }
        }
    }


    page {
        ul {
            classes = "over nobullet"
            li {
                selectable = false
                image {
                    classes = "image-full"
                    name = "kotlin-frontend.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "d2v-logic.png"
                }
            }
        }
    }


    page {
        title { +"Let's see it in action..." }
    }

    page {

        ul {
            classes = "over nobullet"
            li {
                selectable = false
                image {
                    classes = "image-full"
                    name = "check-list-1.png"
                }

            }
            li {
                image {
                    classes = "image-full"
                    name = "check-list-2.png"
                }
            }
        }

    }


    page {
        title { +"Tests" }
        title(3) { +"#1 Js tests" }
        image {
            classes = "image-full"
            name = "multiplatform-3-0.png"
        }
    }

    page {
        image {
            classes = "image-full"
            name = "check-list-3.png"
        }
    }


    page {
        title { +"Release" }
        ul {
            li {
                +"Dead Code Elimination"
            }
            li {
                + "Webpack"
            }
        }
    }

    page {
        image {
            classes = "image-full"
            name = "check-list-4.png"
        }
    }

    page {
        title { +"Feedback on kotlin.js performances." }
    }


    page {
        ul {
            classes = "over nobullet"
            li {
                selectable = false
                image {
                    classes = "image-full"
                    name = "swot-V2-0.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "swot-V2-1.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "swot-V2-2.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "swot-V2-3.png"
                }
            }
            li {
                image {
                    classes = "image-full"
                    name = "swot-V2-4.png"
                }
            }
        }
    }


    page {
        title { +"Thank you" }
        p {
            selectable = true
          + "follow us on https://github.com/data2viz/data2viz"
        }
    }

}


class TwitterContent(val twitterAccount:String, val name:String): PresFlowContent()

private fun ListItemContent.twitter(id: String, name: String) {
    contents.add(TwitterContent(id, name))
}


fun main(args: Array<String>) {
    println("Hello Kotlin Conf")

    val conf = PresConfiguration(
            imgDir = "build/img"
    )
    KotlinJsRunner(kotlinConf, conf).apply {
        addContentGeneration<GitContent> {
            if (it is GitContent)
                span("git") {
                    span("git-container") {
                        span(classes = "git-branch button") {
                            val cmd = "git checkout ${it.branch}"
                            title = "Copy '$cmd' to clipboard"
                            attributes["cmd"] = cmd
                            +it.branch
                        }
                    }
                }
        }

        addContentGeneration<TwitterContent> {
            if (it is TwitterContent)
                div ("cartel-profile") {
                    div("img-container") {
                        img { src="${conf.imgDir}/${it.twitterAccount}.png" }
                    }
                    div("txt-container"){
                        div("name") {
                            span { +it.name }
                        }
                        div("twitter"){
                            +"@${it.twitterAccount}"
                        }
                    }
                }
        }

        afterRefreshFromState = { pres ->
            val curPage = pres.currentPageIndex + 1
            val totalPages = pres.pages.size


            if (pres.currentPageIndex > -1) {
                val percent = (100 * (1.0 - curPage.toDouble() / totalPages.toDouble())).toInt()
                val progress = document.querySelector(".footer-progress-dimmer")!! as HTMLDivElement
                progress.style.transform = "translate3d(-$percent%, 0, 0)"

                (document.querySelector(".footer-progress-notes")!! as HTMLSpanElement).textContent =
                        "$curPage | $totalPages"

            }
        }

        val location = window.location.toString()
        val first = location.indexOfFirst { it == '#' }

        slideHeader = {
            div("logo-container") {
                div("logo") {
                    a(href = location.substring(0 until first)) {
                        unsafe {
                            //language=HTML
                            +"""
                        <svg class="logo-icon" width="40px" height="40px" viewBox="0 0 40 40" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                            <title>icon</title>
                            <path d="M5,5 L36,5 L36,36 L5,36 L5,5 Z M9.96,12.44 L19.26,31.04 L9.96,31.04 L9.96,12.44 Z M11.2,9.96 L19.26,9.96 L25.15,21.74 L21.12,29.8 L11.2,9.96 Z M21.74,9.96 L31.04,9.96 L26.39,19.26 L21.74,9.96 Z M7.5,7.5 L7.5,33.5 L33.5,33.5 L33.5,7.5 L7.5,7.5 Z" stroke="none" fill-rule="evenodd"></path>
                        </svg>
                     """.trimIndent()
                        }
                    }
                    span("logo-type") { +"Data2viz" }
                    span("logo-domain-extension") { +".io" }
                }
//                div("slider-title") { h5 { +"Create web application with kotlin" } }
//                div("chapter-title") { h5 { +"Current chapter" } }
            }
        }
        slideFooter = {
            unsafe {
                //language=HTML
                +"""
             <footer class="slider-footer">
                <h6 class="footer-signature">Data2viz.io | Kotlinconf 2017</h6>
                <div class="footer-progress">
                    <div class="footer-progress-box">
                        <div class="footer-progress-dimmer">
                            <div class="footer-progress-bar"></div>
                            <span class="notes footer-progress-notes">1 | 10</span>
                        </div>
                    </div>
                </div>
            </footer>

            <svg width="1280" height="720" viewBox="0 0 1280 720" xmlns="http://www.w3.org/2000/svg" version="1.1" class="slider-background">
                <circle cx="10" cy="20" r="20" class="background-color-pages" style="
                    cx: 1114px;
                    cy: 244px;
                    animation-delay: 1.5s;
                "></circle>
            </svg>

            <svg version='1.1' id='kotlin-conf-logo' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' x='0px' y='0px' width='802.7px' height='153.6px' viewBox='0 0 802.7 153.6' style='enable-background:new 0 0 802.7 153.6;' xml:space='preserve'>
                <g>
                    <g>
                        <polygon class='kc-st0' points='37.1,1.9 37.1,73.7 73,37.8 ' />
                        <polygon class='kc-st1' points='34.6,36.5 34.6,1.9 0,36.5 ' />
                        <polygon class='kc-st0' points='73.5,153.6 73.5,119.1 38.9,153.6 ' />
                        <polygon class='kc-st0' points='75,114.2 188.9,0 151.6,0 114.9,0 37.7,76.9 ' />
                        <polygon class='kc-st0' points='76.2,153.6 110.8,153.6 76.2,119 ' />
                        <polygon class='kc-st0' points='113.8,78.7 76.9,116.1 114.9,153.6 188.9,153.6 ' />
                        <polygon class='kc-st0' points='37.1,151.8 73.2,116.1 37.1,80 ' />
                    </g>
                    <g>
                        <polygon class='kc-st2' points='287.4,71.4 304.6,71.4 304.6,105.5 336,71.4 357.1,71.4 325.2,104.5 358.4,149.4 337.8,149.4 313.6,116.2 304.6,125.6 304.6,149.4 287.4,149.4 ' />
                        <path class='kc-st2' d='M351.8,119.9v-0.2c0-17.2,13.8-31.1,32.4-31.1c18.5,0,32.2,13.7,32.2,30.9v0.2c0,17.2-13.8,31.1-32.4,31.1 C365.5,150.8,351.8,137.1,351.8,119.9 M399.8,119.9v-0.2c0-8.8-6.4-16.5-15.7-16.5c-9.7,0-15.5,7.5-15.5,16.3v0.2 c0,8.8,6.4,16.5,15.7,16.5C394,136.2,399.8,128.7,399.8,119.9' />
                        <path class='kc-st2' d='M424.2,132.5v-28.3h-7.1V89.7h7.1V74.4h16.9v15.3h14.1v14.5h-14.1v25.5c0,3.9,1.7,5.8,5.5,5.8 c3.1,0,5.9-0.8,8.4-2.1V147c-3.6,2.1-7.7,3.5-13.4,3.5C431.2,150.4,424.2,146.3,424.2,132.5' />
                        <rect x='461.4' y='68' class='kc-st2' width='16.9' height='81.4' />
                        <path class='kc-st2' d='M486.5,68h17.8v15.1h-17.8V68z M486.9,89.7h16.9v59.8h-16.9V89.7z' />
                        <path class='kc-st2' d='M510.9,89.7h16.9v8.5c3.9-5,8.9-9.6,17.5-9.6c12.8,0,20.3,8.5,20.3,22.2v38.7h-16.9v-33.3 c0-8-3.8-12.2-10.3-12.2s-10.6,4.1-10.6,12.2v33.3h-16.9V89.7z' />
                        <path class='kc-st2' d='M569.7,110.6v-0.2c0-22.2,16.7-40.4,40.7-40.4c14.7,0,23.5,4.9,30.8,12l-10.9,12.6c-6-5.5-12.2-8.8-20-8.8 c-13.2,0-22.6,10.9-22.6,24.3v0.2c0,13.4,9.3,24.5,22.6,24.5c8.9,0,14.4-3.6,20.5-9.1l10.9,11c-8,8.6-16.9,13.9-32,13.9 C586.8,150.8,569.7,133,569.7,110.6' />
                        <path class='kc-st2' d='M640.8,119.9v-0.2c0-17.2,13.8-31.1,32.4-31.1c18.5,0,32.2,13.7,32.2,30.9v0.2c0,17.2-13.8,31.1-32.4,31.1 C654.5,150.8,640.8,137.1,640.8,119.9 M688.7,119.9v-0.2c0-8.8-6.4-16.5-15.7-16.5c-9.7,0-15.5,7.5-15.5,16.3v0.2 c0,8.8,6.4,16.5,15.7,16.5C682.9,136.2,688.7,128.7,688.7,119.9' />
                        <path class='kc-st2' d='M708.8,89.7h16.9v8.5c3.9-5,8.9-9.6,17.5-9.6c12.8,0,20.3,8.5,20.3,22.2v38.7h-16.9v-33.3 c0-8-3.8-12.2-10.3-12.2s-10.6,4.1-10.6,12.2v33.3h-16.9V89.7z' />
                        <path class='kc-st2' d='M772.2,104.2h-7v-14h7v-3.8c0-6.6,1.7-11.4,4.8-14.5s7.7-4.7,13.7-4.7c5.4,0,8.9,0.7,12,1.7V83 c-2.5-0.9-4.8-1.4-7.7-1.4c-3.9,0-6.1,2-6.1,6.5v2.3h13.7v13.8h-13.5v45.3h-16.9V104.2z' />
                    </g>
                </g>
            </svg>

                """.trimIndent()
            }
        }
        start()

        document.querySelectorAll(".git-branch")
                .asList().forEach {
            val item = it as HTMLElement
            val gitCmd = item.getAttribute("cmd")!!
            item.onclick = { copyToClipboard(gitCmd) }
        }
    }
}


class GitContent : PresFlowContent() {
    var branch = ""
}

private fun Page.git(init: GitContent.() -> Unit) {
    addContent(GitContent().apply { init() })
}

private fun ListItemContent.git(init: GitContent.() -> Unit) {
    contents.add(GitContent().apply { init() })
}


/**
 * see https://ourcodeworld.com/articles/read/143/how-to-copy-text-to-clipboard-with-javascript-easily
 */
fun copyToClipboard(text: String) {
    println("copyToClipboard :: $text")
    val id = "hidden-text-area-id"
    var textArea = document.getElementById(id) as HTMLInputElement?

    if (textArea == null) {
        val input = document.createElement("input") as HTMLInputElement
        input.id = id
        input.style.position = "fixed"
        input.style.top = "0"
        input.style.left = "0"
        input.style.width = "1px"
        input.style.height = "1px"
        input.style.border = "none"
        input.style.padding = "0"
        input.style.outline = "none"
        input.style.boxShadow = "none"
        input.style.background = "transparent"
        document.querySelector("body")?.appendChild(input)
        textArea = document.getElementById(id) as HTMLInputElement
    }

    textArea.setAttribute("value", text)
    textArea.select()
    try {
        val status = document.execCommand("copy")
        if (!status) {
            console.error("Cannot copy text")
        } else {
            console.log("The text is now on the clipboard")
        }
    } catch (e: Exception) {
        console.log("Unable to copy.")
    }
}
