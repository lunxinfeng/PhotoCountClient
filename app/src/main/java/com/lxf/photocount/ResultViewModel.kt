package com.lxf.photocount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.izis.bean.SituationResult
import com.google.gson.Gson
import com.lxf.photocount.http.ApiResponse
import com.lxf.photocount.http.Http
import com.lxf.photocount.http.RequestModel
import com.lxf.photocount.http.http
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ResultViewModel : ViewModel() {

    val photoPath = MutableLiveData<String>()
    val situationResult = MutableLiveData<SituationResult>()
    val toastMessage = MutableLiveData<String>()

    fun errorUp(file: File) {
        photoPath.value = file.absolutePath.toString()
        viewModelScope.launch {
            val data = http {
                Http.api.errorUp(file.name)
            }
            if (data.code == ApiResponse.CODE_ERROR){
                toastMessage.value = data.msg
                return@launch
            }
            toastMessage.value = "上报成功"
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
                toastMessage.value = e.message
                this@ResultViewModel.situationResult.value = null
            }
        }
    }
}