package com.sport.lotsofballs.fragments

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sport.lotsofballs.R
import com.sport.lotsofballs.databinding.FragmentGameBinding
import com.sport.lotsofballs.model.Ball
import com.sport.lotsofballs.util.*
import com.sport.lotsofballs.util.Constants.RECORD_ONE
import com.sport.lotsofballs.util.Constants.RECORD_THREE
import com.sport.lotsofballs.util.Constants.RECORD_TWO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.cos


class GameFragment : Fragment(R.layout.fragment_game) {

    private val TAG = "@@@"

    private val binding by viewBinding { FragmentGameBinding.bind(it) }

    private var job: Job? = null
    private var jobResult: Job? = null

    var count = 0

    private var result = 0
    private var missed = 0

    private var rotateCount = 0
    private var limit = 0f
    private var isRotate = true

    private lateinit var ballImageView: ImageView
    private lateinit var ballImageViewList: ArrayList<ImageView>

    private lateinit var shar: ImageView

    private var isLeft = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shar = binding.imgShar
        ballImageViewList = ArrayList()
        val outLineSpan = OutlineSpan(
            strokeColor = Color.RED,
            strokeWidth = 4f
        )
        val resultText = resources.getString(R.string.result) + " "
        val missedText = resources.getString(R.string.missed) + " "
        val spannableResultText = SpannableString(resultText)
        val spannableMissedText = SpannableString(missedText)
        spannableResultText.setSpan(outLineSpan,
            0,
            resultText.length - 1,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableMissedText.setSpan(outLineSpan,
            0,
            missedText.length - 1,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        rotateShar(isRotate)

        binding.apply {

            tvResult.text = spannableResultText
            tvMissed.text = spannableMissedText

            jobResult = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                while (isActive) {
                    delay(10)
                    if (missed >= 30) {
                        showResultDialog()
                        job?.cancel()
                        jobResult?.cancel()

                    }
                }
            }

            bgGame.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        bgGame.setImageResource(R.drawable.bg_game_2)
                        job = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            while (isActive) {
                                delay(100)
                                if (count <= ballImageViewList.size) {
                                    if (limit + 50 in 360.0..400.0) {
                                        val ball = ballImageViewList[count]
                                        ballAnim3(
                                            ball,
                                            fromY = 350f,
                                            mStartOffset = 0,
                                            toY = binding.imgShar.y + 150,
                                        )
                                        count++
                                        if (count == ballImageViewList.size) {
                                            generateBalls()
                                        }
                                        result++
                                        binding.tvResultCount.text = result.toString()


                                    } else {

                                        val ball = ballImageViewList[count]
                                        ballAnim2(ball,
                                            fromY = 350f,
                                            mStartOffset = 0,
                                            toY = binding.imgShar.y - 30)
                                        count++

                                        if (count == ballImageViewList.size) {
                                            generateBalls()
                                        }
                                        missed++
                                        binding.tvMissedCount.text = missed.toString()
                                    }
                                }
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        bgGame.setImageResource(R.drawable.bg_game_1)
                        job?.cancel()
                    }
                }
                true
            }

