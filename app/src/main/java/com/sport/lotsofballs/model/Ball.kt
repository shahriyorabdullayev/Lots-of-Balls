package com.sport.lotsofballs.model

import com.sport.lotsofballs.R

data class Ball(
    val x: Float,
    val y: Float,
    val startOffset: Long,
    val drawable: Int = R.drawable.balll
)
