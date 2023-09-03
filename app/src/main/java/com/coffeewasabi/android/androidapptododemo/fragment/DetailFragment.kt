package com.coffeewasabi.android.androidapptododemo.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.coffeewasabi.android.androidapptododemo.MainApplication
import com.coffeewasabi.android.androidapptododemo.R
import com.coffeewasabi.android.androidapptododemo.database.DatabaseRepository
import com.coffeewasabi.android.androidapptododemo.databinding.FragmentDetailBinding
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity
import com.coffeewasabi.android.androidapptododemo.utils.getMainActivity
import com.coffeewasabi.android.androidapptododemo.view.DetailTodoListAdapter
import com.coffeewasabi.android.androidapptododemo.viewmodel.InfoTodoViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment: Fragment() {
    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: InfoTodoViewModel
    private lateinit var detailTodoListAdapter: DetailTodoListAdapter
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {

        activity?.run{
            viewModel = ViewModelProvider(
                this,
                InfoTodoViewModel.InfoTodoViewModelFactory(
                    DatabaseRepository(MainApplication.database.DatabaseDao())
                )
            ).get(InfoTodoViewModel::class.java)
        }

        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@DetailFragment.viewModel
            this.info = this@DetailFragment.viewModel.editInfo.value
            lifecycleOwner = viewLifecycleOwner

            todoViewListRecycler.run {
                layoutManager = LinearLayoutManager(context)
                adapter = DetailTodoListAdapter(viewLifecycleOwner, this@DetailFragment.viewModel).also {
                    detailTodoListAdapter = it
                }
            }
        }
        return binding.run {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 戻るボタン表示
        getMainActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.editTodoList.observe(viewLifecycleOwner){
            detailTodoListAdapter.submitList(it)
        }

        // スナックバーメッセージ
        viewModel.snackBarMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { event ->
                if(!event.isNullOrEmpty()) {
                    Snackbar.make(view, event, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    viewModel.initSnackBarMessage()
                }
            }
        })

        // Todo追加
        binding.todoTextLayout.setEndIconOnClickListener {
            val todoName = binding.todoEdit.text

            var infoId = 0L
            if(viewModel.editInfo.value != null){
                infoId = viewModel.editInfo.value!!.id
            }

            // sortNoはリスト数 + 1
            var sortNo = 1
            if(viewModel.editTodoList.value != null){
                sortNo = viewModel.editTodoList.value!!.size
            }

            val todoEntity = TodoEntity(0, infoId,
                todoName.toString(), MutableLiveData(false), sortNo + 1, Date(), Date()
            )
            viewModel.addTodoByEdit(todoEntity)

            // 入力エリアをクリア
            binding.todoEdit.setText(null)
        }

        binding.detailActionFab.setOnClickListener {
            if(viewModel.isEditModeFlg()){
                // edit → view
                viewModel.onRegistUpdate()
                viewModel.editModeFlgDisable()
            } else {
                // view → edit
                viewModel.editModeFlgEnable()
            }
            onAnimationDetailActionFab()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                // 戻るボタンの制御のみ行いたいので、ここでは何もしない
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    android.R.id.home -> {
                        val editInfo = viewModel.editInfo.value
                        editInfo?.let {
                            if(it.id !== 0L && viewModel.isEditModeFlg()) {
                                // 既に登録されているTODOを編集している場合
                                viewModel.editModeFlgDisable()
                            } else {
                                // 上記以外は全てhomeに戻る
                                return false
                            }
                        }?:run {
                            return false
                        }
                    }
                    else -> {
                        Log.e("DetailFragment", "onMenuItemSelected when ???")
                        return false
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onAnimationDetailActionFab() {
        val fab = binding.detailActionFab
        if(viewModel.isEditModeFlg()){
            fab.animate()
                .rotationBy(180F)   // ローテ―ション
                .setDuration(300)   // フレームを表示する時間
                .scaleX(1.5f)   //  指定したサイズにセット
                .scaleY(1.5f)   //  指定したサイズにセット
                .withEndAction{
                    fab.setImageResource(R.drawable.ic_create)
                    fab.animate()
                        .rotationBy(180F)   // ローテ―ション
                        .setDuration(300)   // フレームを表示する時間
                        .scaleX(1.0f)   //  指定したサイズにセット
                        .scaleY(1.0f)   //  指定したサイズにセット
                }
        } else {
            fab.animate()
                .rotationBy(180F)   // ローテ―ション
                .setDuration(300)   // フレームを表示する時間
                .scaleX(1.5f)   //  指定したサイズにセット
                .scaleY(1.5f)   //  指定したサイズにセット
                .withEndAction{
                    fab.setImageResource(R.drawable.ic_sunny)
                    fab.animate()
                        .rotationBy(180F)   // ローテ―ション
                        .setDuration(300)   // フレームを表示する時間
                        .scaleX(1.0f)   //  指定したサイズにセット
                        .scaleY(1.0f)   //  指定したサイズにセット
                }
        }
    }
}