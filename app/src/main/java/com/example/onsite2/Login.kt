package com.example.onsite2

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Login(mainViewModel: MainViewModel,navController: NavController,modifier: Modifier =Modifier){
    var username by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Box(modifier= Modifier.fillMaxSize()) {
        Column(modifier=Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            Text(
                text="LOGIN",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { new_text ->
                        username = new_text
                        mainViewModel.SetUsername(newUsername = new_text)
                    },
                    singleLine = true,
                    label = { Text("Enter Username") }
                )
                OutlinedTextField(
                    value = password,
                    singleLine = true,
                    onValueChange = { new_text ->
                        password = new_text
                        mainViewModel.SetPassword(newPassword = new_text)
                    },
                    label = { Text("Enter Password") }
                )
                TextButton(
                    onClick = {
                        navController.navigate(Routes.SIGN_UP.name)
                        mainViewModel.SetLoadingUIStateToNone()
                    },
                    modifier=Modifier.align(Alignment.Start)
                ) {
                    Text(
                        text="Don't have account? Sign Up"
                    )
                }
            }
            Button(
                onClick = {
                    mainViewModel.Login()
                }
            ) {
                Text(
                    text="LOGIN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        when(uiState){
            is AuthUIState.None -> {}
            is AuthUIState.Loading -> {
                CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
            }
            is AuthUIState.Failure -> {
                Toast.makeText(context, (uiState as AuthUIState.Failure).error_msg, Toast.LENGTH_SHORT).show()
            }
            is AuthUIState.Success ->{
                LaunchedEffect(uiState) {
                    navController.navigate(Routes.MAIN_ROUTES.name){
                        popUpTo(Routes.AUTH_ROUTES.name){
                            inclusive=true
                        }
                    }
                }
            }
        }
    }
}
