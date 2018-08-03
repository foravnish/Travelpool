package travelpool.app.travelpool.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;


public class Registration extends AppCompatActivity {

    Button btnSubReg;
    EditText editTextname,editmobile,editPassword,editPasswordCon,editEmail,editAddress,editCity,editState,editPincode,editapasport,editpanNo,aadharNo;
    TextView editTextdob,skipNow;
    //Spinner editLocation;
    Dialog dialog;
    List<String> location = new ArrayList<String>();
    List<HashMap<String, String>> DataLoc;
    ArrayAdapter aa,subcat;
    DatePickerDialog  datePickerDialog;
    TextView loginNow;
    Spinner gender;
    String[] cat ={"Male","Female"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSubReg=findViewById(R.id.btnSubReg);

       // editLocation=findViewById(R.id.editLocation);
        editPassword=findViewById(R.id.editPassword);
        editPasswordCon=findViewById(R.id.editPasswordCon);
        editmobile=findViewById(R.id.editmobile);
        editTextname=findViewById(R.id.editTextname);

        editTextdob=findViewById(R.id.editTextdob);
        loginNow=findViewById(R.id.loginNow);
        editEmail=findViewById(R.id.editEmail);
        editAddress=findViewById(R.id.editAddress);
        editCity=findViewById(R.id.editCity);
        editState=findViewById(R.id.editState);
        editPincode=findViewById(R.id.editPincode);

        aadharNo=findViewById(R.id.aadharNo);
        editpanNo=findViewById(R.id.editpanNo);
        editapasport=findViewById(R.id.editapasport);

        dialog=new Dialog(Registration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        DataLoc=new ArrayList<>();



        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        editTextdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Calendar c = Calendar.getInstance();

                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                editTextdob.setText(i2 + "/" + (i1 + 1)+ "/" + i);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });
        btnSubReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                smsAPI();


                if(validate()){

//                    otpAPi(editmobile.getText().toString());

                    submitRegistration();

                }
            }
        });


//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item2, cat);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        gender.setAdapter(dataAdapter);









//

    }

//    private void otpAPi(final String mob) {
//
//        Util.showPgDialog(dialog);
//        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
//                Api.buyerRegistrationOtp+"?mobileNumber="+mob, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("ResposeBaner", response.toString());
//                Util.cancelPgDialog(dialog);
//                try {
//                    // Parsing json object response
//                    // response will be a json object
////                    String name = response.getString("name");
//                    HashMap<String,String> hashMap = null;
//                    if (response.getString("status").equalsIgnoreCase("success")){
//
//                        JSONObject jsonObject=response.getJSONObject("message");
//
//                        String  otp=jsonObject.optString("otp");
//
//                        otpVerfy(mob.toString(),otp);
//
//                    }
//                    else if (response.getString("status").equalsIgnoreCase("failure")){
//                        //Toast.makeText(Registration.this, ""+response.optString("message"), Toast.LENGTH_SHORT).show();
//                        Util.errorDialog(Registration.this,response.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                    Util.cancelPgDialog(dialog);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Respose", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                Util.cancelPgDialog(dialog);
//
//            }
//        });
//
//        // Adding request to request queue
//        jsonObjReq2.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq2);
//
//    }

//    private void otpVerfy(final String mob, final String otp) {
//
//        final Dialog dialog2 = new Dialog(Registration.this);
////                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
////                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog2.setContentView(R.layout.otp_dialog_verfy);
//       // dialog2.setCancelable(false);
//
//        final EditText otp_edit= (EditText) dialog2.findViewById(R.id.otp_edit);
//        TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
//        TextView resend= (TextView) dialog2.findViewById(R.id.resend);
//        recieve.setText("Sent OTP on "+mob);
//        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
//        resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//                Util.showPgDialog(dialog);
//                JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
//                        Api.buyerRegistrationOtp+"?mobileNumber="+mob, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("ResposeBaner", response.toString());
//                        Util.cancelPgDialog(dialog);
//                        try {
//                            // Parsing json object response
//                            // response will be a json object
////                    String name = response.getString("name");
//                            HashMap<String,String> hashMap = null;
//                            if (response.getString("status").equalsIgnoreCase("success")){
//
//                                dialog2.dismiss();
//                                JSONObject jsonObject=response.getJSONObject("message");
//
//                                String  otp=jsonObject.optString("otp");
//
//                                otpVerfy(mob.toString(),otp);
//
//                            }
//                            else if (response.getString("status").equalsIgnoreCase("failure")){
//                                //Toast.makeText(Registration.this, ""+response.optString("message"), Toast.LENGTH_SHORT).show();
//                                Util.errorDialog(Registration.this,response.optString("message"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                            Util.cancelPgDialog(dialog);
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d("Respose", "Error: " + error.getMessage());
//                        Toast.makeText(getApplicationContext(),
//                                "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
//                        // hide the progress dialog
//                        Util.cancelPgDialog(dialog);
//
//                    }
//                });
//
//                // Adding request to request queue
//                jsonObjReq2.setShouldCache(false);
//                AppController.getInstance().addToRequestQueue(jsonObjReq2);
//
//
//            }
//        });
//        submit2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (TextUtils.isEmpty(otp_edit.getText().toString())){
//
//                    Toast.makeText(Registration.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//                    if (otp.equalsIgnoreCase(otp_edit.getText().toString())){
////                        newpassword(mob);
//                        submitRegistration();
//                        dialog2.dismiss();
//                    }
//                    else {
//                        Util.errorDialog(Registration.this,"Enter Correct OTP");
//                    }
//
//                }
//            }
//        });
//
//        dialog2.show();
//
//    }

    private void submitRegistration() {

        Util.showPgDialog(dialog);
        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfsjfdfsdfgd", "Login Response: " + response);
                //parse your response here


                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.optString("status").equals("success")){

                        Log.d("fsdfsdfsdfs","true");

//                        Toast.makeText(getApplicationContext(), "Registration Successfully...", Toast.LENGTH_SHORT).show();
                        Util.errorDialog(Registration.this,"Registration Successfully...");

                        smsAPI();

                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Please Login...", Toast.LENGTH_SHORT).show();

                    }
                    else if (jsonObject.optString("status").equals("failure")){
                        Util.errorDialog(Registration.this,jsonObject.getString("message"));
                        Log.d("fsdfsdfsdfs","false");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("name", editTextname.getText().toString());
                params.put("email",editEmail.getText().toString());
                params.put("mobile",editmobile.getText().toString());
                params.put("password",editPassword.getText().toString());
                params.put("address",editAddress.getText().toString());
                params.put("city",editCity.getText().toString());
                params.put("state",editState.getText().toString());
                params.put("pincode",editPincode.getText().toString());

                params.put("aadhar_no",aadharNo.getText().toString());
                params.put("pan_no",editpanNo.getText().toString());
                params.put("agent_id","No");
               // params.put("status","1");
                return params;
            }

