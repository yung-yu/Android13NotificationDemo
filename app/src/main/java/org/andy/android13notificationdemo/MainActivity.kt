package org.andy.android13notificationdemo

import android.app.PendingIntent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import org.andy.android13notificationdemo.ui.theme.Android13NotificationDemoTheme


class MainActivity : ComponentActivity() {

	private val permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()){
		if(it){
			val channed  = NotificationChannelCompat.Builder("test", NotificationCompat.PRIORITY_HIGH)
				.setName("demo notification")
				.build()
			val notificationManagerCompat = NotificationManagerCompat.from(this)
			notificationManagerCompat.createNotificationChannel(channed)
			val pendingIntent = PendingIntent.getActivity(this, 0,
				Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE);

			val sendNotificationCompat = NotificationCompat.Builder(this, "test")
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setDefaults(NotificationCompat.DEFAULT_ALL)
				.setSmallIcon(R.drawable.ic_stat_name)
				.setContentTitle("hello world")
				.setContentText("大家好！！")
				.setFullScreenIntent(pendingIntent, true)
				.build()
			notificationManagerCompat.notify(1,sendNotificationCompat)
		} else {
			vm.showDialog("請開啟通知權限")
		}
	}
	private val vm:MainViewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {

			Android13NotificationDemoTheme {
				// A surface container using the 'background' color from the theme
				val showDialogState: Pair<String, Boolean> by vm.showDialog.collectAsState()
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					Demo("Android", permissionLaunch)
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

@Composable
fun Demo(name: String, permissionLaunch: ActivityResultLauncher<String>? = null) {
	val context = LocalContext.current
	val activity = LocalActivityResultRegistryOwner.current
	Button(
		modifier = Modifier
			.wrapContentWidth()
			.wrapContentHeight(),
		onClick = {
			if (Build.VERSION.SDK_INT >= 33) {
				permissionLaunch?.launch(android.Manifest.permission.POST_NOTIFICATIONS)

			} else {
				val channed  = NotificationChannelCompat.Builder("test", NotificationCompat.PRIORITY_HIGH)
					.setName("demo notification")
					.build()
				val notificationManagerCompat = NotificationManagerCompat.from(context)
				notificationManagerCompat.createNotificationChannel(channed)
				val pendingIntent = PendingIntent.getActivity(context, 0,
				Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE);

				val sendNotificationCompat = NotificationCompat.Builder(context, "test")
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setDefaults(NotificationCompat.DEFAULT_ALL)
					.setSmallIcon(R.drawable.ic_stat_name)
					.setContentTitle("hello world")
					.setContentText("大家好！！")
					.setFullScreenIntent(pendingIntent, true)
					.build()
				notificationManagerCompat.notify(1,sendNotificationCompat)
			}
		}) {
		Text(text = "send Notification!")
	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	Android13NotificationDemoTheme {
		Demo("Android")
	}
}