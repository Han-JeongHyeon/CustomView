package com.example.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.custom.databinding.LayoutCustomviewBinding

@SuppressLint("ClickableViewAccessibility")
class TrueRatingCustomView(context: Context, attrs: AttributeSet) : ConstraintLayout(context,attrs) {
    private var binding = LayoutCustomviewBinding.inflate(LayoutInflater.from(context), this, true)


    private val customViewTAG = "CustomViewTAG"

    private var emptyDrawable: Drawable? = null
    private var filledDrawable: Drawable? = null
    private var number: Int = 0
    private var ratingWidth: Float = 0F
    private var ratingHeight: Float = 0F
    private var ratingPadding: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrueRatingCustomView)

        number = typedArray.getInt(R.styleable.TrueRatingCustomView_rating_Number,5)
        ratingWidth = typedArray.getDimension(R.styleable.TrueRatingCustomView_rating_Width,20F)
        ratingHeight = typedArray.getDimension(R.styleable.TrueRatingCustomView_rating_Height,20F)
        ratingPadding = typedArray.getDimension(R.styleable.TrueRatingCustomView_rating_Padding,0F).toInt()

        emptyDrawable = ContextCompat.getDrawable(
                context,
                typedArray.getResourceId(R.styleable.TrueRatingCustomView_rating_DrawableEmpty, R.drawable.empty))

        filledDrawable = ContextCompat.getDrawable(
            context,
            typedArray.getResourceId(R.styleable.TrueRatingCustomView_rating_DrawableFilled,
                R.drawable.filled
            ))

        for (i in 1..number) {
            val imageViewId = getIdentifier(i)

            imageViewId.layoutParams.height = ratingWidth.toInt()

            imageViewId.layoutParams.width = ratingHeight.toInt()

            imageViewId.setPadding(ratingPadding)

            imageViewId.setImageDrawable(emptyDrawable)
            imageViewId.visibility = View.VISIBLE

            imageViewId.setOnClickListener {
                backgroundChange(i)
            }
        }

        typedArray.recycle()
    }

    private var positionNum: Int = 0

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

    fun getValue(): Int{
        return 10
    }

}
