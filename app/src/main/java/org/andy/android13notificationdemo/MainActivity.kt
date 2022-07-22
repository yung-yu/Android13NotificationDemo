package org.andy.android13notificationdemo

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import androidx.core.content.ContextCompat
import org.andy.android13notificationdemo.ui.theme.Android13NotificationDemoTheme


class MainActivity : ComponentActivity() {

	val permissionLaunch = registerForActivityResult(ActivityResultContracts.RequestPermission()){
		vm.checkPermission(this)
	}
	private val vm:MainViewModel by viewModels<MainViewModel>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		createNotificationChannel()
		setContent {
			Android13NotificationDemoTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					Demo(this, vm)
				}
			}
		}
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			startForegroundService(Intent(this, DemoService::class.java))
//		}
	}

	override fun onStart() {
		super.onStart()
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
fun Demo(activity: MainActivity, mainViewModel: MainViewModel) {
	val context = LocalContext.current
	Column(
		modifier = Modifier
			.wrapContentWidth()
			.wrapContentHeight()
	) {

		val isGanted:Boolean by mainViewModel.permission.collectAsState()
		Text(text = "Notification Permission: $isGanted")
		if (isGanted) {
			Button(
				modifier = Modifier
					.wrapContentWidth()
					.wrapContentHeight(),
				onClick = {
					val notificationManagerCompat = NotificationManagerCompat.from(context)
					if (notificationManagerCompat.areNotificationsEnabled()) {
						sendNotification(context)
					}

				}) {
				Text(text = "send!")
			}
		} else  {
			Text(text = "提示：\n顯示通知需要使用通知權限")
			Button(
				modifier = Modifier
					.wrapContentWidth()
					.wrapContentHeight(),
				onClick = {
					if(activity.shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){
						android.app.AlertDialog.Builder(activity)
							.setMessage("要發起通知需要通知權限")
							.setPositiveButton("ok"){_,_ ->
								activity.permissionLaunch.launch(android.Manifest.permission.POST_NOTIFICATIONS)
							}.create().show()
						return@Button
					}
					activity.permissionLaunch.launch(android.Manifest.permission.POST_NOTIFICATIONS)
				}) {
				Text(text = "取得權限")
			}
		}

	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	Android13NotificationDemoTheme {
		//Demo()
	}
}
