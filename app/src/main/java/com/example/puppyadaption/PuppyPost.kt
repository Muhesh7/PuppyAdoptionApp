package com.example.puppyadaption

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import com.example.puppyadaption.ui.theme.Shapes
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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
        val otherDesc = remember { mutableStateOf("") }
        val location = remember { mutableStateOf("Location") }
        val puppyBreed = remember { mutableStateOf("puppy_breed") }
        Column {
            var image2 = glider(R.drawable.ic_launcher_foreground).value
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
                            if (age.value == "" || otherDesc.value == "" || puppyBreed.value == "" || gender.value == "" || location.value == "")
                            {
                                Log.d("ASSs","Fill All Fields")
                            }
                            else{
                                puppyPost(image2!!,viewModel)
                                Log.d("ASSs","Post")
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

fun puppyPost(bitmap: Bitmap, viewModel: puppyViewModel) {
    val key = viewModel.getKey()
    val uploadTask: UploadTask = FirebaseStorage.getInstance().reference
        .child("$key.jpeg").putBytes(getBytesFromBitmap(bitmap)!!)
    uploadTask.addOnFailureListener { exception ->
        Log.d("ASSs",exception.message!!)
    }.addOnSuccessListener { taskSnapshot ->
        if (taskSnapshot.metadata != null) {
            if (taskSnapshot.metadata!!.reference != null) {
                val result = taskSnapshot.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    val hashMap = HashMap<String, String>()
                    hashMap["puppy_breed"] = ""
                    hashMap["owner_name"] = ""
                    hashMap["age"] = ""
                    hashMap["gender"] = "text"
                    hashMap["location"] = "text"
                    hashMap["other_description"] = "text"
                    hashMap["image_url"] = uri.toString()
                    viewModel.postPuppy(hashMap, key)
                }
            }
        }
    }
}


