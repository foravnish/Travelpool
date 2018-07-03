package travelpool.app.travelpool.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

public class PayNow extends AppCompatActivity {

    TextView instal,name,packageName;
    Button payNow;
    Dialog dialog;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Log.d("dfgdgdgdf",getIntent().getStringExtra("data"));

        instal=findViewById(R.id.instal);
        name=findViewById(R.id.name);
        packageName=findViewById(R.id.packageName);
        payNow=findViewById(R.id.payNow);

        dialog=new Dialog(PayNow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        try {
            jsonObject=new JSONObject(getIntent().getStringExtra("data"));

            name.setText(jsonObject.optString("name"));
            packageName.setText(jsonObject.optString("package_name"));

            instal.setText("Per Month ₹ : "+jsonObject.optString("per_month_installment"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submitData(jsonObject.optString("id"));

            }
        });
    }

    private void submitData(final String id) {

        Util.showPgDialog(dialog);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.Login,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Util.cancelPgDialog(dialog);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")){

                                JSONArray jsonArray=jsonObject.getJSONArray("login");
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(PayNow.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("userid", MyPrefrences.getUserID(getApplicationContext()));
                params.put("id", id.toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);

    }
}
