package com.app.diagnaltestapp.ui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
//import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.diagnaltestapp.R
import com.app.diagnaltestapp.data.DataStore
import com.app.diagnaltestapp.data.UIContent
import com.app.diagnaltestapp.utils.Utils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContainerView(isHomeView: MutableState<Boolean>/*grid: List<UIContent.Page.ContentItems.Content>,*/ /*paddingValues: PaddingValues*/) {
    var textState by remember { mutableStateOf(TextFieldValue()) }
    var searchText:String? by remember { mutableStateOf(null) }
    val focusRequester = remember { FocusRequester() }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = textState,
            onValueChange = { nextText ->
                Log.d("read","text=="+nextText.composition?.length)
                textState = nextText },
            modifier = Modifier.fillMaxWidth().padding(16.dp).focusRequester(focusRequester),
            trailingIcon = {
                IconButton(onClick = { isHomeView.value = true}) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_cancel),
                        contentDescription = "Action",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(

                // below line is to enable auto
                // correct in our keyboard.
                autoCorrect = true,

                // below line is used to specify our
                // type of keyboard such as text, number, phone.
                keyboardType = KeyboardType.Text,
            ),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Black,cursorColor = Color.White,
                focusedIndicatorColor = Color.White, textColor = Color.White),
            textStyle = Utils.getTextStyle()
        )

        if(textState.composition?.length?:0 >= 3) {
            textState.text?.let {
                searchText = it
            }
        }
        LazyVerticalGridItem(searchText)
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

}





@Composable
fun LazyVerticalGridItem(searchItem: String?) {
    val scrollState = rememberLazyGridState()
    val isScrolled = remember {
        mutableStateOf(false)
    }
    var rowIndex = 0;
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
  //  var data by remember { mutableStateOf(DataStore.getSearchData(context, searchItem)) }
    var data  =  DataStore.getSearchData(context, searchItem)

    val configuration = LocalConfiguration.current
    val cellSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 7 else 3
    Log.d("read"," data = ${data?.size}")
    LazyVerticalGrid(columns = GridCells.Fixed(cellSize),
        state = scrollState,
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(count = data?.size?:0) {
                   Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                        Image(
                            painterResource(id = getImageResource("${data?.get(it)?.posterImage}")),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(end = 10.dp), //Remove the offset here
                            alignment = Alignment.Center,
                            contentDescription = ""
                        )
                        Text(
                            text = "${data?.get(it)?.name}",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.align(Start).offset(0.dp, (-10).dp),
                            style = Utils.getTextStyle()
                        )
                    }
            }
        })
    // Observe scroll state changes
    LaunchedEffect(scrollState) {
          snapshotFlow { scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
                .collect { index ->
                    isScrolled.value = index > 0
                    if (index >= data?.size?:0 - 1 && rowIndex < 2) {
                        scope.launch {
                            Log.d("read", "scrolling2..." + rowIndex)
                            rowIndex++
                            data = data?.plus(DataStore.getNextData(rowIndex, context))
                        }
                    }
        }
    }

    if (isScrolled.value) {
        // Do something when scrolled
        Log.d("read","scrolling...")
    }
}