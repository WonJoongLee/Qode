package com.example.qode.register

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var pwClicked : Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.passwordEyeImageButton.setOnClickListener {
            Toast.makeText(this, "CLIECKED", Toast.LENGTH_SHORT).show()
            if(!pwClicked){
                // 지금 눈이 떠 져 있는 상황인 경우
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

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish() // 스크린을 종료한다
        }

    }
}