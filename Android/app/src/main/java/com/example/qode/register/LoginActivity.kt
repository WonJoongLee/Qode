package com.example.qode.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var pwClicked : Boolean = false


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.passwordEyeImageButton.setOnClickListener {
            if(!pwClicked){
                binding.passwordEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_off_24))
                binding.pwET.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.pwET.setSelection(binding.pwET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwClicked = true
            }else{
                binding.passwordEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_24))
                binding.pwET.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.pwET.setSelection(binding.pwET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwClicked = false
            }
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
            this.finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
    }


}