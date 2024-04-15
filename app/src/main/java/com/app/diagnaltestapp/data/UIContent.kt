package com.app.diagnaltestapp.data

data class UIContent(
    val page: Page?,
) {
    data class Page(
        val title: String?,
        val totalContentItems: String?,
        val contentItems: ContentItems
    ) {
        data class ContentItems(
            val content: List<Content>
        ) {
            data class Content(
                val name: String?,
                val posterImage: String?
            )
        }


    }
}