package com.example.cuidedevoce.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cuidedevoce.Activities.Main.MainActivity
import com.example.cuidedevoce.Help.verifyNetworkConnection
import com.example.cuidedevoce.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verificarLogin()
        setContentView(binding.root)
    }

    fun verificarLogin() {
        if (verifyNetworkConnection.isConnected(this)) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        } else {
            Toast
                .makeText(
                    this,
                    "Sem conexão com a internet",
                    Toast.LENGTH_SHORT
                )
                .show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}