package com.app.diagnaltestapp.data

import android.content.Context
import android.util.Log
import com.app.diagnaltestapp.utils.ReadJSONFromAssets
import com.app.diagnaltestapp.utils.getJsonFilesFromAssets
import com.google.gson.Gson

const val PAGE_1 = "content_list_1.json"
const val PAGE_2 = "content_list_2.json"
const val PAGE_3 = "content_list_3.json"
object DataStore {

   private fun fetchIntialData(context: Context): UIContent {
        val json = ReadJSONFromAssets(context, PAGE_1)
        return Gson().fromJson(json, UIContent::class.java)
    }

    private fun fetchNextData(index: Int, context: Context): UIContent {
        val path = when(index) {
            1 ->  PAGE_2
            2 ->  PAGE_3
            else -> " "
        }
        val json =  ReadJSONFromAssets(context, path)
        return Gson().fromJson(json, UIContent::class.java)
    }

    private fun fetchAllData( context: Context): List<UIContent> {
        val fileLists = getJsonFilesFromAssets(context)
        val jsonData = mutableListOf<UIContent>()
        fileLists.forEach {path ->
            val json =  ReadJSONFromAssets(context, path)
            jsonData.add(Gson().fromJson(json, UIContent::class.java))
        }
        return jsonData
    }

    fun getIntialData(context: Context): List<UIContent.Page.ContentItems.Content> {
        val dummyData = mutableListOf<UIContent.Page.ContentItems.Content>()
        val data = fetchIntialData(context)
        fetchAllData(context)
        val dataSize = data.page?.contentItems?.content?.size ?: 0
        for (rowIndex in 0 until dataSize) {
            data.page?.contentItems?.content?.get(rowIndex)?.let { dummyData.add(it) }

        }
        return dummyData
    }

    fun getNextData(index: Int, context: Context): List<UIContent.Page.ContentItems.Content> {
        val dummyData = mutableListOf<UIContent.Page.ContentItems.Content>()
        val data = fetchNextData(index, context)
        val dataSize = data.page?.contentItems?.content?.size ?: 0
        for (rowIndex in 0 until dataSize) {
            data.page?.contentItems?.content?.get(rowIndex)?.let { dummyData.add(it) }

        }
        return dummyData
    }

    fun getSearchData(context: Context, searchItem: String?): List<UIContent.Page.ContentItems.Content>? {
        val dummyData = ArrayList<UIContent.Page.ContentItems.Content>()
        val data = fetchIntialData(context)
        fetchAllData(context).forEach {data ->
            val dataSize = data.page?.contentItems?.content?.size ?: 0
            for (rowIndex in 0 until dataSize) {
                data.page?.contentItems?.content?.get(rowIndex)?.let {
                    if(searchItem?.let { it1 -> it.name?.contains(it1, ignoreCase = true) } == true) {
                        dummyData.add(it)
                    }
                }

            }
        }
        return dummyData
    }

}