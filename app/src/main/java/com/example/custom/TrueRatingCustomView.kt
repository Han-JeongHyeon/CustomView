package com.example.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.example.custom.databinding.LayoutCustomviewBinding
import java.util.*
import kotlin.math.log

@SuppressLint("ClickableViewAccessibility")
class TrueRatingCustomView(context: Context, attrs: AttributeSet): ConstraintLayout(context,attrs) {

    private val TAG = "CustomView"

    private var emptyDrawable: Drawable? = null
    private var filledDrawable: Drawable? = null
    private var number: Int = 0
    private var ratingWidth: Float = 0F
    private var ratingHeight: Float = 0F
    private var ratingPadding: Int = 0

    init {
        View.inflate(context, R.layout.layout_customview,this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrueRatingCustomView)

        number = typedArray.getInt(R.styleable.TrueRatingCustomView_rating_Number,5)
        ratingWidth = typedArray.getFloat(R.styleable.TrueRatingCustomView_rating_Width,20F)
        ratingHeight = typedArray.getFloat(R.styleable.TrueRatingCustomView_rating_Height,20F)
        ratingPadding = typedArray.getInt(R.styleable.TrueRatingCustomView_rating_Padding,0)

        emptyDrawable = ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(R.styleable.TrueRatingCustomView_rating_DrawableEmpty, R.drawable.empty))

        filledDrawable = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(R.styleable.TrueRatingCustomView_rating_DrawableFilled, R.drawable.filled))

        for (i in 1..number) {
            val imageViewId = getIdentifier(i)

            imageViewId.layoutParams.height =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ratingWidth, resources.displayMetrics)
                    .toInt()

            imageViewId.layoutParams.width =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ratingHeight, resources.displayMetrics)
                .toInt()

            imageViewId.setPadding(ratingPadding,ratingPadding,ratingPadding,ratingPadding)

            imageViewId.setImageDrawable(emptyDrawable)
            imageViewId.visibility = View.VISIBLE

            imageViewId.setOnClickListener {
                backgroundChange(i)
            }
        }
    }

    var positionNum: Int = 0

    private fun backgroundChange(position: Int) {
        for (i in 1..number) {
            val imageViewId = getIdentifier(i)

            if (position >= i) imageViewId.setImageDrawable(filledDrawable)
            else imageViewId.setImageDrawable(emptyDrawable)
        }
        if (positionNum == position) backgroundChange(0)
        else positionNum = position
    }

    private fun getIdentifier(id: Int): ImageView {
        return findViewById(
            resources.getIdentifier("img$id","id","com.example.custom")
        )
    }

}
