package com.lxf.photocount.http

import androidx.lifecycle.ViewModel
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponse<T> {

    companion object {
        val CODE_SUCCESS = 200
        val CODE_ERROR = 1000
    }

    var code = CODE_SUCCESS
    var body: T? = null
    var msg: String? = null

    constructor(response: HttpResponse<T>) {
        if (response.code == 200) {
            this.code = CODE_SUCCESS
            this.body = response.data
        } else {
            this.code = CODE_ERROR
            this.msg = response.msg
        }
    }

    constructor(e: Exception) {
        this.code = CODE_ERROR
        this.msg = e.message
    }

    fun isError() = code == CODE_ERROR
}

suspend fun <T> ViewModel.http(call: suspend () -> HttpResponse<T>): ApiResponse<T> {
    return try {
        ApiResponse(call())
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResponse(e)
    }
}