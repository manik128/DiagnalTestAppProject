package com.app.diagnaltestapp.ui

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.diagnaltestapp.R
import com.app.diagnaltestapp.data.PAGE_1
import com.app.diagnaltestapp.data.UIContent
import com.app.diagnaltestapp.utils.ReadJSONFromAssets
import com.app.diagnaltestapp.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = ReadJSONFromAssets(this, PAGE_1)
        val data = Gson().fromJson(json, UIContent::class.java)
        setContent {
            val showAppBar = remember { mutableStateOf(true) }
            val isHomeView = remember { mutableStateOf(true) }
            OnBackPressed(isHomeView)
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            MyAppTheme {
                Scaffold(
                    topBar = {
                        if (showAppBar.value) {
                            CustomTopAppBar(
                                title = "${data.page?.title}",
                                onNavigationIconClick = { /* Handle navigation icon click */ },
                                actions = {
                                    IconButton(onClick = { isHomeView.value = false }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search),
                                            contentDescription = "Action"
                                        )
                                    }
                                },
                         scrollBehavior = scrollBehavior
                                )
                        }
                    }

                ) { padding ->
                    // 4
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Black
                            ) {
                                MyFragmentComposable(isHomeView, showAppBar, padding)
                            }

                }
            }
        }
    }
}


@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    MaterialTheme( content = content)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBackPressed(isHomeView: MutableState<Boolean>) {
    Log.d("read", " OnBackPressed  =")
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val backPressHandled = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = !backPressHandled.value) {
        println("back pressed")
        backPressHandled.value = true
        coroutineScope.launch {
            awaitFrame()
            if (isHomeView.value) {
                onBackPressedDispatcher?.onBackPressed()
            }
            isHomeView.value = true
            backPressHandled.value = false
        }
    }
}

@Composable
fun MyFragmentComposable(
    isHomeView: MutableState<Boolean>,
    showAppBar: MutableState<Boolean>,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), contentAlignment = Alignment.Center
    ) {
        // First composable

        if (isHomeView.value) {
            showAppBar.value = true
            HomeView()
        } else {
            showAppBar.value = false
            SearchContainerView(isHomeView)
        }
    }
}
