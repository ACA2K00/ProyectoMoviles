package com.emilianosloth.proyectofinal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class RecipeAdapter(private var names: ArrayList<String>, private var authors: ArrayList<String>,
                    private var images: ArrayList<String>,
                    private var listener : View.OnClickListener) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val db = Firebase.firestore
        var name : TextView
        var author : TextView
        var image : ImageView
        var like : RadioButton

        init {
            name = itemView.findViewById(R.id.recnameCV)
            author = itemView.findViewById(R.id.recauthCV)
            image = itemView.findViewById(R.id.imgCV)
            like = itemView.findViewById(R.id.likeButton)

            like.setOnClickListener{
//                Toast.makeText(itemView.context, "${name.text}, ${author.text}", Toast.LENGTH_SHORT).show()
                var likes : ArrayList<String>
                db.collection("usuarios")
                    .whereEqualTo("id", Firebase.auth.currentUser?.email.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents){
                            likes = document.get("likes") as ArrayList<String>
                            likes.add("${name.text},${author.text}")
                            db.collection("usuarios").document(document.id).update("likes", likes)
                            Toast.makeText(itemView.context, "Like", Toast.LENGTH_SHORT).show()
                        }


                    }
                    .addOnFailureListener{
                        Toast.makeText(itemView.context, "${it.toString()}", Toast.LENGTH_SHORT).show()
                    }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reciperow, parent, false)
        view.setOnClickListener(listener)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = names[position]
        holder.author.text = authors[position]
        loadImg(holder.image, images[position])
    }

    override fun getItemCount(): Int {
        return names.size
    }

    fun loadImg(view: ImageView, url: String){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null


        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // Only for making changes in UI
                handler.post {
                    view.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    }