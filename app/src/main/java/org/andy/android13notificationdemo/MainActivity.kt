package org.andy.android13notificationdemo

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
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
			sendNotification(this)
		} else {
			vm.showDialog("請開啟通知權限")
		}
		vm.checkPermission(this)
	}
	private val vm:MainViewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		vm.checkPermission(this)
		setContent {
			Android13NotificationDemoTheme {
				val showDialogState: Pair<String, Boolean> by vm.showDialog.collectAsState()
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					Demo( vm, permissionLaunch)

					PermissionAlertDialog(show = showDialogState.second,
						text = showDialogState.first,
						onDismiss = { vm.onDismissClick() },
						onConfirm = { vm.onConfirmClick() })
				}
			}
		}
	}
}
@Composable
fun PermissionAlertDialog(show:Boolean,text: String,
						  onDismiss: () -> Unit,
						  onConfirm: () -> Unit) {
	if(show) {
		val context = LocalContext.current
		AlertDialog(
			onDismissRequest = {},
			confirmButton = {
				TextButton(onClick = {
					val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					val uri: Uri = Uri.fromParts("package", context.packageName, null)
					intent.data = uri
					context.startActivity(intent)
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
	val channel  = NotificationChannelCompat.Builder("test", NotificationCompat.PRIORITY_HIGH)
		.setName("demo notification")
		.build()

	notificationManagerCompat.createNotificationChannel(channel)
	val channel2  = NotificationChannelCompat.Builder("test2", NotificationCompat.PRIORITY_HIGH)
		.setName("demo notification 2")
		.build()
	notificationManagerCompat.createNotificationChannel(channel2)
	val channel3  = NotificationChannelCompat.Builder("test3", NotificationCompat.PRIORITY_HIGH)
		.setName("demo notification 3")
		.build()
	notificationManagerCompat.createNotificationChannel(channel3)
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
	notificationManagerCompat.notify(2,NotificationCompat.Builder(context, "test2")
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.setDefaults(NotificationCompat.DEFAULT_ALL)
		.setSmallIcon(R.drawable.ic_stat_name)
		.setContentTitle("test2")
		.setContentText("大家好！！")
		.setFullScreenIntent(pendingIntent, true)
		.build())
	notificationManagerCompat.notify(3,NotificationCompat.Builder(context, "test3")
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.setDefaults(NotificationCompat.DEFAULT_ALL)
		.setSmallIcon(R.drawable.ic_stat_name)
		.setContentTitle("test3")
		.setContentText("大家好！！")
		.setFullScreenIntent(pendingIntent, true)
		.build())
}
@Composable
fun Demo(vm: MainViewModel? = null,  permissionLaunch: ActivityResultLauncher<String>? = null) {
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
			if (Build.VERSION.SDK_INT >= 33) {
				permissionLaunch?.launch(android.Manifest.permission.POST_NOTIFICATIONS)
			} else {
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
