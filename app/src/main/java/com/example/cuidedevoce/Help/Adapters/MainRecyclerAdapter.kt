package com.example.cuidedevoce.Help.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cuidedevoce.R
import com.example.cuidedevoce.dataClasses.productInfos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vicmikhailau.maskededittext.MaskedEditText

class MainRecyclerAdapter(private val dados: ArrayList<productInfos>) : RecyclerView.Adapter<MainRecyclerAdapter.viewHolder>() {

    inner class viewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val purchase_date: TextView
        val due_date: TextView
        val due_date_bar: ProgressBar
        val product_img: ImageView
        val name_product: TextView

        init {
            purchase_date = itemview.findViewById(R.id.purchase_date_rv)
            due_date = itemview.findViewById(R.id.due_date_rv)
            due_date_bar = itemview.findViewById(R.id.due_date_bar_rv)
            product_img = itemview.findViewById(R.id.product_img_rv)
            name_product = itemview.findViewById(R.id.name_product)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_main, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val infos: productInfos = dados[position]

        if (infos.imgUrl.isNotEmpty()) {
            Picasso.get().load(infos.imgUrl).into(holder.product_img)
        }
        holder.due_date.text = infos.dataValidade
        holder.name_product.text = infos.nameProduct
        holder.purchase_date.text = infos.data_compra
    }

    override fun getItemCount(): Int {
        return dados.size
    }
}