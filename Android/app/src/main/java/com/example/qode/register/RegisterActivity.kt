package com.example.qode.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var pwClicked : Boolean = false
    private var pwCheckClicked : Boolean = false


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serverString = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)

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
                binding.nicknameET.text.isEmpty() -> {
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
                    var idDuplicated = false // false면 중복이 없음, true면 중복이 있음
                    GlobalScope.launch {
                        idDuplicated = duplicationCheck(serverString)
                        if(!idDuplicated){ // 중복이 없으면 정상적으로 아이디 생성
                            //원래 화면으로 돌아가는 부분
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)

                            //applicationContext.finish() // 스크린을 종료한다
                        }else{ // 중복이 있으면 생성 불가 토스트 메시지 띄워주기
                            runOnUiThread{
                                Toast.makeText(applicationContext, "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    println("!@#!@$!@$")
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
        this.finish()
        //removeView()
    }

    private suspend fun duplicationCheck(serverString : String?) = suspendCoroutine<Boolean> {
        Handler(Looper.getMainLooper()).postDelayed({
            var idDuplicated = false // false면 중복이 없음, true면 중복이 있음
            val request = object : StringRequest(Method.POST, "${serverString}auth/join/", // url은 매번 바꿔줘야 함.
                Response.Listener {response ->
                    /**
                    json_register_test_member.php 에 회원가입관련 정보를 POST방식으로 보내면 그 응답결과를
                    {"success":"1"} 이나 {"success":"error"} 등의 json 결과물을 받을수 있다.
                    현재 php가 아닌 node와 mysql을 사용한다는 점을 감안하고 할 것.
                     */
                    val jsonObject: JSONObject = JSONObject(response)
                    //var success:String = jsonObject.getString("success")//성공적으로 처리 되었는지 확인하는 string
                    val state:String = jsonObject.getString("state")//성공적으로 처리 되었는지 확인하는 string
                    if(state == "idDuplicated"){
                        idDuplicated = true
                    }
                    println("### idDuplicated : $idDuplicated")
                    println("### state : $state")
                    Log.e("state", state)
                    println("@@@성공")
                    it.resume(idDuplicated)
                },
                Response.ErrorListener { error ->
                    println("@@@오류, $error")
                }){
                override fun getParams(): Map<String, String> {
                    val params : MutableMap<String, String> = HashMap()
                    params["userID"] = binding.idET.text.toString()
                    params["userPassword"] = binding.pwET.text.toString()
                    params["userName"] = binding.nicknameET.text.toString() // userName이 아니라 usernickName
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }, 500)
    }
}