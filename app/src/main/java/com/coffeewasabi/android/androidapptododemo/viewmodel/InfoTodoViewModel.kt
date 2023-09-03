package com.coffeewasabi.android.androidapptododemo.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.coffeewasabi.android.androidapptododemo.database.DatabaseRepository
import com.coffeewasabi.android.androidapptododemo.entity.InfoEntity
import com.coffeewasabi.android.androidapptododemo.entity.InfoTodoEntity
import com.coffeewasabi.android.androidapptododemo.entity.TodoEntity
import com.coffeewasabi.android.androidapptododemo.utils.Event
import com.coffeewasabi.android.androidapptododemo.utils.TransDestination
import kotlinx.coroutines.launch
import java.util.*

class InfoTodoViewModel(private val repository: DatabaseRepository) : ViewModel() {

    private val ERROR_INSERT_FAIL = -1L

    var infoTodoList = repository.infoTodo

    private val _navigateTrans = MutableLiveData<Event<String>>()
    val navigateTrans : LiveData<Event<String>>
    get() = _navigateTrans

    private val _snackBarMessage = MutableLiveData<Event<String>>()
    val snackBarMessage : LiveData<Event<String>>
    get() = _snackBarMessage

    var deleteModeFlg: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _editInfo = MutableLiveData<InfoEntity>()
    val editInfo: LiveData<InfoEntity> = _editInfo.distinctUntilChanged()

    private val _editTodoList = MutableLiveData<List<TodoEntity>>()
    val editTodoList: LiveData<List<TodoEntity>> = _editTodoList.distinctUntilChanged()

    var editModeFlg: MutableLiveData<Boolean?> = MutableLiveData(false)

    private fun initEditData(){
        _editInfo.postValue(InfoEntity())
        _editTodoList.postValue(emptyList())
    }

    private fun setEditData(item: InfoTodoEntity) {
        item.run {
            item.todoList = sortEditTodoList(ArrayList(item.todoList))
            _editInfo.postValue(item.info)
            _editTodoList.postValue(item.todoList)
        }
    }

    fun onCheckedByView(position: Int, isChecked: Boolean){
        var list = ArrayList(editTodoList.value?.toMutableList())
        list.get(position).checkFlg.value = isChecked
        list = sortEditTodoList(list)
        _editTodoList.postValue(ArrayList(list))

        // チェックを全ていれた場合
        if(list.all { it.checkFlg.value == true }){
            setSnackBarMessage("All Todos completed!")
        }
    }

    fun onDeleteClick(): Boolean {
        infoTodoList.value?.let {
            val deleteList = it.filter { it -> it.info.deleteFlg }
            if(!deleteList.isNullOrEmpty()){
                delete(deleteList)
                return true
            }
            else{
                // このパターンは無いはず
                setSnackBarMessage("Please select task.")
            }
        }
        return false
    }

    fun getTodoByEdit(position: Int): TodoEntity?{
        return editTodoList.value?.get(position)
    }

    fun addTodoByEdit(item: TodoEntity){
        var list = ArrayList(editTodoList.value?.toMutableList())
        list = addEditTodoList(list, item)
        list = sortEditTodoList(list)
        _editTodoList.postValue(ArrayList(list))
    }

    fun removeTodoByEdit(item: TodoEntity){
        var list = ArrayList(editTodoList.value?.toMutableList())
        list = removeEditTodoList(list, item)
        list = sortEditTodoList(list)
        _editTodoList.postValue(ArrayList(list))
    }

    private fun addEditTodoList(list: ArrayList<TodoEntity>, item: TodoEntity): ArrayList<TodoEntity>{
        list.add(item)
        return list
    }

    private fun removeEditTodoList(list: ArrayList<TodoEntity>, item: TodoEntity): ArrayList<TodoEntity>{
        list.remove(item)
        return list
    }

    private fun sortEditTodoList(list: ArrayList<TodoEntity>): ArrayList<TodoEntity>{
        list.sortWith(compareBy({it.checkFlg.value}, {it.dataType}, {it.sortNo}))
        return list;
    }

