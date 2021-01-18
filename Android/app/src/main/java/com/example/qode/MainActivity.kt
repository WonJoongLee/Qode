package com.example.qode

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.fragment.SamplePagerAdapter
import com.example.qode.recyclerview.QuestionTitleAdapter
import com.example.qode.recyclerview.ShowQuestionData
import com.example.qode.register.LoginActivity
import com.example.qode.register.RegisterActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isFabOpen = false



    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var bottomSheetDialog : BottomSheetDialog

        val mDatas = mutableListOf<ShowQuestionData>()
        val adapter = QuestionTitleAdapter(applicationContext)
        val recyclerView = findViewById<RecyclerView>(R.id.cRecyclerView)

        val buttonClicked = mutableMapOf<String, Boolean>() // scroll view의 버튼이 클릭 됐는지 확인하기 위한 mutableMap
                                                            // 초기 설정값은 모두 false고, 클릭 된 상태를 true로 본다
        buttonClicked["C"] = false
        buttonClicked["JAVA"] = false
        buttonClicked["PYTHON"] = false
        buttonClicked["ETC"] = false
        buttonClicked["TEMP1"] = false
        buttonClicked["TEMP2"] = false

        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)

        mDatas.add(ShowQuestionData("printf와 Cout의 차이?", "printf와 Cout의 차이가 궁금합니다."))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))
        mDatas.add(ShowQuestionData("C언어가 어렵나요?", "코딩 입문자인데 C언어가 어려운지 궁금해요!"))

        adapter.data = mDatas // 데이터를 삽입
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

        binding.refreshButton.setOnClickListener {
            binding.refreshButton.animate().rotation(binding.refreshButton.rotation+720).setDuration(500).start() // 버튼 클릭 시, 0.5초 동안 두 번 회전해서, 새로고침 했다는 것을 알려준다.
        }

        binding.cLangButton.setOnClickListener {
            if(buttonClicked["C"] == false){ // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.cLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.cLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["C"] = true
            }else{
                binding.cLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.cLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["C"] = false
            }
        }

        binding.javaLangButton.setOnClickListener {
            if(buttonClicked["JAVA"] == false){ // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.javaLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.javaLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["JAVA"] = true
            }else{
                binding.javaLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.javaLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["JAVA"] = false
            }
        }

        binding.pythonLangButton.setOnClickListener {
            if(buttonClicked["PYTHON"] == false){ // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.pythonLangButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.pythonLangButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["PYTHON"] = true
            }else{
                binding.pythonLangButton.setBackgroundResource(R.drawable.button_language_background)
                binding.pythonLangButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["PYTHON"] = false
            }
        }

        binding.etcButton.setOnClickListener {
            if(buttonClicked["ETC"] == false){ // 처음에는 모두 false 처리 되어 있는 상태이므로, 조건문도 false부터 검사한다.
                binding.etcButton.setBackgroundResource(R.drawable.button_language_background_on)
                binding.etcButton.setTextColor(Color.parseColor("#FFFFFF"))
                buttonClicked["ETC"] = true
            }else{
                binding.etcButton.setBackgroundResource(R.drawable.button_language_background)
                binding.etcButton.setTextColor(Color.parseColor("#000000"))
                buttonClicked["ETC"] = false
            }
        }


        val mSwipeRefreshLayout : SwipeRefreshLayout = findViewById(R.id.cSwipeLayout)
        mSwipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({
                    mSwipeRefreshLayout.isRefreshing = false
                }, 1500) // 1.5초 후 새로고침 멈추는 코드

            }
        })


        binding.fab.setOnClickListener{
            showReveal()
        }
        binding.hamburgerImageButton.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
            var sheetView : View = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_sheet_layout, findViewById(R.id.bottom_sheet))
            sheetView.findViewById<LinearLayout>(R.id.registerBottomSheetButton).setOnClickListener{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_x_minus_hundred_to_zero, R.anim.slide_x_zero_to_hundred)
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.loginBottomSheetButton).setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                //overridePendingTransition(R.anim.slide_x_hundred_to_zero, R.anim.slide_x_zero_to_minus_hundred)
                overridePendingTransition(R.anim.slide_x_minus_hundred_to_zero, R.anim.slide_x_zero_to_hundred)
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.searchBottomSheetButton).setOnClickListener {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(sheetView)
            bottomSheetDialog.show()
        }

    }

    private fun showReveal(){
        var centerX = binding.fab.x + binding.fab.width / 2 // 애니메이션 중심 x좌표 지정
        var centerY = binding.fab.y + binding.fab.height / 2 // 애니메이션 중심 y좌표 지정
        //target view 지정
        var radius = hypot(binding.layoutContent.width.toDouble(), binding.layoutContent.height.toDouble())

        //Log.e("e", binding.bottomAppBar.hideOnScroll.toString())


        //binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))

        if(isFabOpen) {
            Log.e("FAB", "Fab Status : $isFabOpen")

            binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#009688"))
            binding.fab.setImageResource(R.drawable.pencil)
            binding.bottomAppBar.performShow()
            var revealAnimator: Animator = ViewAnimationUtils.createCircularReveal(binding.layoutContent,
                centerX.toInt(), centerY.toInt(), radius.toFloat(), 0F)//start radius : 애니메이션 시작 지점 원의 반경 지정, end radius : 애니메이션 종료 지점 원의 반경 지정
            revealAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.layoutContent.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(p0: Animator?) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator?) {
                    TODO("Not yet implemented")
                }

            })//TODO
            revealAnimator.duration = 300
            revealAnimator.start()
        }else{
            Log.e("FAB", "Fab Status : $isFabOpen")

            binding.bottomAppBar.performHide()
            binding.fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
            binding.fab.setImageResource(R.drawable.ic_baseline_arrow_back_24)
            var revealAnimator : Animator = ViewAnimationUtils.createCircularReveal(binding.layoutContent, centerX.toInt(), centerY.toInt(), 0F, radius.toFloat())
            revealAnimator.duration = 300
            binding.layoutContent.visibility = View.VISIBLE
            revealAnimator.start()
        }

        isFabOpen = !isFabOpen
    }

    override fun onBackPressed() {
        if(!isFabOpen){
            super.onBackPressed()
        }
    }

}

