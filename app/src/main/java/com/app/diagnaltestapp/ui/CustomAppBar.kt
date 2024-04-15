package com.app.diagnaltestapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.diagnaltestapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior
) {
    DefaultTopAppBar(
        title,
        onNavigationIconClick,
        actions,
        scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior?
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back"
                )
            }
        },

        actions = actions,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent, titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
        ),
        scrollBehavior = scrollBehavior,

        )


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CustomAppBarPreview() {
    DefaultTopAppBar(
        "",
        onNavigationIconClick = {},
        actions = {},
        scrollBehavior = null
    )
}














