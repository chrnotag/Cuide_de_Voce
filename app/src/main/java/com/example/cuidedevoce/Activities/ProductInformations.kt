package com.example.cuidedevoce.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cuidedevoce.databinding.InformationsProductBinding

class ProductInformations : AppCompatActivity() {

    private val binding by lazy {
        InformationsProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }
}