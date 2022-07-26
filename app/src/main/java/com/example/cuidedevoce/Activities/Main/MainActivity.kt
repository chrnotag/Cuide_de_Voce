package com.example.cuidedevoce.Activities.Main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cuidedevoce.Activities.LoginActivity
import com.example.cuidedevoce.Activities.registerProduct
import com.example.cuidedevoce.Help.Adapters.MainRecyclerAdapter
import com.example.cuidedevoce.R
import com.example.cuidedevoce.dataClasses.productInfos
import com.example.cuidedevoce.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class MainActivity : AppCompatActivity() {

    private var clicked = false

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val rotateOpen by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)
    }

    private val rotateClose by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)
    }

    private val fromBottom by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation)
    }

    private val toBottom by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
    }

    private lateinit var recyclerview: RecyclerView
    private lateinit var infosList: ArrayList<productInfos>
    private lateinit var adapter: MainRecyclerAdapter
    private val uID = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private val firestore =  FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar.toolbar)

        recyclerview = binding.recyclerMainView

        recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        infosList = arrayListOf()

        adapter = MainRecyclerAdapter(infosList)

        recyclerview.adapter = adapter

        firestore.collection(uID)
            .document("produtossalvos")
            .collection("produtossalvos")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            infosList.add(dc.document.toObject(productInfos::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    if (adapter.itemCount == 0) {
                        binding.viewEmptyRecicler.viewEmptyRecicler.visibility = View.VISIBLE
                    } else {
                        binding.viewEmptyRecicler.viewEmptyRecicler.visibility = View.INVISIBLE
                    }
                }

            })

        binding.fab1.setOnClickListener {
            onAddButtonClicked()
        }
        binding.fab2.setOnClickListener {
            startActivity(Intent(this, registerProduct::class.java))
            finishAffinity()
        }

        supportActionBar!!.title = "Cuide de você"

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home_page, menu)
        return true
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)

        clicked = !clicked

        binding.fab2.isClickable = clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fab2.visibility = View.VISIBLE
            binding.cadastrarProdutoTextview.visibility = View.VISIBLE
        } else {
            binding.fab2.visibility = View.INVISIBLE
            binding.cadastrarProdutoTextview.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fab2.startAnimation(fromBottom)
            binding.fab1.startAnimation(rotateOpen)
            binding.cadastrarProdutoTextview.startAnimation(fromBottom)
        } else {
            binding.fab2.startAnimation(toBottom)
            binding.cadastrarProdutoTextview.startAnimation(toBottom)
            binding.fab1.startAnimation(rotateClose)
        }
    }
}