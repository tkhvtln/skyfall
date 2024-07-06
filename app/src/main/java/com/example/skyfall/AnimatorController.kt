package com.example.skyfall

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class AnimatorController {
     fun move(view: ImageView, image: Int, duration: Long) {
        val moveLeft = ObjectAnimator.ofFloat(view, "translationX", 0f, -1000f).setDuration(duration)
        val moveRight = ObjectAnimator.ofFloat(view, "translationX", 1000f, 0f).setDuration(duration)

        moveLeft.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                view.setImageResource(image)
                moveRight.start()
            }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }
        })

        moveLeft.start()
    }

    fun move(view: TextView, text: String, duration: Long) {
        val moveLeft = ObjectAnimator.ofFloat(view, "translationX", 0f, -1000f).setDuration(duration)
        val moveRight = ObjectAnimator.ofFloat(view, "translationX", 1000f, 0f).setDuration(duration)

        moveLeft.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                view.text = text
                moveRight.start()
            }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }
        })

        moveLeft.start()
    }

    fun fade(view: TextView, text: String, duration: Long) {
        val fadeStart = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).setDuration(duration)
        val fadeEnd = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration)

        fadeStart.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                view.text = text
                fadeEnd.start()
            }

            override fun onAnimationCancel(animation: Animator) { }

            override fun onAnimationRepeat(animation: Animator) { }
        })

        fadeStart.start()
    }

    fun changeBackground(view: View, backgroundOld: Int, backgroundNew: Int, duration: Int) {
        val start = ContextCompat.getDrawable(view.context, backgroundOld)
        val end = ContextCompat.getDrawable(view.context, backgroundNew)
        val transitionDrawable = TransitionDrawable(arrayOf(start, end))

        view.background = transitionDrawable
        transitionDrawable.startTransition(duration)
    }

    fun rotate(view: View, duration: Long) {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotation.duration = duration
        rotation.repeatCount = ObjectAnimator.INFINITE
        rotation.start()
    }
}