//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            Log.e("fdgdfgdfgdfg","Inside getHeaders()");
//                            Map<String,String> headers=new HashMap<>();
//                            headers.put("Content-Type","application/x-www-form-urlencoded");
//                            return headers;
//                        }
        };
        // Adding request to request queue
        queue.add(strReq);





//        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.signup,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("ResponseRegis", response);
//                        Util.cancelPgDialog(dialog);
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            if (jsonObject.getString("status").equalsIgnoreCase("1")){
//
//                                Toast.makeText(getApplicationContext(),"Registration Successfully..." , Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(Registration.this, Login.class);
//                                startActivity(intent);
//                                finish();
//                                Toast.makeText(getApplicationContext(),"Please Login..." , Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Util.errorDialog(Registration.this,jsonObject.getString("message"));
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Toast.makeText(Registration.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
//                        Util.cancelPgDialog(dialog);
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
////
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("name", editTextname.getText().toString());
//                params.put("email",editEmail.getText().toString());
//                params.put("mobile",editmobile.getText().toString());
//                params.put("password",editPassword.getText().toString());
//                params.put("address",editAddress.getText().toString());
//                params.put("city",editCity.getText().toString());
//                params.put("state",editState.getText().toString());
//                params.put("pincode",editPincode.getText().toString());
//
//                params.put("aadhar_no",aadharNo.getText().toString());
//                params.put("pan_no",editpanNo.getText().toString());
//                params.put("status","1");
////                params.put("passportNo",editapasport.getText().toString());
//
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        postRequest.setShouldCache(false);
//
//        AppController.getInstance().addToRequestQueue(postRequest);



    }

    private void smsAPI() {


//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=TEST%20MSG", null, new Response.Listener<JSONObject>() {
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=Congratulation!%20You%20have%20Successfully%20Registered%20with%20TRAVEL%20POOL,%20Please%20Login%20to%20access", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Log.d("asdfsafsdfsdf",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request);


    }

    private boolean validate(){



        if (TextUtils.isEmpty(editTextname.getText().toString()))
        {
            editTextname.setError("Oops! Buyer Name blank");
            editTextname.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(editmobile.getText().toString()))
        {
            editmobile.setError("Oops! Mobile blank");
            editmobile.requestFocus();
            return false;
        }

        else if (editmobile.getText().toString().length()!=10){
            editmobile.setError("Oops! Enter Valid Mobile No.");
            editmobile.requestFocus();
            return false;
        }

        else if (TextUtils.isEmpty(editPassword.getText().toString()))
        {
            editPassword.setError("Oops! Password blank");
            editPassword.requestFocus();
            return false;
        }

        else if (!editPassword.getText().toString().equals(editPasswordCon.getText().toString())){
            Toast.makeText(getApplicationContext(), "Both Password should be match", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (TextUtils.isEmpty(editAddress.getText().toString()))
        {
            editAddress.setError("Oops! Address blank");
            editAddress.requestFocus();
            return false;
        }

        else if (TextUtils.isEmpty(editCity.getText().toString()))
        {
            editCity.setError("Oops! City blank");
            editCity.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(editState.getText().toString()))
        {
            editState.setError("Oops! State blank");
            editState.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(editPincode.getText().toString()))
        {
            editPincode.setError("Oops! Pincode blank");
            editPincode.requestFocus();
            return false;
        }

        else if (TextUtils.isEmpty(aadharNo.getText().toString()))
        {
            aadharNo.setError("Oops! Aadhar Card blank");
            aadharNo.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(editpanNo.getText().toString()))
        {
            editpanNo.setError("Oops!Pan Card blank");
            editpanNo.requestFocus();
            return false;
        }

        return true;

    }




}
