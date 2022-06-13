package org.andy.android13notificationdemo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel:ViewModel() {
	private val _showDialog = MutableStateFlow(Pair("", false))
	val showDialog: StateFlow<Pair<String, Boolean>> = _showDialog.asStateFlow()

	fun showDialog(text:String){
		_showDialog.value = Pair(text, true)
	}

	fun onConfirmClick(){
		_showDialog.value = Pair("", false)
	}
	fun onDismissClick(){
		_showDialog.value = Pair("", false)
	}
}