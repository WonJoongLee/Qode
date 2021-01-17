package com.example.qode

import android.animation.Animator
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.qode.databinding.ActivityMainBackupBinding
import com.example.qode.databinding.ActivityMainBinding
import com.example.qode.fragment.SamplePagerAdapter
import com.example.qode.register.LoginActivity
import com.example.qode.register.RegisterActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlin.math.hypot

class MainActivity_backup : AppCompatActivity() {

    private lateinit var binding: ActivityMainBackupBinding
    private var isFabOpen = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_backup)
        val adapter2 = SamplePagerAdapter(supportFragmentManager)
        var bottomSheetDialog : BottomSheetDialog

        binding.pager.adapter = adapter2
        binding.tabLayout.setupWithViewPager(binding.pager)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) { // Tab이 클릭 됐을 때
                //Toast.makeText(this@MainActivity, "Selected ${tab?.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { // 클릭 해제 됐을 때
                //Toast.makeText(this@MainActivity, "UnSelected ${tab?.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { // 같은 탭을 다시 클릭 했을 때
                //Toast.makeText(this@MainActivity, "ReSelected ${tab?.text}", Toast.LENGTH_SHORT).show()
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
                bottomSheetDialog.dismiss()
            }
            sheetView.findViewById<LinearLayout>(R.id.loginBottomSheetButton).setOnClickListener{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
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

