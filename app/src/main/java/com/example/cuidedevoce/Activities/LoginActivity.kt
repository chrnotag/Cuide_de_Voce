package com.example.cuidedevoce.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cuidedevoce.Activities.Main.MainActivity
import com.example.cuidedevoce.Help.sampleSnackbar
import com.example.cuidedevoce.R
import com.example.cuidedevoce.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
       ActivityLoginBinding.inflate(layoutInflater)
    }

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.logarButton.setOnClickListener {
            logaruser()
        }

        binding.registerLogin.setOnClickListener {
            register()
        }

        setContentView(binding.root)
    }

    private fun logaruser() {
        when ("") {
            binding.emailLogin.text.toString() -> {
                sampleSnackbar
                    .sampleSnackbar(
                        "Insira algum email para entrar",
                        binding.viewlogin
                    )
            }
            binding.senhaLogin.text.toString() -> {
                sampleSnackbar
                    .sampleSnackbar(
                        "O campo de senha não pode estar vazio",
                        binding.viewlogin
                    )
            }
            else -> {
                validarLogin()
            }
        }
    }

    private fun validarLogin() {
        val email = binding.emailLogin.text.toString()
        val senha = binding.senhaLogin.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "Email ou senha incorretos",
                                    binding.viewlogin
                                )
                        }

                        is FirebaseNetworkException -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "Erro de Rede",
                                    binding.viewlogin
                                )
                        }

                        is FirebaseAuthInvalidUserException -> {
                            val snack = Snackbar.make(
                                binding.viewlogin,
                                "Poxa, parece que você ainda não possui conta.",
                                Snackbar.LENGTH_SHORT
                            )

                            snack.setAction("registre-se") {
                                register()
                            }

                            snack.show()
                        }

                        else -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "Insira algum email para entrar",
                                    binding.viewlogin
                                )
                        }
                    }
                }
            }
    }

    private fun register() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}