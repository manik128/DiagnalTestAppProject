package com.app.diagnaltestapp.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
//import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.diagnaltestapp.R
import com.app.diagnaltestapp.data.DataStore
import com.app.diagnaltestapp.utils.Utils
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeView() {
    UIItemList()
}



@Composable
fun getImageResource(value: String): Int {
    val context = LocalContext.current

    val resources: Resources = context.resources
    return resources.getIdentifier(
        Utils.removeFileExtension(value), "drawable",
        context.packageName
    )
}

@Composable
fun UIItemList() {
    val scrollState = rememberLazyGridState()
    var rowIndex = 0;
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var data by remember { mutableStateOf(DataStore.getIntialData(context)) }
    val configuration = LocalConfiguration.current
    val cellSize = if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 7 else 3
    LazyVerticalGrid(columns = GridCells.Fixed(cellSize),
        state = scrollState,
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(count = data.size) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                   Log.d("read"," valuesize = ${data.size}")
                    Image(
                        painterResource(id = getImageResource( "${data[it].posterImage}")),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(end = 10.dp), //Remove the offset here
                        alignment = Alignment.Center,
                        contentDescription = ""
                    )
                    Text(
                        text = "${data[it].name}",
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
                    if (index >= data.size - 1 && rowIndex < 2) {
                        scope.launch {
                            rowIndex++
                            data += DataStore.getNextData(rowIndex, context)
                        }
                    }
        }
    }
}