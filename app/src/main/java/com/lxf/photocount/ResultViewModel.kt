package com.lxf.photocount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxf.photocount.http.Http
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ResultViewModel : ViewModel() {

    val photoPath = MutableLiveData<String>()
    val result = MutableLiveData<Int>()

    fun errorUp(file: File) {
        photoPath.value = file.absolutePath.toString()
        viewModelScope.launch {
            try {
                val map = Http.api.errorUp(file.name)
                result.value = map["result"]?.toIntOrNull()?:0
            } catch (e: Exception) {
                e.printStackTrace()
                result.value = 0
            }
        }
    }
}