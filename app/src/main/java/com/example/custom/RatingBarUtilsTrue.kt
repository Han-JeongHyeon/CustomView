package com.example.custom

import android.view.MotionEvent
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.abs

class RatingBarUtilsTrue {

    companion object {

        private var mDecimalFormat: DecimalFormat? = null
        private val MAX_CLICK_DISTANCE = 5
        private val MAX_CLICK_DURATION = 200

        fun isClickEvent(startX: Float, startY: Float, event: MotionEvent): Boolean {
            val duration = (event.eventTime - event.downTime).toFloat()
            if (duration > MAX_CLICK_DURATION) {
                return false
            }
            val differenceX = abs(startX - event.x)
            val differenceY = abs(startY - event.y)
            return !(differenceX > MAX_CLICK_DISTANCE || differenceY > MAX_CLICK_DISTANCE)
        }



        fun calculateRating(partialView: PartialViewTrue, stepSize: Float, eventX: Float): Float {
            val decimalFormat = getDecimalFormat()
            val ratioOfView =
                decimalFormat!!.format(((eventX - partialView.left) / partialView.width).toDouble())
                    .toFloat()
            val steps = Math.round(ratioOfView / stepSize) * stepSize
            return decimalFormat.format((partialView.tag as Int - (1 - steps)).toDouble()).toFloat()
        }

        fun getValidMinimumStars(minimumStars: Float, numStars: Int, stepSize: Float): Float {
            var minimumStars = minimumStars
            if (minimumStars < 0) {
                minimumStars = 0f
            }
            if (minimumStars > numStars) {
                minimumStars = numStars.toFloat()
            }
            if (minimumStars % stepSize != 0f) {
                minimumStars = stepSize
            }
            return minimumStars
        }

        fun getDecimalFormat(): DecimalFormat? {
            if (mDecimalFormat == null) {
                val symbols = DecimalFormatSymbols(Locale.ENGLISH)
                symbols.decimalSeparator = '.'
                mDecimalFormat = DecimalFormat("#.##", symbols)
            }
            return mDecimalFormat
        }

    }

}