package org.andy.android13notificationdemo

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.andy.android13notificationdemo.ui.theme.Android13NotificationDemoTheme


class MainActivity : ComponentActivity() {

	private val permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()){
		if(it){
			createNotificationChannel()
		}
		vm.checkPermission(this)
	}
	private val vm:MainViewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		vm.checkPermission(this)
		setContent {
			Android13NotificationDemoTheme {
				val showDialogState: Triple<String, Boolean, Runnable> by vm.showDialog.collectAsState()
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					Demo(vm)
					AlertDialog(show = showDialogState.second,
						text = showDialogState.first,
						onDismiss = { vm.onDismissClick() },
						onConfirm = { vm.onConfirmClick() },
						runnable = showDialogState.third)
				}
			}
		}
		if(Build.VERSION.SDK_INT >= 33) {
			if(checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
				if(shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){
					vm.showDialog("提示通知權限權限才能發通知", Runnable {
						permissionLaunch.launch(android.Manifest.permission.POST_NOTIFICATIONS)
					})
				} else{
					permissionLaunch.launch(android.Manifest.permission.POST_NOTIFICATIONS)
				}
			} else {
				createNotificationChannel()
			}
		} else {
			createNotificationChannel()
		}
		vm.checkPermission(this)
	}

	private fun createNotificationChannel() {
		val notificationManagerCompat = NotificationManagerCompat.from(this)
		val channel =
			NotificationChannelCompat.Builder("test", NotificationCompat.PRIORITY_HIGH)
				.setName("demo notification")
				.build()

		notificationManagerCompat.createNotificationChannel(channel)
	}

}
@Composable
fun AlertDialog(show:Boolean,text: String,
						  onDismiss: () -> Unit,
						  onConfirm: () -> Unit,
							runnable: Runnable) {
	if(show) {
		val context = LocalContext.current
		AlertDialog(
			onDismissRequest = {},
			confirmButton = {
				TextButton(onClick = {
					runnable.run()
					onConfirm.invoke()
				}) {
					Text("ok")
				}
			},
			dismissButton = {
				TextButton(onClick = {
					onDismiss.invoke()
				}) {
					Text("cancel")
				}
			},
			title = { Text(text = context.getString(R.string.app_name)) },
			text = { Text(text = text) })
	}
}

fun sendNotification(context: Context){
	val notificationManagerCompat = NotificationManagerCompat.from(context)
	val pendingIntent = PendingIntent.getActivity(context, 0,
		Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE);

	notificationManagerCompat.notify(1,NotificationCompat.Builder(context, "test")
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.setDefaults(NotificationCompat.DEFAULT_ALL)
		.setSmallIcon(R.drawable.ic_stat_name)
		.setContentTitle("test1")
		.setContentText("大家好！！")
		.setFullScreenIntent(pendingIntent, true)
		.build())
}
@Composable
fun Demo(vm: MainViewModel? = null) {
	val context = LocalContext.current
	Column(modifier = Modifier
		.wrapContentWidth()
		.wrapContentHeight()){
		vm?.let {
			val enable:Pair<Boolean, Boolean>  by vm.enableNotification.collectAsState()
			Text(text = "Notification status: ${enable.first} ${enable.second}")
		}

	Button(
		modifier = Modifier
			.wrapContentWidth()
			.wrapContentHeight(),
		onClick = {
			val notificationManagerCompat = NotificationManagerCompat.from(context)
			if(notificationManagerCompat.areNotificationsEnabled()){
				sendNotification(context)
			}

		}) {
			Text(text = "send!")
		}
		Spacer(modifier = Modifier.width(16.dp))
		Button(
			modifier = Modifier
				.wrapContentWidth()
				.wrapContentHeight(),
			onClick = {
				sendNotification(context)
			}) {
			Text(text = "no permission check sned!")
		}

	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	Android13NotificationDemoTheme {
		Demo()
	}
}
