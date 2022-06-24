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
	private val _showDialog = MutableStateFlow(Triple("", false, Runnable { }))
	val showDialog: StateFlow<Triple<String, Boolean, Runnable>> = _showDialog.asStateFlow()

	fun showDialog(text:String, doEvent:Runnable){
		_showDialog.value = Triple(text, true, doEvent)
	}

	fun onConfirmClick(){
		_showDialog.value = Triple("", false, Runnable {  })
	}
	fun onDismissClick(){
		_showDialog.value = Triple("", false, Runnable {  })
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