package com.rsschool.quiz.querstions

import com.rsschool.android2021.querstions.Question
import java.util.*

object Const {
    fun getQuestions(): ArrayList<Question> {
        val qList = ArrayList<Question>()

        qList.add(
            Question(
                "2+2*2=?", listOf(
                    "6",
                    "8",
                    "10",
                    "4",
                    "2"
                ), 0
            )
        )
        qList.add(
            Question(
                "What is the capital of England?",
                listOf(
                    "Dublin",
                    "Liverpool",
                    "Manchester",
                    "London",
                    "New York"
                ),
                3
            )
        )
        qList.add(
            Question(
                "Kotlin Short max value?",
                listOf(
                    "255",
                    "2,147,483,647",
                    "32767",
                    "127",
                    "65535"
                ),
                2
            )
        )
        qList.add(
            Question(
                "Current president of USA?",
                listOf(
                    "Donald Trump",
                    "Joe Biden",
                    "Bill Clinton",
                    "Richard Nixon",
                    "George H. W. Bush"
                ),
                1
            )
        )
        qList.add(
            Question(
                "Android 'Marshmallow' is api version?",
                listOf(
                    "17",
                    "21",
                    "29",
                    "23",
                    "25"
                ),
                3
            )
        )

        return qList
    }
}