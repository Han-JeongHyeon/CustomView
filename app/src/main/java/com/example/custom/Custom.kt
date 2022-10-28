package com.example.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat

open class Custom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr), SimpleRatingBarTrue {
    
    interface OnRatingChangeListener {
        fun onRatingChange(ratingBar: Custom?, rating: Float, fromUser: Boolean)
    }

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
    
    private var mOnRatingChangeListener: OnRatingChangeListener? = null

    private var mPartialViews: MutableList<PartialViewTrue>? = null

    private fun initParamsValue(typedArray: TypedArray, context: Context) {
        mNumStars = typedArray.getInt(R.styleable.BaseRatingBar_srb_numStars, mNumStars)
        mStepSize = typedArray.getFloat(R.styleable.BaseRatingBar_srb_stepSize, mStepSize)
        mMinimumStars = typedArray.getFloat(R.styleable.BaseRatingBar_srb_minimumStars, mMinimumStars)
        mPadding = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starPadding, mPadding)
        mStarWidth = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starWidth, 0)
        mStarHeight = typedArray.getDimensionPixelSize(R.styleable.BaseRatingBar_srb_starHeight, 0)
        mEmptyDrawable =
            if (typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableEmpty)) 
                ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableEmpty, NO_ID)
            ) else null
        mFilledDrawable =
            if (typedArray.hasValue(R.styleable.BaseRatingBar_srb_drawableFilled)) 
                ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.BaseRatingBar_srb_drawableFilled, NO_ID)
            ) else null
        mIsIndicator = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_isIndicator, mIsIndicator)
        mIsScrollable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_scrollable, mIsScrollable)
        mIsClickable = typedArray.getBoolean(R.styleable.BaseRatingBar_srb_clickable, mIsClickable)
        mClearRatingEnabled = typedArray.getBoolean(
            R.styleable.BaseRatingBar_srb_clearRatingEnabled,
            mClearRatingEnabled
        )
        typedArray.recycle()
    }

    private fun verifyParamsValue() {
        if (mNumStars <= 0) {
            mNumStars = 5
        }
        if (mPadding < 0) {
            mPadding = 0
        }
        if (mEmptyDrawable == null) {
            mEmptyDrawable = ContextCompat.getDrawable(context, R.drawable.empty)
        }
        if (mFilledDrawable == null) {
            mFilledDrawable = ContextCompat.getDrawable(context, R.drawable.filled)
        }
        if (mStepSize > 1.0f) {
            mStepSize = 1.0f
        } else if (mStepSize < 0.1f) {
            mStepSize = 0.1f
        }

        mMinimumStars = RatingBarUtilsTrue.getValidMinimumStars(mMinimumStars, mNumStars, mStepSize)
    }

    private fun initRatingView() {
        mPartialViews = ArrayList()
        for (i in 1..mNumStars) {
            val partialView = getPartialView(
                i,
                mStarWidth,
                mStarHeight,
                mPadding,
                mFilledDrawable,
                mEmptyDrawable
            )
            addView(partialView)
            mPartialViews!!.add(partialView)
        }
    }

    private fun getPartialView(
        partialViewId: Int, starWidth: Int, starHeight: Int, padding: Int,
        filledDrawable: Drawable?, emptyDrawable: Drawable?
    ): PartialViewTrue {
        val partialView = PartialViewTrue(context, partialViewId, starWidth, starHeight, padding)
        partialView.setFilledDrawable(filledDrawable!!)
        partialView.setEmptyDrawable(emptyDrawable!!)
        return partialView
    }



    /**
     * Retain this method to let other RatingBar can custom their decrease animation.
     */
    protected fun emptyRatingBar() {
        fillRatingBar(0f)
    }

    /**
     * Use {maxIntOfRating} because if the rating is 3.5
     * the view which id is 3 also need to be filled.
     */
    protected fun fillRatingBar(rating: Float) {
        for (partialView in mPartialViews!!) {
            val ratingViewId = partialView.tag as Int
            val maxIntOfRating = Math.ceil(rating.toDouble())
            if (ratingViewId > maxIntOfRating) {
                partialView.setEmpty()
                continue
            }
            if (ratingViewId.toDouble() == maxIntOfRating) {
                partialView.setPartialFilled(rating)
            } else {
                partialView.setFilled()
            }
        }
    }

    override fun setNumStars(numStars: Int) {
        if (numStars <= 0) {
            return
        }
        mPartialViews!!.clear()
        removeAllViews()
        mNumStars = numStars
        initRatingView()
    }

    override fun getNumStars(): Int {
        return mNumStars
    }

    override fun setRating(rating: Float) {
        setRating(rating, false)
    }

    private fun setRating(rating: Float, fromUser: Boolean) {
        var rating = rating
        if (rating > mNumStars) {
            rating = mNumStars.toFloat()
        }
        if (rating < mMinimumStars) {
            rating = mMinimumStars
        }
        if (mRating == rating) {
            return
        }

        // Respect Step size. So if the defined step size is 0.5, and we're attributing it a 4.7 rating,
        // it should actually be set to `4.5` rating.
        val stepAbidingRating =
            java.lang.Double.valueOf(Math.floor((rating / mStepSize).toDouble()))
                .toFloat() * mStepSize
        mRating = stepAbidingRating
        if (mOnRatingChangeListener != null) {
            mOnRatingChangeListener!!.onRatingChange(this, mRating, fromUser)
        }
        fillRatingBar(mRating)
    }

    override fun getRating(): Float {
        return mRating
    }

    // Unit is pixel
    override fun setStarWidth(@IntRange(from = 0) starWidth: Int) {
        mStarWidth = starWidth
        for (partialView in mPartialViews!!) {
            partialView.setStarWidth(starWidth)
        }
    }

    override fun getStarWidth(): Int {
        return mStarWidth
    }

    // Unit is pixel
    override fun setStarHeight(@IntRange(from = 0) starHeight: Int) {
        mStarHeight = starHeight
        for (partialView in mPartialViews!!) {
            partialView.setStarHeight(starHeight)
        }
    }

    override fun getStarHeight(): Int {
        return mStarHeight
    }

    override fun setStarPadding(ratingPadding: Int) {
        if (ratingPadding < 0) {
            return
        }
        mPadding = ratingPadding
        for (partialView in mPartialViews!!) {
            partialView.setPadding(mPadding, mPadding, mPadding, mPadding)
        }
    }

    override fun getStarPadding(): Int {
        return mPadding
    }

    override fun setEmptyDrawableRes(@DrawableRes res: Int) {
        val drawable = ContextCompat.getDrawable(context, res)
        drawable?.let { setEmptyDrawable(it) }
    }

    override fun setFilledDrawableRes(@DrawableRes res: Int) {
        val drawable = ContextCompat.getDrawable(context, res)
        drawable?.let { setFilledDrawable(it) }
    }

    override fun setEmptyDrawable(drawable: Drawable) {
        mEmptyDrawable = drawable
        for (partialView in mPartialViews!!) {
            partialView.setEmptyDrawable(drawable)
        }
    }

    override fun setFilledDrawable(drawable: Drawable) {
        mFilledDrawable = drawable
        for (partialView in mPartialViews!!) {
            partialView.setFilledDrawable(drawable)
        }
    }

    override fun setMinimumStars(@FloatRange(from = 0.0) minimumStars: Float) {
        mMinimumStars = RatingBarUtilsTrue.getValidMinimumStars(minimumStars, mNumStars, mStepSize)
    }

    override fun isIndicator(): Boolean {
        return mIsIndicator
    }

    override fun setIsIndicator(indicator: Boolean) {
        mIsIndicator = indicator
    }

    override fun isScrollable(): Boolean {
        return mIsScrollable
    }

    override fun setScrollable(scrollable: Boolean) {
        mIsScrollable = scrollable
    }

    override fun isClickable(): Boolean {
        return mIsClickable
    }

    override fun setClickable(clickable: Boolean) {
        mIsClickable = clickable
    }

    override fun setClearRatingEnabled(enabled: Boolean) {
        mClearRatingEnabled = enabled
    }

    override fun isClearRatingEnabled(): Boolean {
        return mClearRatingEnabled
    }

    override fun getStepSize(): Float {
        return mStepSize
    }

    override fun setStepSize(@FloatRange(from = 0.1, to = 1.0) stepSize: Float) {
        mStepSize = stepSize
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isIndicator()) {
            return false
        }
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = eventX
                mStartY = eventY
                mPreviousRating = mRating
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isScrollable()) {
                    return false
                }
                handleMoveEvent(eventX)
            }
            MotionEvent.ACTION_UP -> {
                if (!RatingBarUtilsTrue.isClickEvent(mStartX, mStartY, event) || !isClickable) {
                    return false
                }
                handleClickEvent(eventX)
            }
        }
        parent.requestDisallowInterceptTouchEvent(true)
        return true
    }

    private fun handleMoveEvent(eventX: Float) {
        for (partialView in mPartialViews!!) {
            if (eventX < partialView.width / 10f + mMinimumStars * partialView.width) {
                setRating(mMinimumStars, true)
                return
            }
            if (!isPositionInRatingView(eventX, partialView)) {
                continue
            }
            val rating = RatingBarUtilsTrue.calculateRating(partialView, mStepSize, eventX)
            if (mRating != rating) {
                setRating(rating, true)
            }
        }
    }

    private fun handleClickEvent(eventX: Float) {
        for (partialView in mPartialViews!!) {
            if (!isPositionInRatingView(eventX, partialView)) {
                continue
            }
            val rating =
                if (mStepSize == 1f) (partialView.tag as Int).toFloat() else RatingBarUtilsTrue.calculateRating(
                    partialView,
                    mStepSize,
                    eventX
                )
            if (mPreviousRating == rating && isClearRatingEnabled()) {
                setRating(mMinimumStars, true)
            } else {
                setRating(rating, true)
            }
            break
        }
    }

    private fun isPositionInRatingView(eventX: Float, ratingView: View): Boolean {
        return eventX > ratingView.left && eventX < ratingView.right
    }

//    fun setOnRatingChangeListener(onRatingChangeListener: OnRatingChangeListener?) {
//        mOnRatingChangeListener = onRatingChangeListener
//    }

//    override fun onSaveInstanceState(): Parcelable? {
//        val superState = super.onSaveInstanceState()
//        val ss = SavedState(superState)
//        ss.rating = mRating
//        return ss
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable) {
//        val ss = state as SavedState
//        super.onRestoreInstanceState(ss.superState)
//        setRating(ss.rating)
//    }

    companion object {
        const val TAG = "getDecimalFormat"
    }

    /**
     * @param context      context
     * @param attrs        attributes from XML => app:mainText="mainText"
     * @param defStyleAttr attributes from default style (Application theme or activity theme)
     */
    /* Call by xml layout */

    private var mEmptyView: ImageView? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseRatingBar)
        val rating = typedArray.getFloat(R.styleable.BaseRatingBar_srb_rating, 0f)
        initParamsValue(typedArray, context)
        verifyParamsValue()
        initRatingView()
        setRating(rating)
    }
}