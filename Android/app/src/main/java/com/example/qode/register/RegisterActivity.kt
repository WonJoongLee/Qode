package com.example.qode.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var pwClicked : Boolean = false
    private var pwCheckClicked : Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        //비밀번호 EditText 암호 보이게 하는 것 관리하는 코드
        binding.passwordEyeImageButton.setOnClickListener {
            //Toast.makeText(this, "CLIECKED", Toast.LENGTH_SHORT).show()
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

        //비밀번호 확인 EditText 암호 보이게 하는 것 관리하는 코드
        binding.passwordCheckEyeImageButton.setOnClickListener {
            //Toast.makeText(this, "CLIECKED", Toast.LENGTH_SHORT).show()
            if(!pwCheckClicked){
                // 지금 눈이 떠 져 있는 상황인 경우
                binding.passwordCheckEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_off_24))
                binding.pwCheckET.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.pwCheckET.setSelection(binding.pwCheckET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwCheckClicked = true
            }else{
                binding.passwordCheckEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_24))
                binding.pwCheckET.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.pwCheckET.setSelection(binding.pwCheckET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwCheckClicked = false
            }
        }

        //회원가입을 클릭했을 때
        binding.registerButton.setOnClickListener {
            when {
                binding.nameET.text.isEmpty() -> {
                    Toast.makeText(this, "이름을을 입력해 주세요.",Toast.LENGTH_SHORT).show()
                }
                binding.idET.text.isEmpty() -> {
                    Toast.makeText(this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
                binding.pwET.text.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
                binding.pwCheckET.text.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 한 번 더 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
                binding.pwET.text.toString() != binding.pwCheckET.text.toString() -> {
                    Log.e("text", "pwET : ${binding.pwET.text}, pwCheckEt : ${binding.pwCheckET.text}")
                    Toast.makeText(this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val request = object : StringRequest(Method.POST, "http://54cf09e2d946.ngrok.io/register/", // url은 매번 바꿔줘야 함.
                        Response.Listener {
                            println("@@@성공")
                        },
                        Response.ErrorListener { error ->
                            println("@@@오류, $error")
                        }){
                        override fun getParams(): Map<String, String> {
                            val params : MutableMap<String, String> = HashMap()
                            params["userID"] = binding.idET.text.toString()
                            params["userPassword"] = binding.pwET.text.toString()
                            params["userName"] = binding.nameET.text.toString()
                            return params
                        }
                    }
                    val queue = Volley.newRequestQueue(this)
                    queue.add(request)

                    //원래 화면으로 돌아가는 부분
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
                    this.finish() // 스크린을 종료한다
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_x_hundred_to_zero,
            R.anim.slide_x_zero_to_minus_hundred
        )
    }
}