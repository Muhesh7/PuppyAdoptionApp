package com.example.puppyadaption

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.puppyadaption.ui.theme.Shapes

@Composable
fun PuppyList(
    navController: NavController,
    viewModel: puppyViewModel,
) {
    viewModel.getPuppyList()
    val query = remember { mutableStateOf("") }
    val list = viewModel.puppyList.value
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("puppy_post") })
            {
                Icon(
                    Icons.Default.Add,
                    "Add",
                    tint = Color.Black
                )

            }
        },
        content = {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Puppies",
                                color = Color.White
                            )
                        }
                    )
                    LazyColumn(
                        Modifier.fillMaxHeight()
                    ) {
                        itemsIndexed(items = list) { index, doggy ->
                            PuppyCard(navController, doggy)
                        }
                    }
                }
            }
    )
}