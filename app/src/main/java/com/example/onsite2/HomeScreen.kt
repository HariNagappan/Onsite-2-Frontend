package com.example.onsite2

import android.R.attr.label
import android.R.attr.onClick
import android.R.attr.singleLine
import android.graphics.Outline
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    val tabs = remember{ mutableStateListOf("POST","GET all","Post N","DELETE") }
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
                0->{
                    PostCityData(mainViewModel=mainViewModel)}
                1->{
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
                        if(new_text.toInt()<=100)
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
                mainViewModel.ResetPostUIState()
                Toast.makeText(context,(postUIState.value as UIState.Failure).error_msg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loading -> {
                CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
            }
            is UIState.None -> {

            }
            is UIState.Success -> {
                mainViewModel.ResetPostUIState()
                Toast.makeText(context,"Posted Data Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
@Composable
fun DeleteData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){
    val uistate by mainViewModel.deleteUIState.collectAsState()
    LaunchedEffect(mainViewModel.deletion_count) {
        mainViewModel.GetUserPosts()
    }
    Box(modifier= Modifier.fillMaxSize()) {
        when (uistate) {
            is UIState.None -> {Text("Nothing",modifier=Modifier.align(Alignment.Center))}
            is UIState.Failure -> {
                Button(onClick = {
                    mainViewModel.GetUserPosts()
                },
                    modifier=Modifier.align(Alignment.Center)
                    ) {
                    Text("Could not fetch Data,Retry")
                }

            }

            is UIState.Loading -> {CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))}
            is UIState.Success -> {
                if(mainViewModel.users_posts.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(
                            text="You didn't post any data yet",
                            modifier=Modifier.align(Alignment.Center)
                        )
                    }
                }
                else {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mainViewModel.users_posts.forEach { user_post ->
                            DataCard(user_post, showDelete = true, {
                                mainViewModel.DeleteCityData(post_id = user_post.post_id)

                            })
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GetNData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){
    var numberOfDays by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    val uistate by mainViewModel.getNUIState.collectAsState()
    Box(modifier=Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Log.d("general","uistate:${uistate.javaClass.name}")
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                },
                label = { Text("Enter city name:") },
                singleLine = true,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = numberOfDays,
                    onValueChange = {
                        numberOfDays = it
                    },
                    label = { Text("Enter Number of Days") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    mainViewModel.GetNData(city, numberOfDays.toInt())
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
            Box(modifier=Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (uistate) {
                        is UIState.Failure -> {
                            mainViewModel.ResetNUIState()
                            Log.d("general","${(uistate as UIState.Failure).error_msg}")
                            Toast.makeText(LocalContext.current,"Failed to make text${(uistate as UIState.Failure).error_msg}",Toast.LENGTH_SHORT).show()}
                        is UIState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is UIState.None -> {Text("Nothing")
                            Toast.makeText(LocalContext.current,"Nothing to make text",Toast.LENGTH_SHORT)}
                        is UIState.Success -> {
                            mainViewModel.ResetNUIState()
                            mainViewModel.Nposts.forEach { npost->
                                DataCard(npost, showDelete = false, onDeleteClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GetAllData(mainViewModel: MainViewModel,modifier: Modifier=Modifier){
    var city by remember { mutableStateOf("") }
    val uistate by mainViewModel.getAllUIState.collectAsState()
    Box(modifier=Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Log.d("general","uistate:${uistate.javaClass.name}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {
                        city = it
                    },
                    label = { Text("Enter City Name:") },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    mainViewModel.GetAllWeatherForCity(city)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
            Box(modifier=Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (uistate) {
                        is UIState.Failure -> {
                            mainViewModel.ResetGetAllUIState()
                            Log.d("general","${(uistate as UIState.Failure).error_msg}")
                            Toast.makeText(LocalContext.current,"Error:${(uistate as UIState.Failure).error_msg}",Toast.LENGTH_SHORT).show()}
                        is UIState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is UIState.None -> {
                            Text("Nothing")
                        }
                        is UIState.Success -> {
                            mainViewModel.ResetGetAllUIState()
                            mainViewModel.all_city_today.forEach { city_today->
                                DataCard(city_today, showDelete = false, onDeleteClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DataCard(weatherGet: CityWeatherGet,showDelete:Boolean=false,onDeleteClick:()->Unit){
    Card(modifier=Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier=Modifier.fillMaxWidth().padding(8.dp)
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
            Text(
                "created at(UTC): "+weatherGet.created_at
            )

            if(showDelete){
                IconButton(
                    onClick={
                        onDeleteClick()
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

