package travelpool.app.travelpool.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import travelpool.app.travelpool.R;

public class PayNow extends AppCompatActivity {

    TextView instal,name,packageName;
    Button payNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Log.d("dfgdgdgdf",getIntent().getStringExtra("data"));

        instal=findViewById(R.id.instal);
        name=findViewById(R.id.name);
        packageName=findViewById(R.id.packageName);
        payNow=findViewById(R.id.payNow);
        try {
            JSONObject jsonObject=new JSONObject(getIntent().getStringExtra("data"));

            name.setText(jsonObject.optString("name"));
            packageName.setText(jsonObject.optString("package_name"));

            instal.setText("Per Month ₹ : "+jsonObject.optString("per_month_installment"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
