package com.example.puppyadaption

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.puppyadaption.ui.theme.PuppyAdaptionTheme

class MainActivity : AppCompatActivity() {

    lateinit var viewModel:puppyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(puppyViewModel::class.java)
        setContent {
            val navController = rememberNavController()
            PuppyAdaptionTheme {
                NavHost(navController, startDestination = "puppy_list") {
                    composable("puppy_list") { PuppyList(navController,viewModel) }
                    composable("puppy_post") { PuppyPost(navController,viewModel,imageLoader) }
                    composable("puppy_details/{puppy_id}",
                        listOf(navArgument("puppy_id") { type = NavType.StringType })
                    )
                    { backStack ->
                        PuppyDetail(navController, viewModel,backStack.arguments?.getString("puppy_id"))
                    }
                }
            }
        }
    }
    private val imageLoader = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.imageUri.value = it
    }
}

@Preview("App")
@Composable
fun DefaultPreview() {
    PuppyAdaptionTheme {

    }

}




