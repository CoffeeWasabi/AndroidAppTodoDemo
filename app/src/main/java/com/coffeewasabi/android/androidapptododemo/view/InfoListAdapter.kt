package com.coffeewasabi.android.androidapptododemo.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coffeewasabi.android.androidapptododemo.databinding.RecyclerViewItemInfoListBinding
import com.coffeewasabi.android.androidapptododemo.entity.InfoTodoEntity
import com.coffeewasabi.android.androidapptododemo.fragment.InfoListFragment
import com.coffeewasabi.android.androidapptododemo.utils.TransDestination
import com.coffeewasabi.android.androidapptododemo.viewmodel.InfoTodoViewModel

private object InfoDiffCallback : DiffUtil.ItemCallback<InfoTodoEntity>() {
    override fun areItemsTheSame(oldItem: InfoTodoEntity, newItem: InfoTodoEntity): Boolean {
        return oldItem.info.id == newItem.info.id
    }

    override fun areContentsTheSame(oldItem: InfoTodoEntity, newItem: InfoTodoEntity): Boolean {
        return oldItem == newItem
    }
}

class InfoListAdapter(
    private val viewLifecyclerOwner: LifecycleOwner,
    private val viewModel: InfoTodoViewModel,
    private val fragment: InfoListFragment,
    ) : ListAdapter<InfoTodoEntity, InfoListAdapter.InfoViewHolder>(InfoDiffCallback) {

    class InfoViewHolder(private val binding: RecyclerViewItemInfoListBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: InfoTodoEntity, viewLifecycleOwner: LifecycleOwner, viewModel: InfoTodoViewModel, fragment: InfoListFragment) {
                binding.apply {
                    infoData = item
                    this.viewModel = viewModel
                    this.lifecycleOwner = viewLifecycleOwner

                    infoCard.setOnClickListener {
                        if(viewModel.deleteModeFlg.value!!){
                            val position = bindingAdapterPosition
                            infoCard.isChecked = !infoCard.isChecked
                            val deleteMode = viewModel.updateDeleteMode(infoCard.isChecked, position)

                            // チェック件数によってボタンを切り替える
                            if(!deleteMode){
                                fragment.onAnimationInfoActionFab(viewModel.isDeleteMode())
                            }
                        } else {
                            val position = bindingAdapterPosition
                            val infoData = viewModel.infoTodoList.value?.get(position)
                            if(infoData != null){
                                viewModel.editModeFlgDisable()
                                viewModel.onClickTransWithItem(TransDestination.VIEW, infoData)
                            }
                            else{
                                Log.e("InfoListAdapter","get data failed position["+position+"]")
                            }
                        }
                        true
                    }

                    infoCard.setOnLongClickListener {
                        val position = bindingAdapterPosition
                        infoCard.isChecked = !infoCard.isChecked
                        viewModel.updateDeleteMode(infoCard.isChecked, position)
                        fragment.onAnimationInfoActionFab(viewModel.isDeleteMode())
                        true
                    }

                }.run {
                    executePendingBindings()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InfoViewHolder(RecyclerViewItemInfoListBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(getItem(position), viewLifecyclerOwner, viewModel, fragment)
    }
}