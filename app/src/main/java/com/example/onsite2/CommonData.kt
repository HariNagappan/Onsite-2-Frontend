package com.example.onsite2

import android.health.connect.datatypes.units.Temperature
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

enum class Routes{
                 AUTH_ROUTES,
                    LOGIN,
                    SIGN_UP,
                MAIN_ROUTES,
                    HOME,
}
sealed class AuthUIState {
    object Loading : AuthUIState()
    object Success: AuthUIState()
    data class Failure(val error_msg:String) : AuthUIState()
    object None: AuthUIState()
}

sealed class UIState {
    object Loading : UIState()
    object Success: UIState()
    data class Failure(val error_msg:String) : UIState()
    object None: UIState()
}

data class CityWeatherGet(
    val post_id: Int,
    val city: String,
    val pincode:Int,
    val temperature: Float,
    val humidity: Int,
    val created_at:String
)
data class CityWeatherPost(
    val username: String,
    val city: String,
    val pincode:Int,
    val temperature: Float,
    val humidity: Int
)
data class User(
    val username:String,
    val password:String
)
data class CheckSuccess(val success:Boolean,val error_msg:String)
val BASE_URL="http://192.168.1.33:5000"
val client= OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .build()

val retrofit= Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
val api=retrofit.create(APIService::class.java)
