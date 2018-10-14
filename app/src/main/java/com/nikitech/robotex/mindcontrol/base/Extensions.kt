package mobile.ecofleet.com.common.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.widget.*

/**
 * Created by aareundo on 03/07/2017.
 */

fun TextView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun ImageView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun Switch.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun ScrollView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun ListView.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun View.setFrame(x: Int, y: Int, width: Int, height: Int) {

    val params = RelativeLayout.LayoutParams(width, height)
    params.leftMargin = x
    params.topMargin = y

    layoutParams = params
}

fun BaseView.isLargeTablet(): Boolean {

    val metrics = getMetrics()
    val width = metrics.widthPixels
    val height = metrics.heightPixels
    val density = metrics.density

    var greater = height
    var lesser = width

    if (isLandScape) {
        greater = width
        lesser = height
    }

    if (density > 2.5) {
        // If density is too large, it'll be a phone
        return false
    }

    return greater > 1920 && lesser > 1080
}

val BaseView.isLandScape: Boolean
    get() = frame.width > frame.height

fun View.hide(complete: (success: Boolean) -> Unit) {
    animateAlpha(0.0f, complete)
}

fun View.show(complete: (success: Boolean) -> Unit) {
    animateAlpha(1.0f, complete)
}

val duration: Long = 200

fun View.animateAlpha(to: Float, complete: (success: Boolean) -> Unit) {
    val animator = ObjectAnimator.ofFloat(this, "alpha", to)
    animator.duration = duration
    animator.start()

    animator.addListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator?) { }
        override fun onAnimationRepeat(animation: Animator?) { }

        override fun onAnimationCancel(animation: Animator?) {
            complete(false)
        }

        override fun onAnimationEnd(animation: Animator?) {
            complete(true)
        }

    })
}

fun View.animateY(to: Float, complete: (success: Boolean) -> Unit) {
    val animator = ObjectAnimator.ofFloat(this, "y", to)
    animator.duration = duration
    animator.start()

    animator.addListener(object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator?) { }
        override fun onAnimationRepeat(animation: Animator?) { }

        override fun onAnimationCancel(animation: Animator?) {
            complete(false)
        }

        override fun onAnimationEnd(animation: Animator?) {
            complete(true)
        }

    })
}