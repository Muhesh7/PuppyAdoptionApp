package com.example.puppyadaption

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate

@Composable
fun PuppyCard(
    navController: NavController,
    doggy: puppyModel,
) {

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp),
        elevation = 15.dp,
    ) {
        Column(
            Modifier
                .height(100.dp)
                .clickable(onClick = { navController.navigate("puppy_details/${doggy.id}") }),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                doggy.image_url.let { url ->
                    val image = glider(url, R.drawable.ic_launcher_foreground).value
                    image?.let {
                        Image(
                            image.asImageBitmap(),
                            "image",
                            modifier = Modifier
                                .background(color = Color.Blue)
                                .align(Alignment.CenterVertically)
                                .weight(3f),
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                        .weight(5f)
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        text = "DogBreed: ${doggy.puppy_breed}",
                        style = TextStyle( fontSize = 14.sp)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Location: ${doggy.location}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(fontSize = 13.sp)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "OwnedBy: ${doggy.owner_name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(color = Color.Gray, fontSize = 12.sp)
                    )
                }

                Text(
                    text = "Tap to Read More",
                    style = TextStyle(fontSize = 8.sp),
                    maxLines = 1,
                    softWrap = false,
                    modifier = Modifier.align(Alignment.Bottom)
                               .weight(2.5f)
                               .padding(5.dp)
                )
            }
        }
    }
}
