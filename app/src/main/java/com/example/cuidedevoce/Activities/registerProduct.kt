package com.example.cuidedevoce.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.cuidedevoce.Activities.Main.MainActivity
import com.example.cuidedevoce.dataClasses.productInfos
import com.example.cuidedevoce.databinding.ActivityRegisterProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class registerProduct : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterProductBinding.inflate(layoutInflater)
    }

    private val uID = FirebaseAuth.getInstance().currentUser?.uid

    var nomeproduto: String = ""
    var datavalidade: String = "Não Informado"
    var valor: Double = 0.0
    var dataCompra = ""
    var img_url = ""
    val img_url_padrao =
        "https://firebasestorage.googleapis.com/v0/b/cuide-de-voce.appspot.com/o/sem_imagem.png?alt=media&token=4b1147f6-6be2-4ccf-ac5e-af97d4bf6480"

    private val register = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { image: Bitmap? ->
        image?.let {
            binding.imageProduct.setImageBitmap(image)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Registrar Produto"
        supportActionBar?.isHideOnContentScrollEnabled

        abrirCamera()

        binding.saveProduct.setOnClickListener {
            salvarImagemProduto()
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun registrarProduto() {

        nomeproduto = binding.nameRegisterProduct.text.toString()
        datavalidade = binding.dueDate.text.toString()
        dataCompra = DataAtual()
        valor = binding.valueRegisterProduct.text.toString().toDouble()
        FirebaseFirestore.getInstance().collection(uID.toString())
            .document("produtossalvos")
            .collection("produtossalvos")
            .add(
                productInfos(
                    nomeproduto,
                    dataCompra,
                    datavalidade,
                    valor,
                    img_url
                )
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }

    }

    private fun abrirCamera() {
        register.launch(null)
    }

    private fun salvarImagemProduto() {
        val imageview = binding.imageProduct
        val bitmap = getBitmapFromView(imageview)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val reference = FirebaseStorage.getInstance().reference

        val path = reference.child("${uID.toString()}/$nomeproduto/imagem_produto.jpg")

        var uploadTask = path.putBytes(data)
        uploadTask.addOnProgressListener {
            binding.progressBar.max = 100

            val progress: Long = (100 * it.bytesTransferred / it.totalByteCount)
            val currentProgress = progress.toInt()

            binding.progressBar.visibility = View.VISIBLE

            binding.progressBar.progress = currentProgress
        }.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let { it1 ->
                    throw it1
                }
            }
            path.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                img_url = it.result.toString()
                registrarProduto()
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun DataAtual(): String {
        val formatData: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val data = Date()
        println("Data formatada: ${formatData.format(data)}")
        return formatData.format(data)
    }
}