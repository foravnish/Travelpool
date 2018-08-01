package travelpool.app.travelpool.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.MyPrefrences;

public class SplashAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            public void run() {
                try {

                    sleep(3*1000);

//                    Intent intent = new Intent(Splash.this, Login.class);
//                    startActivity(intent);
//                    finish();

                    if (MyPrefrences.getUserLogin(SplashAct.this)==true){
                        Intent intent=new Intent(SplashAct.this,HomeAct.class);
                        intent.putExtra("userType", MyPrefrences.getUserType(getApplicationContext()));
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashAct.this, Login.class);
                        startActivity(intent);
                        finish();
                    }



                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}
