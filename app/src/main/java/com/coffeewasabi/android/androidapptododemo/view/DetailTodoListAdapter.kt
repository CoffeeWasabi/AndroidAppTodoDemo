package com.coffeewasabi.android.androidapptododemo.view

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coffeewasabi.android.androidapptododemo.databinding.RecyclerViewDetailTodoListBinding
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity
import com.coffeewasabi.android.androidapptododemo.viewmodel.InfoTodoViewModel

private object DetailTodoDiffCallback : DiffUtil.ItemCallback<TodoEntity>() {
    override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
        return oldItem == newItem
    }
}

class DetailTodoListAdapter (
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: InfoTodoViewModel
) : ListAdapter<TodoEntity, DetailTodoListAdapter.DetailTodoViewHolder>(DetailTodoDiffCallback) {

    class DetailTodoViewHolder (private val binding: RecyclerViewDetailTodoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: TodoEntity,
            viewLifecycleOwner: LifecycleOwner,
            viewModel: InfoTodoViewModel
        ) {
            binding.apply {
                todo = item
                this.viewModel = viewModel
                lifecycleOwner = viewLifecycleOwner
                viewCheckBox.setOnCheckedChangeListener { view, isChecked ->
                    // 取り消し線
                    if(isChecked){
                        view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    } else {
                        view.paintFlags = Paint.ANTI_ALIAS_FLAG
                    }

                    // 更新設定
                    viewModel.onCheckedByView(bindingAdapterPosition, isChecked)
                }
                todoName.setEndIconOnClickListener {
                    val removeTodoEntity = viewModel.getTodoByEdit(bindingAdapterPosition)
                    removeTodoEntity?.let {
                        viewModel.removeTodoByEdit(removeTodoEntity)
                    }
                }
            }.run {
                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailTodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailTodoViewHolder(RecyclerViewDetailTodoListBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: DetailTodoViewHolder, position: Int) {
        holder.bind(getItem(position), viewLifecycleOwner, viewModel)
    }
}