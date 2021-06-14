package com.eliadp.androidshowcase.task

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eliadp.androidshowcase.R
import com.eliadp.androidshowcase.databinding.FragmentTaskListBinding
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.extensions.exhaustive
import com.eliadp.androidshowcase.extensions.navigateSafely
import com.eliadp.androidshowcase.extensions.onQueryTextChanged
import com.eliadp.androidshowcase.task.addedit.AddEditTaskFragment.Companion.ADD_EDIT_REQUEST_KEY
import com.eliadp.androidshowcase.task.addedit.AddEditTaskFragment.Companion.ADD_EDIT_RESULT_KEY
import com.eliadp.androidshowcase.task.entities.TaskUIModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by viewModel()

    private val taskListAdapter = TaskListAdapter(object : TaskListListener {
        override fun onItemClicked(task: TaskUIModel) {
            viewModel.onTaskSelected(task)
        }

        override fun onCheckBoxClicked(task: TaskUIModel, isChecked: Boolean) {
            viewModel.onTaskCheckedChanged(task, isChecked)
        }
    })

    private lateinit var searchView: SearchView
    private lateinit var hideCompletedTaskMenuItem: MenuItem
    private lateinit var searchItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerViewTasks.apply {
                adapter = taskListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAddTask.setOnClickListener {
                viewModel.onAddNewTaskClicked()
            }
        }

        setFragmentResultListener(ADD_EDIT_REQUEST_KEY) { _, bundle ->
            val result = bundle.getInt(ADD_EDIT_RESULT_KEY)
            viewModel.onAddEditResult(result)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            handleState(state)
        }

        viewModel.handleActions { action ->
            handleAction(action)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        hideCompletedTaskMenuItem = menu.findItem(R.id.action_hide_completed_tasks)

        searchView.onQueryTextChanged {
            viewModel.onQueryTextChanged(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClicked(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompletedClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleState(state: TaskListState) {
        when (state) {
            is TaskListState.Data -> binding.bindDataState(state)
            is TaskListState.Empty -> {
                with(binding) {
                    labelNoElement.visibility = View.VISIBLE
                    recyclerViewTasks.visibility = View.GONE
                }

                bindSearchView(state.query)
            }
            TaskListState.Loading -> Unit
        }.exhaustive
    }

    private fun FragmentTaskListBinding.bindDataState(state: TaskListState.Data) {
        if (::hideCompletedTaskMenuItem.isInitialized) {
            hideCompletedTaskMenuItem.isChecked = state.hideCompleted
        }

        labelNoElement.visibility = View.GONE
        binding.recyclerViewTasks.visibility = View.VISIBLE
        taskListAdapter.submitList(state.tasks)

        bindSearchView(state.query)
    }

    private fun bindSearchView(query: String) {
        if (::searchItem.isInitialized && query.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(query, false)
        }
    }

    private fun handleAction(action: TaskListAction) {
        when (action) {
            TaskListAction.NavigateToAddTask -> {
                findNavController().navigateSafely(
                    TaskListFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                        null,
                        getString(R.string.add_edit_title_new),
                    )
                )
            }
            TaskListAction.ShowDeleteConfirmationDialog -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_dialog_title)
                    .setMessage(R.string.delete_dialog_message)
                    .setNegativeButton(R.string.delete_dialog_cancel, null)
                    .setPositiveButton(R.string.delete_dialog_yes) { _, _ ->
                        viewModel.onDeleteConfirmClicked()
                    }
                    .show()
            }
            is TaskListAction.NavigateToEditTask -> {
                findNavController().navigateSafely(
                    TaskListFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                        action.taskId,
                        getString(R.string.add_edit_title_edit),
                    )
                )
            }
            TaskListAction.ShowTaskAddedConfirmationMessage -> {
                Snackbar.make(
                    requireView(),
                    R.string.confirmation_task_added,
                    Snackbar.LENGTH_SHORT,
                ).show()
            }
            TaskListAction.ShowTaskUpdatedConfirmationMessage -> {
                Snackbar.make(
                    requireView(),
                    R.string.confirmation_task_updated,
                    Snackbar.LENGTH_SHORT,
                ).show()
            }
        }.exhaustive
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView.setOnQueryTextListener(null)
        _binding = null
    }
}
