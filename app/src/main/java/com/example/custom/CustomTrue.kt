package com.example.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import com.example.custom.databinding.LayoutCustomviewBinding
import java.util.*
import kotlin.math.log

@SuppressLint("ClickableViewAccessibility")
class CustomTrue(context: Context, attrs: AttributeSet): ConstraintLayout(context,attrs) {
    private var binding = LayoutCustomviewBinding.inflate(LayoutInflater.from(context), this, true)

    private val TAG = "CustomView"
    
    init {
        binding.img1.setOnClickListener { backgroumd(1) }
        binding.img2.setOnClickListener { backgroumd(2) }
        binding.img3.setOnClickListener { backgroumd(3) }
        binding.img4.setOnClickListener { backgroumd(4) }
        binding.img5.setOnClickListener { backgroumd(5) }
    }

    fun reSet() {
        binding.img1.setBackgroundResource(R.drawable.empty)
        binding.img2.setBackgroundResource(R.drawable.empty)
        binding.img3.setBackgroundResource(R.drawable.empty)
        binding.img4.setBackgroundResource(R.drawable.empty)
        binding.img5.setBackgroundResource(R.drawable.empty)
    }

    var posi: Int = 0

    fun backgroumd(position: Int) {
        reSet()
        if (position >= 1) binding.img1.setBackgroundResource(R.drawable.filled)
        if (position >= 2) binding.img2.setBackgroundResource(R.drawable.filled)
        if (position >= 3) binding.img3.setBackgroundResource(R.drawable.filled)
        if (position >= 4) binding.img4.setBackgroundResource(R.drawable.filled)
        if (position >= 5) binding.img5.setBackgroundResource(R.drawable.filled)

        posi = if (position == posi){
            reSet()
            0
        } else position
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> Log.d(TAG, "onTouchEvent:1 ")
            MotionEvent.ACTION_MOVE -> Log.d(TAG, "onTouchEvent:2 ")
        }
//        binding.img1.
        return super.onTouchEvent(event)
    }

}