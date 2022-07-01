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

	private var _tipPermission = MutableStateFlow<Boolean>(false)
	val tipPermission =_tipPermission.asStateFlow()

	fun checkPermission(activity: MainActivity){
		val isTip = if (Build.VERSION.SDK_INT >= 33) {
			activity.shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)
		} else {
			false
		}
		_tipPermission.value = isTip
		val isGranted = if (Build.VERSION.SDK_INT >= 33) {
			ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
		} else {
			true
		}
		_permission.value = isGranted

	}
}