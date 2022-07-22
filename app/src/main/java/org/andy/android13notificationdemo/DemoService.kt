package org.andy.android13notificationdemo

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class DemoService: Service() {
	private var timer:Timer? = null
	override fun onCreate() {
		super.onCreate()

	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show()
		val pendingIntent = PendingIntent.getActivity(this, 0,
			Intent(this, MainActivity::class.java), PendingIntent.FLAG_MUTABLE);
		val notification = NotificationCompat.Builder(this, "test")
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setDefaults(NotificationCompat.DEFAULT_ALL)
			.setSmallIcon(R.drawable.ic_stat_name)
			.setContentTitle("前景服務")
			.setContentText("現在時間 ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}")
			.setFullScreenIntent(pendingIntent, true)
			.build()
		startForeground(222, notification)
		timer?.cancel()
		timer = Timer()
		timer?.schedule(object:TimerTask(){
			override fun run() {
				Log.d("test" , "${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}")
				val pendingIntent = PendingIntent.getActivity(this@DemoService, 0,
					Intent(this@DemoService, MainActivity::class.java), PendingIntent.FLAG_MUTABLE);
				val notification = NotificationCompat.Builder(this@DemoService, "test")
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setDefaults(NotificationCompat.DEFAULT_ALL)
					.setSmallIcon(R.drawable.ic_stat_name)
					.setContentTitle("前景服務")
					.setContentText("現在時間 ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}")
					.setFullScreenIntent(pendingIntent, true)
					.build()
				NotificationManagerCompat.from(this@DemoService).notify(222, notification)
				Log.d(TAG, "現在時間 ${SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(Date())}")
			}
		},0,1000)
		return super.onStartCommand(intent, flags, startId)

	}

	override fun onDestroy() {
		super.onDestroy()
		timer?.cancel()
	}

	override fun onBind(p0: Intent?): IBinder? {
		return null
	}
	private companion object{
		private const val TAG = "DemoService"
	}
}