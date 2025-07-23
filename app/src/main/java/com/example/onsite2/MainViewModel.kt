package com.example.onsite2

import androidx.compose.runtime.mutableStateListOf
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


    val users_posts = mutableStateListOf<CityWeatherGet>()
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
    fun ResetDeleteUIState(){
        _deleteUIState.value= UIState.None
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
                }
                else{
                    _deleteUIState.value= UIState.Failure(response.error_msg)
                }
            }
            catch (e: Exception){
                _deleteUIState.value=UIState.Failure("${e.message}")
            }
        }
    }
    fun GetUserPosts(){
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
}