package com.example.puppyadaption

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*

@Composable
fun PuppyPost(
    navController: NavController,
    viewModel: puppyViewModel,
    imageLoader: ActivityResultLauncher<String>,
) {
    Box() {
        val gender = remember { mutableStateOf("Gender") }
        val age = remember { mutableStateOf("") }
        val name = remember { mutableStateOf("") }
        val mail = remember { mutableStateOf("") }
        val otherDesc = remember { mutableStateOf("") }
        val location = remember { mutableStateOf("Location") }
        val puppyBreed = remember { mutableStateOf("puppy_breed") }
        Column {
            var image2 = glider(R.drawable.background).value
            TopAppBar(
                title = {
                    Text(
                        text = "Post Your Puppy",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Search",
                            tint = Color.White
                        )
                    }
                }
            )
            Box {
                if (viewModel.imageUri.value != null) {
                    image2 = glider(viewModel.imageUri.value!!).value
                }
                image2?.let {
                    Image(
                        image2!!.asImageBitmap(),
                        "image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f),
                        contentScale = ContentScale.FillBounds,
                    )
                }
                IconButton(
                    onClick = { imageLoader.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    val image3 = glider(R.drawable.ic_image).value
                    image3?.let {
                        Image(
                            image3.asImageBitmap(),
                            "image",
                        )
                    }
                }

            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 15.dp,
                            end = 15.dp
                        )

                ) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Description",
                        Modifier
                            .padding(5.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Divider(Modifier.padding(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(1.25f)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it },
                            label = { Text(text = "name") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = mail.value,
                            onValueChange = { mail.value = it },
                            label = { Text(text = "mail") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        DropDownMenu(
                            hint = " Country",
                            list = countryList,
                            selectedString = {
                                location.value
                            })
                        Spacer(modifier = Modifier.padding(5.dp))
                        DropDownMenu(hint = "PuppyBreed",
                            list = countryList,
                            selectedString = {
                                puppyBreed.value
                            })
                        Spacer(modifier = Modifier.padding(5.dp))
                        DropDownMenu(hint = "Gender",
                            list = genderList,
                            selectedString = {
                                gender.value
                            })
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = age.value,
                            onValueChange = { age.value = it },
                            label = { Text(text = "Age") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = otherDesc.value,
                            onValueChange = { otherDesc.value = it },
                            label = { Text(text = "Other descriptions") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.25f)
                            .padding(10.dp),
                        onClick = {
                            if (age.value == "" || otherDesc.value == "" || puppyBreed.value == "" || gender.value == "" || location.value == ""
                                || name.value == "") {
                                Log.d("ASSs", "Fill All Fields")
                            } else {
                                val puppy = puppyModel(
                                    "",
                                    puppyBreed.value,
                                    name.value,
                                    age.value,
                                    gender.value,
                                    otherDesc.value,
                                    location.value,
                                    "",
                                    mail.value
                                )
                                puppyPost(puppy, image2!!, viewModel)
                                Log.d("ASSs", "Post")
                            }
                        }
                    ) {
                        Text(text = "Post")
                    }
                }
            }
        }
    }
}

private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    return stream.toByteArray()
}

fun puppyPost(puppyModel: puppyModel, bitmap: Bitmap, viewModel: puppyViewModel) {
    val key = viewModel.getKey()
    val uploadTask: UploadTask = FirebaseStorage.getInstance().reference
        .child("$key.jpeg").putBytes(getBytesFromBitmap(bitmap)!!)
    uploadTask.addOnFailureListener { exception ->
        Log.d("ASSs", exception.message!!)
    }.addOnSuccessListener { taskSnapshot ->
        if (taskSnapshot.metadata != null) {
            if (taskSnapshot.metadata!!.reference != null) {
                val result = taskSnapshot.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    val hashMap = HashMap<String, String>()
                    hashMap["puppy_breed"] = puppyModel.puppy_breed
                    hashMap["owner_name"] = puppyModel.owner_name
                    hashMap["age"] = puppyModel.age
                    hashMap["gender"] = puppyModel.gender
                    hashMap["location"] = puppyModel.location
                    hashMap["mail"] = puppyModel.mail
                    hashMap["other_description"] = puppyModel.other_description
                    hashMap["image_url"] = uri.toString()
                    viewModel.postPuppy(hashMap, key)
                }
            }
        }
    }
}


