package com.lxf.photocount.http

data class HttpResponse<T>(val code: Int, val data: T, val msg: String)