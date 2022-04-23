package com.example.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var adapter:DogAdapter
    private val dogImages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<SearchView>(R.id.svDogs).setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter = DogAdapter(dogImages)
        findViewById<RecyclerView>(R.id.rvDogs).layoutManager=LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.rvDogs).adapter=adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create()) //serializador
            .build()
    }

    private fun searchByName(query:String){
        Toast.makeText(this,"buscando..",Toast.LENGTH_LONG).show()
        //hilo secundaria
        CoroutineScope(Dispatchers.IO).launch {
            val call:Response<DogsResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies:DogsResponse? = call.body()
            runOnUiThread {//regreso al hilo principal
                if (call.isSuccessful) {
                    val images:List<String> = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()

                } else {
                    showError()
                }
            }
        }
    }

    private fun showError(){
        Toast.makeText(this, "Ha ocurrido un error",Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        if (!p0.isNullOrEmpty()){
            searchByName(p0.toLowerCase())
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
}