package com.example.a26nov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var movieRecycleView: RecyclerView

    var movieDataArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieRecycleView = findViewById(R.id.lstMovie)
        movieRecycleView.adapter = MovieAdapter()
        movieRecycleView.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

       downloaddata()

    }

    fun downloaddata()
    {
        //RequestQueue que - volly.newRequestQueue(this)

        val que = Volley.newRequestQueue(this)
        val url = "https://api.themoviedb.org/3/movie/upcoming?api_key=945343a9152cdca2cc82852f09ac9c22"
        val request = JsonObjectRequest(url, JSONObject(),
            { response ->
                Log.e("Response", response.toString())

                try {
                    movieDataArray = response.getJSONArray("results")
                    movieRecycleView.adapter?.notifyDataSetChanged()

                }catch (error: Exception)
                {

                }

            },
            {error ->
                Log.e("Error", error.toString())

            })

        que.add(request)



    }

    private inner class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_row,parent,false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            //Log.e("Inflate","Binding")
//            holder.movieTitle.text = "Test Title"
//            holder.movieReleaseDate.text = "Test Release Date"
            try
            {
                holder.movieTitle.text = movieDataArray.getJSONObject(position).getString("original_title")
                holder.movieReleaseDate.text = movieDataArray.getJSONObject(position).getString("release_date")
                Glide.with(applicationContext).load("https://image.tmdb.org/t/p/w500" + movieDataArray.getJSONObject(position).getString("poster_path")).into(holder.movieThumb)
            }
            catch (e : Exception)
            {

            }
        }

        override fun getItemCount(): Int { //How many items to be returned
            return movieDataArray.length()
        }

    }
    private inner class MovieViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    {
        var movieTitle : TextView = itemView.findViewById(R.id.txtname)
        var movieReleaseDate : TextView = itemView.findViewById(R.id.txtdate)
        var movieThumb  : ImageView = itemView.findViewById(R.id.imageView2)
    }
}


