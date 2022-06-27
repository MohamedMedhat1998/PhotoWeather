package com.mohamed.medhat.photoweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.photoweather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main screen of the app.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}