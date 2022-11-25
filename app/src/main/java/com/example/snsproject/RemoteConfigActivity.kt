package com.example.snsproject

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.snsproject.databinding.ActivityRemoteConfigBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class RemoteConfigActivity : AppCompatActivity() {
    companion object {
        const val imgSpringURL = "gs://sns-project-2022.appspot.com/season/spring.jpg"
        const val imgSummerURL = "gs://sns-project-2022.appspot.com/season/summer.jpg"
        const val imgFallURL = "gs://sns-project-2022.appspot.com/season/fall.jpg"
        const val imgWinterURL = "gs://sns-project-2022.appspot.com/season/winter.jpg"
    }

    private val remoteConfig = Firebase.remoteConfig
    private lateinit var binding: ActivityRemoteConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoteConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1 // For test purpose only, 3600 seconds for production
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.rc_defaults)

        binding.buttonFetchActivate.setOnClickListener {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) {
                    val yourPrice = remoteConfig.getLong("your_price")
                    val cheatEnabled = remoteConfig.getBoolean("cheat_enabled")
                    binding.textYourPrice.text = yourPrice.toString()
                    binding.textCheatEnabled.text = cheatEnabled.toString()
                }
        }

        refreshImage()

        binding.refresh.setOnClickListener {
            refreshImage()
        }
    }

    private fun refreshImage() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val season = remoteConfig.getString("season")
                    val imgRef = when (season) {
                        "spring" -> Firebase.storage.getReferenceFromUrl(imgSpringURL)
                        "summer" -> Firebase.storage.getReferenceFromUrl(imgSummerURL)
                        "fall" -> Firebase.storage.getReferenceFromUrl(imgFallURL)
                        else -> Firebase.storage.getReferenceFromUrl(imgWinterURL)
                    }
                    displayImageRef(imgRef, binding.imageView)
                }
            }
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }
}