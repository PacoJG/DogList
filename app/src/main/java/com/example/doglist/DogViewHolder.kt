package com.example.doglist

import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DogViewHolder(view:View):RecyclerView.ViewHolder(view) {
    val iv:ImageView = view.findViewById(R.id.ivDog)
    fun bind(image:String){
        Picasso.get().load(image).into(iv)
    }
}