package com.lxf.photocount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxf.photocount.http.Http
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class PhotoViewModel: ViewModel() {

    val photoPath = MutableLiveData<String>()
    val chess = MutableLiveData<String>()

    fun photoCount(file:File){
        photoPath.value = file.absolutePath.toString()
        viewModelScope.launch {
            try {
                val map = Http.photoCount(file)
                chess.value = map["chess"]
            }catch (e:Exception){
                e.printStackTrace()
                chess.value = ""
            }
        }
    }
}