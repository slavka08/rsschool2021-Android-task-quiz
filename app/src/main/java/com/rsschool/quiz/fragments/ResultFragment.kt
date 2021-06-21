package com.rsschool.quiz.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.interfaces.ResultFragmentInterface


private const val ARG_RESULT = "result"


class ResultFragment : Fragment() {
    private var fragTrans: ResultFragmentInterface? = null
    private var _binding: FragmentResultBinding? = null
    private val binding
        get() = _binding!!

    private var result: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getString(ARG_RESULT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.textViewResult.text = result

        binding.imageViewShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, fragTrans?.getShareResultStr())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.imageViewBack.setOnClickListener {
            fragTrans?.restart()
        }

        binding.imageViewCloseApp.setOnClickListener {
            fragTrans?.closeApp()
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragTrans = context as ResultFragmentInterface

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    remove()
                    fragTrans?.restart()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(result: String) =
            ResultFragment().apply {
                arguments = bundleOf(
                    ARG_RESULT to result,
                )
            }
    }
}