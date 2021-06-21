package com.rsschool.quiz.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.android2021.querstions.Question
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.interfaces.FragmentsQuestionInterface


private const val ARG_PARAM_QUESTION = "param1"
private const val ARG_PARAM_NUMBER_QUESTION = "param2"
private const val ARG_PARAM_Q_ANS1 = "ans1"
private const val ARG_PARAM_Q_ANS2 = "ans2"
private const val ARG_PARAM_Q_ANS3 = "ans3"
private const val ARG_PARAM_Q_ANS4 = "ans4"
private const val ARG_PARAM_Q_ANS5 = "ans5"
private const val ARG_PARAM_Q_CORRECT_ID = "answer_correct"


class QuestionFragment : Fragment() {
    private var fragTrans: FragmentsQuestionInterface? = null
    private var _binding: FragmentQuizBinding? = null
    private val binding
        get() = _binding!!

    private var numberQuestion: Int = 0
    private var correctAns: Int? = null
    private var qString: String? = null
    private var qAnswers: ArrayList<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numberQuestion = it.getInt(ARG_PARAM_NUMBER_QUESTION)
            qString = it.getString(ARG_PARAM_QUESTION)
            qAnswers.add(it.getString(ARG_PARAM_Q_ANS1))
            qAnswers.add(it.getString(ARG_PARAM_Q_ANS2))
            qAnswers.add(it.getString(ARG_PARAM_Q_ANS3))
            qAnswers.add(it.getString(ARG_PARAM_Q_ANS4))
            qAnswers.add(it.getString(ARG_PARAM_Q_ANS5))
            correctAns = it.getInt(ARG_PARAM_Q_CORRECT_ID)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragTrans = context as FragmentsQuestionInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.question.text = qString

        binding.toolbar.title = getString(R.string.question) + " " + (numberQuestion + 1)
        fragTrans?.getResultIdIfExist(questionNum = numberQuestion)?.let {
            val idRadioButton = getIdRadioButton(it)
            binding.radioGroup.check(idRadioButton)
            binding.nextButton.isEnabled = (idRadioButton > -1)
        }

        if (numberQuestion > 0) {
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        remove()
                        setFragmentQuestion(forwardDirection = false)
                    }
                }
            )
        }

        if (numberQuestion > 0) {
            binding.toolbar.navigationIcon = context?.let {
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.ic_baseline_chevron_left_24
                )
            }
        } else {
            binding.toolbar.navigationIcon = (null)
        }

        for (i: Int in 0..4) {
            (binding.radioGroup.getChildAt(i) as (RadioButton)).text = qAnswers[i]
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.nextButton.isEnabled = (checkedId > -1)
        }

        binding.nextButton.setOnClickListener {
            setFragmentQuestion(forwardDirection = true)
        }

        binding.previousButton.setOnClickListener {
            setFragmentQuestion(forwardDirection = false)
        }

        binding.toolbar.setNavigationOnClickListener {
            setFragmentQuestion(forwardDirection = false)
        }

        binding.previousButton.isEnabled = numberQuestion != 0

        val questionCount = fragTrans?.getQuestionsCount()
        if (questionCount != null) {
            binding.nextButton.text =
                if (numberQuestion < questionCount - 1) getString(R.string.next) else getString(
                    R.string.submit
                )
        }
        return binding.root
    }

    private fun setFragmentQuestion(forwardDirection: Boolean) {
        val answerId = getAnswerId()
        fragTrans?.setResult(
            qNumber = numberQuestion,
            answer = (answerId == correctAns),
            answerId = answerId
        )
        if (forwardDirection) {
            numberQuestion++
        } else {
            numberQuestion--
        }
        fragTrans?.openFragmentQuestion(
            question = fragTrans?.getQuestion(numberQuestion),
            theme = getTheme(numberQuestion),
            qNumber = numberQuestion
        )
    }

    private fun getIdRadioButton(num: Int): Int {
        return when (num) {
            0 -> R.id.option_one
            1 -> R.id.option_two
            2 -> R.id.option_three
            3 -> R.id.option_four
            4 -> R.id.option_five
            else -> -1
        }
    }

    private fun getTheme(num: Int): Int {
        return when (num) {
            0 -> R.style.Theme_Quiz_First
            1 -> R.style.Theme_Quiz_Second
            2 -> R.style.Theme_Quiz_Fourth
            3 -> R.style.Theme_Quiz_Third
            4 -> R.style.Theme_Quiz_Five
            else -> 0
        }

    }

    private fun getAnswerId(): Int {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.option_one -> 0
            R.id.option_two -> 1
            R.id.option_three -> 2
            R.id.option_four -> 3
            R.id.option_five -> 4
            else -> -1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(qNumber: Int, question: Question?) =
            QuestionFragment().apply {
                if (question != null) {
                    arguments = bundleOf(
                        ARG_PARAM_QUESTION to question.questionStr,
                        ARG_PARAM_NUMBER_QUESTION to qNumber,
                        ARG_PARAM_Q_ANS1 to question.answersList[0],
                        ARG_PARAM_Q_ANS2 to question.answersList[1],
                        ARG_PARAM_Q_ANS3 to question.answersList[2],
                        ARG_PARAM_Q_ANS4 to question.answersList[3],
                        ARG_PARAM_Q_ANS5 to question.answersList[4],
                        ARG_PARAM_Q_CORRECT_ID to question.correctId,
                    )
                }
            }
    }
}