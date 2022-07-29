package org.andy.android13notificationdemo

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel:ViewModel() {
	private var _permission = MutableStateFlow<Boolean>(false)
	val permission =_permission.asStateFlow()



	fun checkPermission(activity: MainActivity){
		val isGranted = if (Build.VERSION.SDK_INT >= 33) {
			ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
		} else {
			true
		}
		_permission.value = isGranted

	}
}