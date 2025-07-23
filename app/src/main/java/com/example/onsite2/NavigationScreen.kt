package com.example.onsite2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

@Composable
fun NavigationScreen(mainViewModel: MainViewModel= viewModel(),navController: NavHostController= rememberNavController(),modifier:Modifier=Modifier){
    NavHost(navController=navController, startDestination = Routes.AUTH_ROUTES.name, modifier = Modifier.fillMaxSize()){
        navigation(startDestination = Routes.LOGIN.name,route=Routes.AUTH_ROUTES.name){
            composable(route=Routes.LOGIN.name) {
                Login(mainViewModel=mainViewModel,navController=navController)
            }
            composable(route=Routes.SIGN_UP.name) {
                Signup(mainViewModel=mainViewModel,navController=navController)
            }
        }
        navigation(startDestination = Routes.HOME.name,route=Routes.MAIN_ROUTES.name) {
            composable(route =Routes.HOME.name) {
                HomeScreen(
                    mainViewModel=mainViewModel,
                    navController=navController
                    )
            }
        }
    }
}