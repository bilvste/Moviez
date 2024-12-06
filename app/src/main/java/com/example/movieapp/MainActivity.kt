package com.example.movieapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize UI elements
        val userEditText = findViewById<EditText>(R.id.userEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.rememberMeCheckBox)
        val loginButton = findViewById<Button>(R.id.loginButton)

        // SharedPreferences to save user data
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        val savedPassword = sharedPreferences.getString("password", "")

        // Log to check if credentials are being retrieved correctly
        Log.d("Login", "Saved username: $savedUsername, Saved password: $savedPassword")

        // Auto-login: if credentials are saved, navigate directly to MoviesMain
        if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            Log.d("Login", "Auto login successful for username: $savedUsername")
            navigateToMoviesMain(savedUsername)
            return // Skip further code execution
        }

        // Set up login button click listener
        loginButton.setOnClickListener {
            val username = userEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Log the input for debugging purposes
            Log.d("Login", "Entered username: $username, password: $password")

            // Check if username or password is empty
            when {
                username.isEmpty() -> {
                    Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Validate credentials: check if entered username and password match the saved ones

                        // Optionally save credentials if "Remember Me" is checked
                        if (rememberMeCheckBox.isChecked) {
                            sharedPreferences.edit().apply {
                                putString("username", username)
                                putString("password", password)
                                apply()
                            }
                            Log.d("Login", "Credentials saved successfully!")
                            Toast.makeText(this, "Credentials saved!", Toast.LENGTH_SHORT).show()
                        }
                        // Navigate to MoviesMain
                        navigateToMoviesMain(username)

                }
            }
        }
    }

    private fun navigateToMoviesMain(username: String) {
        val intent = Intent(this, MoviesMain::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
        finish() // Close the login activity so the user can't go back
    }
}
