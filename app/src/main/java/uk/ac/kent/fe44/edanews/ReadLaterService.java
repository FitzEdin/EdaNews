package uk.ac.kent.fe44.edanews;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import uk.ac.kent.fe44.edanews.controller.ArticleListActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ReadLaterService extends IntentService {

    private static final String SAVED_COUNT = "saved_count";
    private static final int delay = 24*60*60*1000;

    public ReadLaterService() {
        super("ReadLaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final int count = intent.getIntExtra(SAVED_COUNT, 1);
            //set timer for 24 hours
            new CountDownTimer(10000, 1000) {
                public void onTick(long millisUntilFinished) {  /*do nothing*/  }
                public void onFinish() {
                    buildNotification(count);
                    Looper.myLooper().quit();
                }
            }.start();
            Looper.loop();
        }
    }

    public void buildNotification(int count) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_papaya)
                        .setContentTitle("EDA News Reader")
                        //assume 1 article
                        .setContentText("There is " + count + " article waiting for you to read.")
                        .setAutoCancel(true);

        if (count > 1) {
            mBuilder.setContentText("There are " + count + " articles waiting for you to read.");
        }
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ArticleListActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ArticleListActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
