package com.lxf.photocount.http

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.util.concurrent.TimeUnit

interface Api {
    @Multipart
    @POST("/photo_count/")
    suspend fun photoCount(@Part part: MultipartBody.Part): Map<String, String>
}

object Http {
    private val logger = LoggerFactory.getLogger("网络请求")
    private val client = OkHttpClient.Builder()
        .connectTimeout(8000, TimeUnit.MILLISECONDS)
        .addInterceptor(HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    logger.debug(message)
                }
            }
        ).apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
    private val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
//        .baseUrl("http://192.168.168.145:8888/")
        .baseUrl("http://116.62.153.52:8888/")
        .build()

    val api: Api = retrofit.create(Api::class.java)

    suspend fun photoCount(file: File): Map<String, String> {
        val requestBody = file.asRequestBody(MultipartBody.FORM)
        val part = MultipartBody.Part.createFormData("image",file.name,requestBody)

        return api.photoCount(part)
    }
}