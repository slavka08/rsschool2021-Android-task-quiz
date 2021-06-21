package com.rsschool.android2021.querstions

data class Question(
    val questionStr: String,
    val answersList:List<String>,
    val correctId: Int,
)

