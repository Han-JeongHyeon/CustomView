package com.example.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.log

@SuppressLint("ClickableViewAccessibility")
class CustomTrue(context: Context, attrs: AttributeSet): ConstraintLayout(context,attrs) {

    private var mNumStars = 0
    private var mPadding = 20
    private var mStarWidth = 0
    private var mStarHeight = 0

    private var mMinimumStars = 0f
    private var mRating = -1f
    private var mStepSize = 1f
    private var mPreviousRating = 0f

    private var mIsIndicator = false
    private var mIsScrollable = true
    private var mIsClickable = true
    private var mClearRatingEnabled = true

    private var mStartX = 0f
    private var mStartY = 0f

    private var mEmptyDrawable: Drawable? = null
    private var mFilledDrawable: Drawable? = null

//    private var rating: RatingBar

    private var text1: TextView
    private var text2: TextView

    init {
        val v = View.inflate(context, R.layout.layout_customview, this)

        text1 = v.findViewById(R.id.text1)
        text2 = v.findViewById(R.id.text2)
//        rating = v.findViewById(R.id.rating)



        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BaseRatingBarTrue,
            0, 0).apply {
            try {

                val typeArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRatingBarTrue)

//                setStepSize(typeArray.getFloat(R.styleable.BaseRatingBarTrue_stepSize, mStepSize))
//                setNumStars(typeArray.getInt(R.styleable.BaseRatingBarTrue_numStars, mNumStars))
            } finally {
                recycle()
            }

        }



//        rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
//            Log.d("TAG", "$ratingBar $fl $b ")
//            rating.setOnTouchListener { view, event ->
//
////                rating.setOnRatingBarChangeListener { ratingBar, fl, b ->
////                    Log.d("TAG", "$ratingBar $fl $b ")
////                }
//
//                val eventX = event.x
//                val eventY = event.y
//
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        mStartX = eventX
//                        mStartY = eventY
//                        mPreviousRating = mRating
////                        Log.d(Custom.TAG, "1")
//                    }
//                    MotionEvent.ACTION_MOVE -> {
////                        handleClickEvent(eventX)
////                        Log.d(Custom.TAG, "2")
//                    }
//                    MotionEvent.ACTION_UP -> {
////                        handleClickEvent(eventX)
////                        Log.d(Custom.TAG, "3")
//                    }
//                }
//                parent.requestDisallowInterceptTouchEvent(true)
//                true
//            }
//        }

//        initRatingView()

        val view = v.findViewById<View>(R.id.view)

        view.setOnTouchListener { view, motionEvent ->

            Log.d("TAG", "text1 ${view.left} ${view.right}")
            true
        }

        text1.setOnTouchListener { view, motionEvent ->

            Log.d("TAG", "text1 ${text1.left} ${text1.right}")
            true
        }
        text2.setOnTouchListener { view, motionEvent ->

            Log.d("TAG", "text2 ${text2.left} ${text2.right}")
            true
        }
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        Log.d("TAG", "text1 ${text1.left} ${text1.right}")
        Log.d("TAG", "text1 ${text2.left} ${text2.right}")
    }

//    private fun handleClickEvent(eventX: Float) {
////        Log.d(Custom.TAG, "4")
//        for (aa in mPartialViews!!) {
////            Log.d(Custom.TAG, "5")
//            if (!isPositionInRatingView(eventX, aa)) {
//                continue
//            }
////            val rating =
////                if (mStepSize != 1f) {
////                    (aa.tag as Int).toFloat()
////                }
////                else{
////
////                    }
////            if (mPreviousRating == rating && isClearRatingEnabled()) {
////                setRating(mMinimumStars, true)
////            } else {
////                setRating(rating, true)
////            }
//            break
//        }
//    }
//
//    private fun initRatingView() {
//        mPartialViews = ArrayList()
//
//        for (i in 1..5) {
//            val partialView = getPartialView(
//                i,
//                mStarWidth,
//                mStarHeight,
//                mPadding
//            )
//            addView(partialView)
//            mPartialViews!!.add(partialView)
//        }
//
//    }
//
//    private var mPartialViews: MutableList<PartialViewTrue>? = null
//
//    private fun isPositionInRatingView(eventX: Float, aa: View): Boolean {
//        Log.d("TAG", "$eventX ${aa.left} ${aa.right}")
//        return eventX > aa.left && eventX < aa.right
//    }

//    private fun getPartialView(
//        partialViewId: Int, starWidth: Int, starHeight: Int
//        , padding: Int
//    ): PartialViewTrue {
//        return PartialViewTrue(context, partialViewId, starWidth, starHeight, padding)
//    }
//
//    private fun setStepSize(stepSize: Float) {
//        rating.stepSize = stepSize
//        onRefresh()
//    }
//
//    private fun setNumStars(numStars: Int){
//        rating.numStars = numStars
//    }

    private fun onRefresh(){
        invalidate()
        requestLayout()
    }

}