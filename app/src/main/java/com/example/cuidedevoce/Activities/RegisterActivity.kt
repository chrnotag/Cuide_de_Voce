package com.example.cuidedevoce.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.cuidedevoce.Activities.Main.MainActivity
import com.example.cuidedevoce.Help.sampleSnackbar
import com.example.cuidedevoce.R
import com.example.cuidedevoce.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Registre-se"

        binding.registrar.setOnClickListener {
            verificarRegistro()
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
        return true
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun verificarRegistro() {
        when ("") {
            binding.emailRegister.text.toString() -> {
                sampleSnackbar
                    .sampleSnackbar(
                        "Insira algum email para entrar",
                        binding.registrar
                    )
            }
            binding.passRegister.text.toString() -> {
                sampleSnackbar
                    .sampleSnackbar(
                        "O campo de senha não pode estar vazio.",
                        binding.registrar
                    )
            }
            binding.repassRegister.text.toString() -> {
                sampleSnackbar
                    .sampleSnackbar(
                        "Confirme sua senha.",
                        binding.registrar
                    )
            }
            else -> {
                registrar()
            }
        }
    }

    private fun registrar() {
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(
                binding.emailRegister.text.toString(),
                binding.repassRegister.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    when (it.exception) {
                        is FirebaseAuthEmailException -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "Insira algum email válido",
                                    binding.registrar
                                )
                        }
                        is FirebaseNetworkException -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "Erro na Rede",
                                    binding.registrar
                                )
                        }
                        is FirebaseAuthUserCollisionException -> {
                            val snack = Snackbar.make(
                                binding.imageView2,
                                "Email já registrado.",
                                Snackbar.LENGTH_SHORT
                            )

                            snack.setAction("Faça o login!") {
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                            snack.show()
                        }

                        is FirebaseAuthWeakPasswordException -> {
                            sampleSnackbar
                                .sampleSnackbar(
                                    "A senha deve ter no minimo 6 caracteres!",
                                    binding.registrar
                                )
                        }
                    }
                }
            }
    }
}