package com.example.onsite2

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @POST("login")
    suspend fun Login(@Body user: User) : CheckSuccess

    @POST("signup")
    suspend fun Signup(@Body user: User) : CheckSuccess

    @POST("weather")
    suspend fun PostWeather(@Body cityWeatherPost: CityWeatherPost): CheckSuccess

    @DELETE("weather/{post_id}")
    suspend fun DeleteWeather(@Path("post_id") post_id:Int): CheckSuccess

    @GET("weather/today")
    suspend fun GetAllTodayWeather(@Query("city") city:String) :List<CityWeatherGet>

    @GET("userposts")
    suspend fun GetUsersPosts(@Query("username") username:String):List<CityWeatherGet>

    @GET("weather/history")
    suspend fun GetNData(@Query("city") city:String,@Query("days") days:Int):List<CityWeatherGet>
}