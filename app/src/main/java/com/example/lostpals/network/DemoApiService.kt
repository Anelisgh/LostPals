package com.example.lostpals.network

import com.example.lostpals.data.dto.PostDemoDto
import com.example.lostpals.data.dto.UserDemoDto
import retrofit2.http.GET

// interfata Retrofit care defineste cereri HTTP GET catre un server extern de test/demo
interface DemoApiService {

    // cerere Get catre endpoint-ul posts
    // primeste o lista de postari (PostDemoDto)
    @GET("posts")
    suspend fun getPosts(): List<PostDemoDto>

    // primeste o lista de useri (UserDemoDto)
    @GET("users")
    suspend fun getUsers(): List<UserDemoDto>
}
