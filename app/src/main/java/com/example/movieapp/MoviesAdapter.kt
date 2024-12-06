package com.example.movieapp

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class MoviesAdapter(private var moviesList: List<Movie>, private val context: Context) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = moviesList[position]
        holder.movieName.text = movie.name
        holder.movieRating.text = "⭐ Rating: ${movie.rating}"
        holder.movieDescription.text = movie.description

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .placeholder(R.drawable.loading) // Optional placeholder
            .error(R.drawable.error) // Optional error image
            .into(holder.movieImage)

        // Fade-in animation
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(500).start()

        // Slide-in animation from the bottom
        val slideIn = ObjectAnimator.ofFloat(holder.itemView, "translationY", 500f, 0f)
        slideIn.duration = 500
        slideIn.start()

        // Scale-up animation
        val scaleUpX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.8f, 1f)
        val scaleUpY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.8f, 1f)
        scaleUpX.duration = 500
        scaleUpY.duration = 500
        scaleUpX.start()
        scaleUpY.start()
    }

    // Method to add more movies to the list
    fun addMovies(newMovies: List<Movie>) {
        val updatedMovies = moviesList.toMutableList()
        updatedMovies.addAll(newMovies)
        moviesList = updatedMovies
        notifyDataSetChanged() // Notify adapter about the changes
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.movieName)
        val movieRating: TextView = itemView.findViewById(R.id.movieRating)
        val movieDescription: TextView = itemView.findViewById(R.id.movieDescription)
        val movieImage: ShapeableImageView = itemView.findViewById(R.id.movieImage)
    }
}
