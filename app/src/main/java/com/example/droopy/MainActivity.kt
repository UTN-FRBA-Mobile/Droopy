package com.example.droopy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.droopy.ui.login.ui.LoginScreen
import com.example.droopy.ui.login.ui.LoginViewModel
import com.example.droopy.ui.searches.SearchInfoScreen
import com.example.droopy.ui.theme.DroopyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroopyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(LoginViewModel(), navigateToSearchInfo = { navController.navigate("searchInfo") })
                        }
                        composable("searchInfo") {
                            //TODO: add argument
                            SearchInfoScreen(searchId = "test")
                        }
                    }
                }
            }
        }
    }
}
