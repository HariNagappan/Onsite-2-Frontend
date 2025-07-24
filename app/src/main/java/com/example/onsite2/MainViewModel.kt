package com.example.onsite2

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private var _username= MutableStateFlow("")
    private var _password = MutableStateFlow("")

    val username: StateFlow<String> =_username
    val password: StateFlow<String> =_password

    private var _uiState: MutableStateFlow<AuthUIState> = MutableStateFlow(AuthUIState.None)
    val uiState:StateFlow<AuthUIState> = _uiState

    private var _postUIState: MutableStateFlow<UIState> = MutableStateFlow(UIState.None)
    val postUIState: StateFlow<UIState> = _postUIState

    private var _deleteUIState: MutableStateFlow<UIState> = MutableStateFlow(UIState.None)
    val deleteUIState: StateFlow<UIState> = _deleteUIState

    private var _getNUIState: MutableStateFlow<UIState> = MutableStateFlow(UIState.None)
    val getNUIState: StateFlow<UIState> = _getNUIState



    private var _getAllUIState: MutableStateFlow<UIState> = MutableStateFlow(UIState.None)
    val getAllUIState: StateFlow<UIState> = _getAllUIState


    var deletion_count by mutableStateOf(0)

    val users_posts = mutableStateListOf<CityWeatherGet>()
    val Nposts = mutableStateListOf<CityWeatherGet>()

    val all_city_today =mutableStateListOf<CityWeatherGet>()
    fun SetUsername(newUsername:String){
        _username.value=newUsername
    }
    fun SetPassword(newPassword:String){
        _password.value=newPassword
    }
    fun SetLoadingUIStateToNone(){
        _uiState.value= AuthUIState.None
    }
    fun ResetPostUIState(){
        _postUIState.value= UIState.None
    }
    fun ResetNUIState(){
        _getNUIState.value= UIState.None
    }
    fun ResetGetAllUIState(){
        _getAllUIState.value= UIState.None
    }


    fun Login(){
        viewModelScope.launch {
            try {
                _uiState.value= AuthUIState.Loading
                val response=api.Login(User(username.value,password.value))
                if(response.success){
                    _uiState.value= AuthUIState.Success
                }
                else{
                    _uiState.value= AuthUIState.Failure("${response.error_msg}")
                }
            }
            catch (e: Exception){
                _uiState.value= AuthUIState.Failure("${e.message}")
            }
        }
    }
    fun Signup(){
        viewModelScope.launch {
            try {
                _uiState.value= AuthUIState.Loading
                val response=api.Signup(User(username.value,password.value))
                if(response.success){
                    _uiState.value= AuthUIState.Success
                }
                else{
                    _uiState.value= AuthUIState.Failure(response.error_msg)
                }
            }
            catch (e: Exception){
                _uiState.value= AuthUIState.Failure("${e.message}")
            }
        }
    }

    fun PostCityData(city: String, pincode:Int, temperature: Float, humidity: Int){
        viewModelScope.launch {
            try {
                _postUIState.value = UIState.Loading
                val response = api.PostWeather(
                    CityWeatherPost(
                        city = city,
                        pincode = pincode,
                        temperature = temperature,
                        username = _username.value,
                        humidity = humidity
                    )
                )
                if (response.success) {
                    _postUIState.value = UIState.Success
                } else {
                    _postUIState.value = UIState.Failure(response.error_msg)
                }
            }
            catch (e: Exception){
                _postUIState.value = UIState.Failure("${e.message}")
            }
        }
    }

    fun DeleteCityData(post_id:Int){
        viewModelScope.launch {
            try {
                _deleteUIState.value= UIState.Loading
                val response=api.DeleteWeather(post_id)
                if(response.success){
                    _deleteUIState.value= UIState.Success
                    deletion_count+=1
                }
                else{
                    _deleteUIState.value= UIState.Failure(response.error_msg)
                    deletion_count+=1
                }
            }
            catch (e: Exception){
                _deleteUIState.value=UIState.Failure("${e.message}")
                deletion_count+=1
            }
        }
    }
    fun GetUserPosts() {
        viewModelScope.launch {
            try {
                _deleteUIState.value= UIState.Loading
                val lst=api.GetUsersPosts(username=_username.value)
                users_posts.clear()
                users_posts.addAll(lst)
                _deleteUIState.value= UIState.Success
            }
            catch (e: Exception){
                _deleteUIState.value= UIState.Failure("${e.message}")
            }
        }
    }
    fun GetNData(city:String,days:Int){
        viewModelScope.launch {
            try {
                _getNUIState.value= UIState.Loading
                val lst=api.GetNData(city,days)
                Nposts.clear()
                Nposts.addAll(lst)
                Nposts.forEach { post->
                    Log.d("general","$post")
                }
                _getNUIState.value= UIState.Success
            }
            catch (e: Exception){
                _getNUIState.value= UIState.Failure("${e.message}")
            }
        }
    }

    fun GetAllWeatherForCity(city: String){
        viewModelScope.launch {
            _getAllUIState.value= UIState.Loading
            try {
                val response=api.GetAllTodayWeather(city=city)
                all_city_today.clear()
                all_city_today.addAll(response)
                _getAllUIState.value= UIState.Success
            }
            catch (e: Exception){
                _getAllUIState.value= UIState.Failure("${e.message}")
            }
        }
    }
}