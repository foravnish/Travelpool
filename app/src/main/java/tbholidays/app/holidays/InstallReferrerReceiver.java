package tbholidays.app.holidays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tbholidays.app.holidays.Utils.MyPrefrences;

public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ghfghfgfytyfgv","true");
        String referrer = intent.getStringExtra("referrer");

        Log.d("dfsdfsdfsdfsdfs",referrer);

        MyPrefrences.setRefer(context,referrer);
        //Use the referrer
    }

}