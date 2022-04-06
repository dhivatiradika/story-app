package com.dhiva.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.dhiva.storyapp.R
import com.dhiva.storyapp.customview.EmailEditText
import com.dhiva.storyapp.customview.PasswordEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EmailEditText
    private lateinit var etPassword: PasswordEditText
    private lateinit var btLogin: Button
    private lateinit var tvToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init(){
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btLogin = findViewById(R.id.bt_login)
        tvToRegister = findViewById(R.id.tv_login)

        tvToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
    }
}