    private fun insert(info: InfoEntity, todoList: List<TodoEntity>) = viewModelScope.launch {
        val infoId = repository.insert(info, todoList)
        if(infoId === ERROR_INSERT_FAIL){
            Log.e("ViewModel", "Insert Fail")
        }
        else {
            // idをセット
            info.id = infoId
            todoList.map { it -> it.infoId = infoId }

            // update mutable data
            _editInfo.postValue(info)
            _editTodoList.postValue(todoList)

            setSnackBarMessage("["+info.infoName+"] is success insert.")
        }
    }

    private fun update(info: InfoEntity, todoList: List<TodoEntity>) = viewModelScope.launch {
        if(repository.update(info, todoList)){
            _editInfo.postValue(info)
            setSnackBarMessage("["+info.infoName+"] is success update.")
        }
        else{
            Log.e("ViewModel", "Update Fail")
        }
    }

    private fun delete(list: List<InfoTodoEntity>) = viewModelScope.launch {
        if(repository.deleteInfoTodoList(list)){
            setSnackBarMessage("delete "+list.size+" task.")
        }else{
            Log.e("ViewModel", "Delete Fail")
        }
    }

    fun onClickTrans(transValue: TransDestination){
        if(transValue == TransDestination.NEW){
            initEditData()
            editModeFlgEnable()
        }
        onTrans(transValue)
    }

    fun onClickTransWithItem(transValue: TransDestination, item: InfoTodoEntity){
        setEditData(item!!)
        onClickTrans(transValue)
    }

    fun onRegistUpdate(){
        _editInfo.value?.let {
            if(validate(it.infoName)){
                return
            }

            if(it.id == 0L){
                val date = Date()
                val infoTodo = InfoEntity(0L, it.infoName, date, date)
                insert(infoTodo, editTodoList.value!!)
            } else {
                val updInfoTodo = InfoEntity(it.id, it.infoName, it.insDate, Date())
                update(updInfoTodo, editTodoList.value!!)
            }
        } ?: run {
            Log.e("ViewModel", "onClickRegist No Edit Data item["+_editInfo+"]")
        }
    }

    private fun setSnackBarMessage(message: String){
        _snackBarMessage.value = Event(message)
    }

    fun initSnackBarMessage(){
        _snackBarMessage.value = Event("")
    }

    private fun onTrans(transValue: TransDestination){
        // 遷移先ごとに追加
        if(transValue == TransDestination.VIEW){
            _navigateTrans.value = Event("view");
        } else if(transValue == TransDestination.NEW) {
            _navigateTrans.value = Event("info");
        } else {
            Log.e("onTrans", "Unexpected parameter. trans["+transValue+"]")
        }
    }

    fun initDeleteModeFlg(){
        if(deleteModeFlg.value != false){
            deleteModeFlg.postValue(false)
        }
    }

    fun updateDeleteMode(checked: Boolean, position: Int): Boolean{
        infoTodoList.value?.let {
            it[position].info.deleteFlg = checked
        }
        // Check Delete Mode
        return if(isDeleteMode()){
            deleteModeFlg.postValue(true)
            true
        } else {
            deleteModeFlg.postValue(false)
            false
        }
    }

    fun isDeleteMode(): Boolean{
        infoTodoList.value?.let {
            return it.filter { item -> item.info.deleteFlg }!!.any()
        }
        return false
    }

    fun editModeFlgEnable(){
        editModeFlg.postValue(true)
    }

    fun editModeFlgDisable(){
        editModeFlg.postValue(false)
    }

    fun isEditModeFlg(): Boolean{
        return editModeFlg.value as Boolean
    }

    private fun validate(infoName: String): Boolean{
        if(infoName.isNullOrEmpty()){
            setSnackBarMessage("Please input title.")
            return true
        } else if(editTodoList.value.isNullOrEmpty()){
            setSnackBarMessage("Please register at least one todo.")
            return true
        }
        return false
    }

    class InfoTodoViewModelFactory(
        private val repository: DatabaseRepository
    ) :
    ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T{
            return if(modelClass != InfoTodoViewModel::class.java){
                super.create(modelClass)
            } else{
                InfoTodoViewModel(repository) as T
            }
        }
    }
}