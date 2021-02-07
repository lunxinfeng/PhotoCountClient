package com.lxf.photocount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxf.photocount.http.Http
import com.lxf.photocount.http.http
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class PhotoViewModel: ViewModel() {

    val photoPath = MutableLiveData<String>()
    val chess = MutableLiveData<String>()

    fun photoCount(file:File){
        photoPath.value = file.absolutePath.toString()
        viewModelScope.launch {
            val data = http {
                Http.photoCount(file)
            }
            if (data.isError()){
                chess.value = ""
                return@launch
            }
            chess.value = data.body!!["chess"]
        }
    }
}