package com.example.qode.register

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.qode.MainActivity
import com.example.qode.R
import com.example.qode.databinding.ActivityLoginBinding
import com.example.qode.preferences.SharedData.Companion.prefs
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var pwClicked : Boolean = false


    @SuppressLint("UseCompatLoadingForDrawables", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serverString = getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //println("@@@ server url : ${getSharedPreferences("sharedPrefs", MODE_PRIVATE).getString("serverUrl", null)}")

        binding.passwordEyeImageButton.setOnClickListener {
            if(!pwClicked){
                binding.passwordEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_off_24_gray))
                binding.pwET.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.pwET.setSelection(binding.pwET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwClicked = true
            }else{
                binding.passwordEyeImageButton.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_baseline_visibility_24_gray))
                binding.pwET.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.pwET.setSelection(binding.pwET.text.length) // 커서가 Edittext의 앞으로 다시 돌아가는 것을 방지한다.
                pwClicked = false
            }
        }

        binding.loginButton.setOnClickListener {
//            val request = object : StringRequest(
//                Method.POST, "${serverString}login/",
//                Response.Listener { response ->
//                    var jsonObject:JSONObject = JSONObject(response)
//                    var state:String = jsonObject.getString("state")//성공적으로 처리 되었는지 확인하는 string
//                    Log.d("Connection", state)
//                },
//                Response.ErrorListener { error ->
//                    Log.e("Connection", "Error : $error")
//                }){
//                override fun getParams(): Map<String, String> {
//                    val params : MutableMap<String, String> = HashMap()
//                    params["userID"] = binding.idET.text.toString()
//                    params["userPassword"] = binding.pwET.text.toString()
//                    return params
//                }
//            }
//            val queue = Volley.newRequestQueue(this)
//            queue.add(request)

            GlobalScope.launch{
                when(loginCheck(serverString!!)){
                    "passwordUncorrect" -> {
                        runOnUiThread{
                            Toast.makeText(applicationContext, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    "loginSuccess" -> {
                        val sharedPreferences : SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor : SharedPreferences.Editor = sharedPreferences.edit()
                        editor.apply{
                            putBoolean("isLogin", true)
                        }.apply()
                        println("### 로그인 정보: ${sharedPreferences.getBoolean("isLogin", false)}") // sharedPreference로 로그인 했음을 확인한다.
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
                    }
                    "wrongAccount" -> {
                        runOnUiThread{
                            Toast.makeText(applicationContext, "해당 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
    }

    private suspend fun loginCheck(serverString : String) = suspendCoroutine<String> {
        Handler(Looper.getMainLooper()).postDelayed({
            val request = object : StringRequest(
                Method.POST, "${serverString}auth/login/",
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    val state:String = jsonObject.getString("state")//성공적으로 처리 되었는지 확인하는 string
                    val nickName : String = jsonObject.getString("nick")
                    it.resume(state) // 서버로부터 받은 state를 그대로 넘겨준다.
                    Log.d("Connection", state)
                    Log.d("nick", nickName)
                },
                Response.ErrorListener { error ->
                    Log.e("Connection", "Error : $error")
                }){
                override fun getParams(): Map<String, String> {
                    val params : MutableMap<String, String> = HashMap()
                    params["userID"] = binding.idET.text.toString()
                    params["userPassword"] = binding.pwET.text.toString()
                    return params
                }
            }
            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }, 500)
    }

}

