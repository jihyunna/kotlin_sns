package com.example.snsproject.navigation.util

import com.example.snsproject.navigation.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.squareup.okhttp.MediaType
import okhttp3.*
import java.io.IOException

class FcmPush {
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "http://fcm.googleapis/fcm/send"
    var serverKey = "AIzaSyBjMi9uDdCkMM8KGnwNjPzA0Ky15DYdaNg" //2022. 11. 28. PM 9:26:38
    var gson: Gson? = null
    var okHttpClient : okhttp3.OkHttpClient? = null
    companion object{
        var instance = FcmPush()
    }


    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid : String, title : String, message : String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get().addOnCompleteListener {
            task ->
            if(task.isSuccessful) {
                var token = task?.result?.get("pushToken").toString()
                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification.title = title
                pushDTO.notification.body = message

                var body = RequestBody.create(JSON,gson?.toJson(pushDTO))
                var request = Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key="+serverKey)
                    .url(url)
                    .post(body)
                    .build()

                okHttpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response.body?.string())
                    }
                })
            }
        }
    }
}