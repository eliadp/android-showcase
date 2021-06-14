package com.eliadp.androidshowcase.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliadp.androidshowcase.databinding.ItemTaskBinding
import com.eliadp.androidshowcase.task.entities.TaskUIModel

interface TaskListListener {
    fun onItemClicked(task: TaskUIModel)
    fun onCheckBoxClicked(task: TaskUIModel, isChecked: Boolean)
}

class TaskListAdapter(private val listener: TaskListListener) :
    ListAdapter<TaskUIModel, TaskListAdapter.TaskListViewHolder>(TaskListAdapterItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class TaskListViewHolder(
        private val binding: ItemTaskBinding,
        private val listener: TaskListListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskUIModel) {
            with(binding) {
                itemView.setOnClickListener {
                    listener.onItemClicked(task)
                }
                checkBoxCompleted.setOnClickListener {
                    listener.onCheckBoxClicked(task, checkBoxCompleted.isChecked)
                }

                checkBoxCompleted.isChecked = task.isCompleted
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.isCompleted
                labelPriority.isVisible = task.isImportant

                checkBoxCompleted.jumpDrawablesToCurrentState()
            }
        }
    }
}

object TaskListAdapterItemCallback : DiffUtil.ItemCallback<TaskUIModel>() {
    override fun areItemsTheSame(oldItem: TaskUIModel, newItem: TaskUIModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TaskUIModel, newItem: TaskUIModel) =
        oldItem == newItem
}
