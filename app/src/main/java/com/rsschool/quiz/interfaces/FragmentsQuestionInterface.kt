package com.rsschool.quiz.interfaces

import com.rsschool.android2021.querstions.Question

interface FragmentsQuestionInterface {
    fun openFragmentQuestion(question: Question?, qNumber: Int, theme: Int)
    fun setResult(qNumber: Int, answerId: Int, answer: Boolean)
    fun getQuestion(number: Int): Question?
    fun getResultIdIfExist(questionNum: Int): Int
    fun getQuestionsCount(): Int
}