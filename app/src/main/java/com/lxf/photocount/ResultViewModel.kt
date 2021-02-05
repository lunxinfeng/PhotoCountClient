package com.lxf.photocount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.izis.bean.SituationResult
import com.google.gson.Gson
import com.lxf.photocount.http.Http
import com.lxf.photocount.http.RequestModel
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ResultViewModel : ViewModel() {

    val photoPath = MutableLiveData<String>()
    val errorUpResult = MutableLiveData<Int>()
    val situationResult = MutableLiveData<SituationResult>()

    fun errorUp(file: File) {
        photoPath.value = file.absolutePath.toString()
        viewModelScope.launch {
            try {
                val map = Http.api.errorUp(file.name)
                errorUpResult.value = map["result"]?.toIntOrNull() ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
                errorUpResult.value = 0
            }
        }
    }

    fun situation(yzSgf: String, boardSize: Int = 19) {
        viewModelScope.launch {
            try {
                val requestModel = RequestModel().apply {
                    code = "010113"
                    info = "$boardSize#${yzSgf}#21"
                }
                val responseModel =
                    Http.api.situation("http://golaxy.izis.cn:8110/genmoves", requestModel)
//                    Http.api.situation("http://192.168.168.141:8110/genmoves", requestModel)
                val situationResult =
                    Gson().fromJson<SituationResult>(responseModel.data, SituationResult::class.java)
                this@ResultViewModel.situationResult.value = situationResult
            }catch (e:Exception){
                e.printStackTrace()
                this@ResultViewModel.situationResult.value = null
            }
        }
    }
}