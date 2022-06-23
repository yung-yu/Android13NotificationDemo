package org.andy.android13notificationdemo

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
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

	private val _enableNotification = MutableStateFlow(Pair(false, false))
	val enableNotification = _enableNotification.asStateFlow()

	fun checkPermission(context: Context){
		val status1  =  NotificationManagerCompat.from(context).areNotificationsEnabled()
		val status2  = ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) ==
				PackageManager.PERMISSION_GRANTED
		_enableNotification.value = Pair(status1, status2)

	}

}