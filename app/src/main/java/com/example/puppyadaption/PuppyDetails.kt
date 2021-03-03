package com.example.puppyadaption

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
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

@Composable
fun PuppyDetail(
    navController: NavController,
    viewModel: puppyViewModel,
    id: String?
) {
    if(id != null) {
        viewModel.getPuppyDetails(id)
        val doggy = viewModel.puppyDetail.value
        Column {
            Box(
                modifier = Modifier
                    .background(color = Color.Blue)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .weight(1f)
            ) {
                doggy.image_url.let { url ->
                    val image = glider(url, R.drawable.ic_launcher_foreground).value
                    image?.let {
                        Image(
                            image.asImageBitmap(),
                            "image",
                            Modifier.fillMaxWidth()
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                }
                IconButton(
                    onClick = { navController.navigateUp() },
                    Modifier.align(Alignment.TopStart),
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        "Search",
                        tint = Color.White
                    )
                }

            }
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(2f),
                elevation = 5.dp,
            ) {
                Column(
                    modifier = Modifier
                    // .background(Color.White),
                ) {
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row(
                        modifier = Modifier
                    ) {
                        IconButton(
                            onClick = { },
                            Modifier.weight(1f),
                        ) {
                            Icon(
                                Icons.Default.MailOutline,
                                "Search",
                                tint = Color.Black
                            )
                        }
                        IconButton(
                            onClick = { },
                            Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Phone,
                                "Search",
                                tint = Color.Black
                            )
                        }

                    }
                    Divider(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Owner:  ${doggy.owner_name}",
                        modifier = Modifier
                            .padding(5.dp),
                        style = TextStyle(fontSize = 14.sp)
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    Divider(modifier = Modifier.padding(5.dp))
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        text = "DogBreed: ${doggy.puppy_breed}",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Text(
                        text = "Location: ${doggy.location}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Description: ${doggy.other_description}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .wrapContentHeight(),
                        style = TextStyle(color = Color.Black, fontSize = 17.sp)
                    )
                }
            }
        }
    }
}