package com.eliadp.androidshowcase.task.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eliadp.androidshowcase.R
import com.eliadp.androidshowcase.databinding.FragmentAddEditTaskBinding
import com.eliadp.androidshowcase.extensions.exhaustive
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AddEditTaskFragmentArgs>()
    private val viewModel: AddEditTaskViewModel by viewModel {
        parametersOf(args.taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            checkBoxImportant.jumpDrawablesToCurrentState()

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClicked(
                    editTextTaskName.text.toString(),
                    checkBoxImportant.isChecked,
                )
            }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }

        viewModel.handleActions { action ->
            handleAction(action)
        }
    }

    private fun handleState(state: AddEditState) {
        when (state) {
            is AddEditState.Data -> {
                with(binding) {
                    with(state.task) {
                        editTextTaskName.setText(name)
                        checkBoxImportant.isChecked = isImportant
                        textViewDateCreated.isVisible = true
                        textViewDateCreated.text =
                            getString(R.string.add_edit_creation_date, creationDate)
                    }
                }
            }
            AddEditState.Loading -> Unit
        }.exhaustive
    }

    private fun handleAction(action: AddEditAction) {
        when (action) {
            is AddEditAction.NavigateBackWithResult -> {
                binding.editTextTaskName.clearFocus()
                setFragmentResult(
                    ADD_EDIT_REQUEST_KEY,
                    bundleOf(ADD_EDIT_RESULT_KEY to action.result),
                )
                findNavController().popBackStack()
            }
            AddEditAction.ShowInvalidInputMessage -> {
                Snackbar.make(
                    requireView(),
                    R.string.invalid_input_message,
                    Snackbar.LENGTH_LONG,
                ).show()
            }
        }.exhaustive
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ADD_EDIT_REQUEST_KEY = "add_edit_request"
        const val ADD_EDIT_RESULT_KEY = "add_edit_result"
    }
}
