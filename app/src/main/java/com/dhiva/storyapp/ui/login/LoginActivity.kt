package com.dhiva.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.dhiva.storyapp.R
import com.dhiva.storyapp.customview.EmailEditText
import com.dhiva.storyapp.customview.PasswordEditText
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.model.User
import com.dhiva.storyapp.ui.main.MainActivity
import com.dhiva.storyapp.ui.signup.SignupActivity
import com.dhiva.storyapp.utils.ViewModelFactory
import com.dhiva.storyapp.utils.matchEmailFormat
import com.dhiva.storyapp.utils.toast

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EmailEditText
    private lateinit var etPassword: PasswordEditText
    private lateinit var btLogin: Button
    private lateinit var tvToRegister: TextView
    private lateinit var loginScreen: MotionLayout
    private lateinit var loadingScreen: ConstraintLayout
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        initViewModel()
    }

    private fun init() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btLogin = findViewById(R.id.bt_login)
        tvToRegister = findViewById(R.id.tv_login)
        loginScreen = findViewById(R.id.login_screen)
        loadingScreen = findViewById(R.id.loading_screen)

        tvToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        btLogin.setOnClickListener {
            login()
        }
    }

    private fun initViewModel() {
        loginViewModel.getAuthSession().observe(this) {
            if (it.token != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                isLoadingShown(false)
            }
        }
        loginViewModel.result.observe(this) { result ->
            when (result) {
                is Resource.Loading -> isLoadingShown(true)
                is Resource.Success -> {
                    val user = User(
                        name = result.data?.name,
                        userId = result.data?.userId,
                        token = result.data?.token
                    )
                    loginViewModel.setAuthSession(user)
                }
                is Resource.Error -> {
                    isLoadingShown(false)
                    this.toast(result.message ?: resources.getString(R.string.something_wrong))
                }
            }
        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (!email.matchEmailFormat()) {
            val message = resources.getString(R.string.wrong_email_format)
            this.toast(message)
            return
        }

        if (password.length < 6) {
            val message = resources.getString(R.string.wrong_password_format)
            this.toast(message)
            return
        }

        loginViewModel.login(email, password)
    }

    private fun isLoadingShown(isShown: Boolean) {
        loadingScreen.visibility = if (isShown) View.VISIBLE else View.GONE
        loginScreen.visibility = if (isShown) View.GONE else View.VISIBLE
    }
}