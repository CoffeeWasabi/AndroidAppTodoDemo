package com.coffeewasabi.android.androidapptododemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coffeewasabi.android.androidapptododemo.MainApplication
import com.coffeewasabi.android.androidapptododemo.R
import com.coffeewasabi.android.androidapptododemo.database.DatabaseRepository
import com.coffeewasabi.android.androidapptododemo.databinding.FragmentInfoListBinding
import com.coffeewasabi.android.androidapptododemo.utils.TransDestination
import com.coffeewasabi.android.androidapptododemo.utils.getMainActivity
import com.coffeewasabi.android.androidapptododemo.view.InfoListAdapter
import com.coffeewasabi.android.androidapptododemo.viewmodel.InfoTodoViewModel
import com.google.android.material.snackbar.Snackbar

class InfoListFragment: Fragment() {

    private lateinit var viewModel: InfoTodoViewModel
    private lateinit var infoListAdapter: InfoListAdapter
    private lateinit var binding: FragmentInfoListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.run{
            this@InfoListFragment.viewModel = ViewModelProvider(
                this,
                InfoTodoViewModel.InfoTodoViewModelFactory(
                    DatabaseRepository(MainApplication.database.DatabaseDao())
                )
            ).get(InfoTodoViewModel::class.java)
        }

        // deleteFlgを初期化する
        viewModel.initDeleteModeFlg()

        // 戻るボタンは非表示
        getMainActivity()?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return FragmentInfoListBinding.inflate(inflater, container, false).apply {
            this.viewModel = this@InfoListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            infoRecyclerView.run {
                layoutManager = LinearLayoutManager(context)
            }

            val infoListAdapter = InfoListAdapter(viewLifecycleOwner, this@InfoListFragment.viewModel, this@InfoListFragment)
            infoRecyclerView.adapter = infoListAdapter
            this@InfoListFragment.infoListAdapter = infoListAdapter
        }.run {
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.infoTodoList.observe(viewLifecycleOwner) {
            infoListAdapter.submitList(it)
        }

        // スナックバーでのメッセージ
        viewModel.snackBarMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { event ->
                if(!event.isNullOrEmpty()) {
                    Snackbar.make(view, event, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    viewModel.initSnackBarMessage()
                }
            }
        })

        // 遷移イベント
        viewModel.navigateTrans.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { event ->
                findNavController().navigate(R.id.action_infoListFragment_to_detailFragment)
            }
        })

        // fabイベント
        binding.infoActionFab.setOnClickListener {
            if(viewModel.isDeleteMode()){
                if(viewModel.onDeleteClick()){
                    viewModel.initDeleteModeFlg()
                    onAnimationInfoActionFab(false)
                }
            } else {
                viewModel.onClickTrans(TransDestination.NEW)
            }
        }
    }

    fun onAnimationInfoActionFab(isDeleteMode: Boolean) {
        val fab = binding.infoActionFab
        if(isDeleteMode){
            fab.animate()
                .rotationBy(180F)   // ローテ―ション
                .setDuration(200)   // フレームを表示する時間
                .scaleX(1.3f)   //  指定したサイズにセット
                .scaleY(1.3f)   //  指定したサイズにセット
                .withEndAction{
                    fab.setImageResource(R.drawable.ic_delete)
                    fab.animate()
                        .rotationBy(180F)   // ローテ―ション
                        .setDuration(200)   // フレームを表示する時間
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
                    fab.setImageResource(R.drawable.ic_create)
                    fab.animate()
                        .rotationBy(180F)   // ローテ―ション
                        .setDuration(300)   // フレームを表示する時間
                        .scaleX(1.0f)   //  指定したサイズにセット
                        .scaleY(1.0f)   //  指定したサイズにセット
            }
        }
    }
}