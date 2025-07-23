package com.example.onsite2

import android.R.attr.label
import android.R.attr.onClick
import android.graphics.Outline
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(mainViewModel: MainViewModel,navController: NavController,modifier: Modifier =Modifier){
    val tabs = remember{ mutableStateListOf("POST","GET all","Past N","DELETE") }
    var cur_selected by remember { mutableStateOf(0) }
    Box(modifier=Modifier.fillMaxSize()){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(top = 40.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)) {
            Row(modifier=Modifier.fillMaxWidth()) {
                tabs.forEach { tab ->
                    val isSelected=(cur_selected==tabs.indexOf(tab))
                    TextButton(onClick = {
                        cur_selected=tabs.indexOf(tab)
                    }) {
                        Text(
                            text=tab,
                            fontSize = 18.sp,
                            color = if(isSelected) Color.Green else Color.Black,
                            modifier=Modifier
                                .border(
                                    width = 2.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    color = if (isSelected) Color.Red else Color.Transparent
                                )
                                .padding(6.dp)
                        )
                    }
                }
            }
            when(cur_selected){
                0->{mainViewModel.ResetPostUIState()
                    PostCityData(mainViewModel=mainViewModel)}
                1->{
                    mainViewModel.GetUserPosts()
                    GetAllData(mainViewModel=mainViewModel)}
                2->{GetNData(mainViewModel=mainViewModel)}
                3->{DeleteData(mainViewModel=mainViewModel)}
            }
        }
    }
}
@Composable
fun PostCityData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){
    var city by rememberSaveable { mutableStateOf("") }
    var temperature by rememberSaveable { mutableStateOf("") }
    var humidity by rememberSaveable { mutableStateOf("") }
    var pincode by rememberSaveable { mutableStateOf("") }
    var postUIState = mainViewModel.postUIState.collectAsState()
    val context= LocalContext.current
    Box(modifier=modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { new_text ->
                    city = new_text
                },
                singleLine = true,
                label = { Text("Enter City") },
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = temperature,
                    onValueChange = { new_text ->
                        temperature = new_text

                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Enter Temperature") },
                )
                Text(
                    text = "C",
                    fontSize = 24.sp
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = humidity,
                    onValueChange = { new_text ->
                        humidity = new_text
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Enter Humidity") },
                )
                Text(
                    text = "%",
                    fontSize = 24.sp
                )
            }
            OutlinedTextField(
                value = pincode,
                onValueChange = { new_text ->
                    if (new_text.length <= 6)
                        pincode = new_text
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Enter Pincode") },
            )
            Button(onClick = {
                mainViewModel.PostCityData(
                    city = city,
                    pincode = pincode.toInt(),
                    temperature = temperature.toFloat(),
                    humidity = humidity.toInt()
                )
            }) {
                Text("Post City Data")
            }
        }
        when(postUIState.value){
            is UIState.Failure -> {
                Toast.makeText(context,(postUIState.value as UIState.Failure).error_msg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loading -> {
                CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
            }
            is UIState.None -> {

            }
            is UIState.Success -> {
                Toast.makeText(context,"Posted Data Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
@Composable
fun GetAllData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        mainViewModel.users_posts.forEach { user_post->
            DataCard(mainViewModel, user_post)
        }
    }
}
@Composable
fun GetNData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){

}
@Composable
fun DeleteData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){

}
@Composable
fun DataCard(mainViewModel: MainViewModel, weatherGet: CityWeatherGet,showDelete:Boolean=false){
    Card(modifier=Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier=Modifier.fillMaxWidth()
        ) {
            Text(
                "post_id:"+weatherGet.post_id
            )
            Text(
                "city:"+weatherGet.city
            )
            Text(
                "temperature:"+weatherGet.temperature.toString()
            )
            Text(
                "humidity:"+weatherGet.humidity
            )
            Text(
                "pincode:"+weatherGet.pincode
            )
            if(showDelete){
                IconButton(
                    onClick={
                        mainViewModel.DeleteCityData(post_id = weatherGet.post_id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

