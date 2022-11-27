package com.example.snsproject.navigation.util

import com.google.firebase.firestore.FirebaseFirestore
//import com.example.snsproject.navigation.model.PushDTO
import com.squareup.okhttp.MediaType
import java.io.IOException

class FcmPush {
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "http://fcm.googleapis/fcm/send"
    var serverKey = ""
}