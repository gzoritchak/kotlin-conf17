package io.data2viz.presentation


/**
 * Manage the state of presentation.
 */
class PresentationManager (val presentation: Presentation) {

    val pages:List<PageState> = presentation.pages.mapIndexed { index, page ->  PageState(page, index) }
    var currentPageIndex = -1
    var currentItemIndex = -1
    var started = false
    val currentPage:PageState?
        get() = if(currentPageIndex <0) null else pages[currentPageIndex]


    fun next(){

        fun eventuallyGoToNextItem():Boolean  = currentPage?.let {
            if (currentItemIndex < it.items.size - 1) {
                if (currentItemIndex > -1) {
                    it.items[currentItemIndex].state = State.PREVIOUS
                }
                currentItemIndex++
                val itemState = it.items[currentItemIndex]
                itemState.state = State.CURRENT
                true
            }
            else false
        }?: false


        fun eventuallyGoToNextPage() {
            if (currentPageIndex < pages.size - 1) {
                if (currentPageIndex > -1) {
                    pages[currentPageIndex].state = State.PREVIOUS
                }
                currentPageIndex++
                val pageState = pages[currentPageIndex]
                currentItemIndex = -1
                pageState.state = State.CURRENT
            }
        }

        if (currentPageIndex == -1) {
            started = true
        }

        if (!eventuallyGoToNextItem())
            eventuallyGoToNextPage()
    }

    fun previous(){
        if (currentPageIndex > 0){
            pages[currentPageIndex].state = State.NEXT
            currentPageIndex--
            pages[currentPageIndex].state = State.CURRENT
        }
    }

    fun synchronizeWithState(presState: PresState){
        while (currentPageIndex != presState.page && currentPageIndex < pages.size - 1)
            next()
    }

}

class PageState(val page: Page, val index:Int) {
    var state = State.NEXT
    val items = page.getSelectableItems().mapIndexed { index, presFlowContent -> ItemState(presFlowContent, index) }
}

class ItemState(val item: PresFlowContent, val index:Int) {
    var state = State.NEXT
}

enum class State {
    PREVIOUS, CURRENT, NEXT
}

data class PresState(val page:Int, val item:Int)