            generateBalls()


        }

    }

    private fun showResultDialog() {
        val dialog = Dialog(requireContext(),
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        dialog.apply {
            dialog.setContentView(R.layout.game_result_dialog)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            findViewById<TextView>(R.id.btn_back_dialog).setOnClickListener {
                requireActivity().onBackPressed()
                dismiss()
            }



            if (result >= getScoreString(RECORD_ONE) && result > getScoreString(RECORD_TWO) && result > getScoreString(
                    RECORD_THREE)
            ) {
                saveString(RECORD_ONE, result.toString())
            } else if (result < getScoreString(RECORD_ONE) && result >= getScoreString(RECORD_TWO) && result > getScoreString(
                    RECORD_THREE)
            ) {
                saveString(RECORD_TWO, result.toString())
            } else if (result < getScoreString(RECORD_ONE) && result < getScoreString(RECORD_TWO) && result >= getScoreString(
                    RECORD_THREE)
            ) {
                saveString(RECORD_THREE, result.toString())
            }

            findViewById<TextView>(R.id.tv_dialog_result).text = result.toString()

            findViewById<TextView>(R.id.tv_dialog_best_result).text =
                getScoreString(RECORD_ONE).toString()


            show()

        }

    }


    private fun rotateShar(rotate: Boolean) {
        val value = if (rotate) 360f else -360f

        shar
            .animate()
            .rotationBy(value)
            .withEndAction(object : Runnable {
                override fun run() {
                    binding.imgShar
                        .animate()
                        .rotationBy(value)
                        .withEndAction(this)
                        .setDuration(5000)
                        .setInterpolator(LinearInterpolator())
                        .start()
                }

            })
            .setDuration(5000)
            .setInterpolator(LinearInterpolator())
            .setUpdateListener {
                limit = abs(binding.imgShar.rotation) - 360f * rotateCount
            }
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    rotateCount += 1
                }

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationRepeat(p0: Animator?) {}

            })
            .start()
    }

    private fun generateBalls() {
        ball(Ball(270f, 0f, 0), 350f)
        ball(Ball(340f, 0f, 0), 350f)
        ball(Ball(410f, 0f, 0), 350f)
        ball(Ball(220f, -60f, 0), 370f)
        ball(Ball(300f, -60f, 0), 360f)
        ball(Ball(380f, -60f, 0), 360f)
        ball(Ball(450f, -60f, 0), 370f)
        ball(Ball(250f, -120f, 0), 370f)
        ball(Ball(340f, -120f, 0), 370f)
        ball(Ball(430f, -120f, 0), 370f)
    }

    private fun ball(ball: Ball, toY: Float) {
        ballImageView = ImageView(requireContext())
        binding.parentConstraint.addView(ballImageView)
        ballImageView.apply {
            layoutParams.width = 60
            layoutParams.height = 60
            x = ball.x
            y = ball.y
            setImageResource(ball.drawable)
        }
        ballImageViewList.add(ballImageView)
        ballAnim(ballImageView, mStartOffset = ball.startOffset, toY = toY)

    }

    private fun ballAnim(
        imageView: ImageView,
        fromX: Float = 0f,
        toX: Float = 0f,
        fromY: Float = 0f,
        toY: Float,
        mStartOffset: Long,

        ) {
        val transAnim = TranslateAnimation(
            fromX, toX, fromY, toY
        )
        transAnim.apply {
            startOffset = mStartOffset
            duration = 500
            fillAfter = true
            interpolator = BetterBounceInterpolator(1, 1.0)
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
        }
        imageView.startAnimation(transAnim)
    }

    private fun ballAnim2(
        imageView: ImageView,
        fromX: Float = 0f,
        toX: Float = 0f,
        fromY: Float = 0f,
        toY: Float,
        mStartOffset: Long,
    ) {
        val transAnim = TranslateAnimation(
            fromX, toX, fromY, toY
        )
        transAnim.apply {
            startOffset = mStartOffset
            duration = 500
            fillAfter = true
            interpolator = BetterBounceInterpolator(1, 1.1)
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    val transFormation = Transformation()
                    val endTime = transAnim.startTime + transAnim.duration
                    transAnim.getTransformation(endTime, transFormation)

                    val matrix = transFormation.matrix
                    val matrixValue = FloatArray(9)
                    matrix.getValues(matrixValue)
                    val xTraveled = matrixValue[6]
                    val yTraveled = matrixValue[5]
                    isLeft = !isLeft
                    startRandomAnim(imageView, isLeft, yTraveled)


                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
        }
        imageView.startAnimation(transAnim)
    }

    private fun startRandomAnim(imageView: ImageView, isLeft: Boolean, fromY: Float) {
        val toX = if (isLeft) 1200f else -1200f
        val toY = if (isLeft) -300f else -300f
        val transAnimation = TranslateAnimation(
            binding.imgShar.x, toX, fromY, toY
        )
        transAnimation.apply {
            startOffset = 0
            duration = 700
            fillAfter = true
            interpolator = LinearInterpolator()
        }
        imageView.startAnimation(transAnimation)
    }

    private fun ballAnim3(
        imageView: ImageView,
        fromX: Float = 0f,
        toX: Float = 0f,
        fromY: Float = 0f,
        toY: Float,
        mStartOffset: Long,
    ) {
        val transAnim = TranslateAnimation(
            fromX, toX, fromY, toY
        )
        transAnim.apply {
            startOffset = mStartOffset
            duration = 500
            fillAfter = true
            interpolator = BetterBounceInterpolator(1, 1.0)
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    imageView.clearAnimation()
                    imageView.gone()

                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
        }
        imageView.startAnimation(transAnim)
    }


    private fun View.absX(): Float {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[0].toFloat()
    }

    private fun View.absY(): Float {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return location[1].toFloat()
    }

    fun View.getLocationOnScreen(): Point {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        return Point(location[0], location[1])
    }

    inner class BetterBounceInterpolator(val bounces: Int, val energy: Double) : Interpolator {
        override fun getInterpolation(x: Float): Float =
            (1.0 + (-abs(cos(x * 10 * bounces / Math.PI)) * getCurveAdjustment(x))).toFloat()

        private fun getCurveAdjustment(x: Float): Double =
            -(2 * (1 - x) * x * energy + x * x) + 1
    }


    override fun onDestroyView() {
        super.onDestroyView()
        shar.animate().cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        shar.animate().cancel()
    }


}