package com.rsschool.quiz


import android.content.res.TypedArray
import android.os.Bundle
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rsschool.android2021.querstions.Question
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.fragments.QuestionFragment
import com.rsschool.quiz.fragments.ResultFragment
import com.rsschool.quiz.interfaces.FragmentsQuestionInterface
import com.rsschool.quiz.interfaces.ResultFragmentInterface
import com.rsschool.quiz.querstions.Const


val answers = mutableMapOf<Int, Boolean>()
val answersIds = mutableMapOf<Int, Int>()
val list = Const.getQuestions()

class MainActivity : AppCompatActivity(), FragmentsQuestionInterface, ResultFragmentInterface {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openQuestionFragment(list[0], 0)
        supportActionBar?.hide()
    }

    private fun openQuestionFragment(question: Question?, qNumber: Int) {
        val firstFragment: Fragment =
            QuestionFragment.newInstance(qNumber = qNumber, question = question)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, firstFragment)
        transaction.commit()
    }

    private fun openResultFragment(result: String) {
        val firstFragment: Fragment =
            ResultFragment.newInstance(result = result)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, firstFragment)
        transaction.commit()
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()
        val a: TypedArray =
            this.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }

    override fun setTheme(resId: Int) {
        super.setTheme(resId)

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = fetchAccentColor()
    }

    override fun openFragmentQuestion(question: Question?, qNumber: Int, theme: Int) {
        if (question != null) {
            openQuestionFragment(question = question, qNumber = qNumber)
            setTheme(theme)
        } else {
            setTheme(R.style.Theme_Quiz_Submit)
            openResultFragment(result = calcQuizScore())
        }
    }

    override fun setResult(qNumber: Int, answerId: Int, answer: Boolean) {
        answers[qNumber] = answer
        answersIds[qNumber] = answerId
    }

    override fun getQuestion(number: Int): Question? {
        if (number >= list.size) {
            return null
        }
        return list[number]
    }

    override fun getResultIdIfExist(questionNum: Int): Int {
        for (element in answersIds) {
            if (element.key == questionNum) {
                return element.value
            }
        }
        return -1
    }

    override fun getQuestionsCount(): Int {
        return list.count()
    }

    private fun calcQuizScore(): String {
        var correctAnswers = 0
        for (element in answers) {
            if (element.value) {
                ++correctAnswers
            }
        }
        return getString(R.string.uRes) + " " + correctAnswers + "/" + answers.size
    }

    override fun getShareResultStr(): String {
        val str = StringBuilder()

        str.append(calcQuizScore())
        str.appendLine()
        str.appendLine()
        for (i: Int in 0 until list.size) {
            str.append(i + 1)
            str.append(") ")
            str.append(list[i].questionStr)
            str.appendLine()
            str.append(getString(R.string.uAns))
            str.append(": ")
            str.append(list[i].answersList[getResultIdIfExist(i)])
            str.appendLine()
            str.appendLine()
        }
        return str.toString()
    }

    override fun restart() {
        answersIds.clear()
        answers.clear()
        openQuestionFragment(list[0], 0)
        setTheme(R.style.Theme_Quiz_First)
    }

    override fun closeApp() {
        finishAffinity()
    }

}