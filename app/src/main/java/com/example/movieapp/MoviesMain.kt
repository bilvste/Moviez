package com.example.movieapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MoviesMain : AppCompatActivity() {

    private lateinit var moviesList: MutableList<Movie>
    private lateinit var adapter: MoviesAdapter
    private var currentIndex = 0
    private val increment = 5  // Show 5 movies at a time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_main)

        // Initialize movies list and adapter
        moviesList = loadMoviesFromRaw().toMutableList() // Load all movies
        val initialMovies = moviesList.take(minOf(increment, moviesList.size))
        adapter = MoviesAdapter(initialMovies, this) // Show first 5
        val moviesRecyclerView = findViewById<RecyclerView>(R.id.moviesRecyclerView)
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        moviesRecyclerView.adapter = adapter

        // Fetching username from Intent and displaying in the NavBar
        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
            usernameTextView.text = "Welcome $username"
        }

        // Dropdown button functionality to add more movies
        val dropdownButton = findViewById<ImageView>(R.id.dropdownButton)
        dropdownButton.setOnClickListener {
            addMoreMovies()
        }
    }

    // Load movies from raw JSON file
    private fun loadMoviesFromRaw(): List<Movie> {
        val movieList = mutableListOf<Movie>()
        try {
            val inputStream = resources.openRawResource(R.raw.movies) // Access raw movie JSON file
            val reader = InputStreamReader(inputStream)
            val movieListType = object : TypeToken<List<Movie>>() {}.type
            val movies: List<Movie> = Gson().fromJson(reader, movieListType)
            movieList.addAll(movies)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading movies: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return movieList
    }

    // Load and add more movies
    private fun addMoreMovies() {
        if (currentIndex + increment < moviesList.size) {
            currentIndex += increment
            val newMovies = moviesList.subList(currentIndex, minOf(currentIndex + increment, moviesList.size))
            adapter.addMovies(newMovies)
        } else {
            Toast.makeText(this, "No more movies to load.", Toast.LENGTH_SHORT).show()
        }
    }
}
