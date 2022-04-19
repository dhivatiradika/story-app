package com.dhiva.storyapp.ui.signup

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dhiva.storyapp.R
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.databinding.ActivitySignupBinding
import com.dhiva.storyapp.utils.ViewModelFactory
import com.dhiva.storyapp.utils.matchEmailFormat
import com.dhiva.storyapp.utils.toast

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels{
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            tvLogin.setOnClickListener {
                finish()
            }

            btLogin.setOnClickListener {
                signUp()
            }
        }
    }

    private fun signUp() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (name.isEmpty()) {
            val message = resources.getString(R.string.wrong_name_format)
            this.toast(message)
            return
        }

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

        signupViewModel.signup(name, email, password).observe(this){result ->
            when (result) {
                is Resource.Loading -> isLoadingShown(true)
                is Resource.Success -> {
                    this.toast(resources.getString(R.string.signup_success))
                    finish()
                }
                is Resource.Error -> {
                    isLoadingShown(false)
                    this.toast(result.message ?: resources.getString(R.string.something_wrong))
                }
            }

        }
    }

    private fun isLoadingShown(isShown: Boolean) {
        with(binding) {
            loadingScreen.visibility = if (isShown) View.VISIBLE else View.GONE
            signupScreen.visibility = if (isShown) View.GONE else View.VISIBLE
        }
    }
}