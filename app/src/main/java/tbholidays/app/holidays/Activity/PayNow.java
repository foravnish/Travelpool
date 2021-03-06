package tbholidays.app.holidays.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tbholidays.app.holidays.Fragments.MyKitty;
import tbholidays.app.holidays.PayUMoney.AppEnvironment;
import tbholidays.app.holidays.PayUMoney.AppPreference;
import tbholidays.app.holidays.R;
import tbholidays.app.holidays.Utils.Api;
import tbholidays.app.holidays.Utils.AppController;
import tbholidays.app.holidays.Utils.MyPrefrences;
import tbholidays.app.holidays.Utils.Util;

public class PayNow extends AppCompatActivity {

    TextView instal,name,packageName;
    Button payNow;
    Dialog dialog;
    JSONObject jsonObject1;
    JSONObject jsonObject2;

    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private AppPreference mAppPreference;
    String merKey,merId,salt;
    NetworkImageView imageView;
    String paymentMode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);

        Log.d("dfgdgdgdf",getIntent().getStringExtra("data"));

        instal=findViewById(R.id.instal);
        name=findViewById(R.id.name);
        packageName=findViewById(R.id.packageName);
        payNow=findViewById(R.id.payNow);
        imageView=findViewById(R.id.imageView);

        dialog=new Dialog(PayNow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        mAppPreference = new AppPreference();
        merKey= AppEnvironment.SANDBOX.merchant_Key();
        merId=AppEnvironment.SANDBOX.merchant_ID();
        salt=AppEnvironment.SANDBOX.salt();


        final RadioGroup gr =(RadioGroup)findViewById(R.id.radiogroup) ;
        final int selectedId = gr.getCheckedRadioButtonId();

        gr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);
//                textViewChoice.setText("You Selected "+rb.getText());
                // Toast.makeText(getActivity(),rb.getText()+"", Toast.LENGTH_SHORT).show();
                paymentMode=rb.getText().toString();
               // Toast.makeText(getApplicationContext(), ""+rb.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });




        try {
            jsonObject1=new JSONObject(getIntent().getStringExtra("data"));

            name.setText(jsonObject1.optString("name"));

            JSONArray jsonArray2=jsonObject1.getJSONArray("package_details");
            jsonObject2=jsonArray2.getJSONObject(0);
            packageName.setText(jsonObject2.optString("name"));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject2.optString("banner").toString().replace(" ","%20"),imageLoader);

            instal.setText("Per Month ₹ : "+getIntent().getStringExtra("pay_amount"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkJoinOrNot(jsonObject1.optString("id"),getIntent().getStringExtra("pay_amount"));

//                launchPayUMoneyFlow("", jsonObject.optString("per_month_installment"));

            }
        });
    }

    private void checkJoinOrNot(final String id,final String price) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(PayNow.this);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Api.myKittyStatus+"/"+MyPrefrences.getUserID(getApplicationContext())+"/"+id.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfsjfdfsdfgd", "joinCheck Response: " + response);
                //parse your response here

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){



                        if (paymentMode.equals("")){
                            Util.errorDialog(PayNow.this,"Please Select Payment Mode.");
                        }
                        else {
                            if (paymentMode.equals("Pay Online")) {

//                                launchPayUMoneyFlow("", price);

                                submitData(jsonObject1.optString("id"),jsonObject1.optString("name"),jsonObject2.optString("id"),jsonObject2.optString("name"),jsonObject1.optString("per_month_installment"),"online");

                            } else if (paymentMode.equals("Pay By Cash")) {
                                submitData(jsonObject1.optString("id"),jsonObject1.optString("name"),jsonObject2.optString("id"),jsonObject2.optString("name"),jsonObject1.optString("per_month_installment"),"cash");

//                                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                            } else if (paymentMode.equals("Pay By Cheque")) {
                                submitData(jsonObject1.optString("id"),jsonObject1.optString("name"),jsonObject2.optString("id"),jsonObject2.optString("name"),jsonObject1.optString("per_month_installment"),"cheque");

                                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                            }
                        }





//                            Util.errorDialog(PayNow.this,"You have Already Joined this Kitty, Please Select another Kitty!");
                            Log.d("sdfsfsdfsdfsds","no");



                        //Toast.makeText(getApplicationContext(), ""+jsonObject.optString("msg") ,Toast.LENGTH_SHORT).show();

                    }
                    else  if (jsonObject.getString("status").equalsIgnoreCase("failure")){

                      //  launchPayUMoneyFlow("", price);
                        Util.errorDialog(PayNow.this,"You have Already Joined this Kitty, Please Select another Kitty!");
                        Log.d("sdfsfsdfsdfsds","yes");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Util.cancelPgDialog(dialog);
                Log.e("fgdfgfrrgertgerytry", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
//                params.put("userid",  MyPrefrences.getUserID(getApplicationContext()));
//                params.put("kitty_id",  id.toString());


                Log.d("fsfsdfsdfsdfsf",MyPrefrences.getUserID(getApplicationContext()));
                Log.d("fsfsdfsdfsdfsf",id.toString());
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

    }


    private void submitData(final String id,final String K_name,final String P_id,final String P_name,final String amount, final String P_mode) {
        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(PayNow.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.purchaseKitty, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("fgfdgdfgdfgdfgfhfh", "Purchase Response: " + response);
                //parse your response here


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){

//                        Util.errorDialog(PayNow.this,jsonObject.getString("message"));
//                        Toast.makeText(getApplicationContext(), ""+jsonObject.optString("message") ,Toast.LENGTH_SHORT).show();
                          Util.errorDialog(PayNow.this,jsonObject.optString("message"));
//                        Fragment fragment = new MyKitty();
//                        FragmentManager manager = getSupportFragmentManager();
//                        FragmentTransaction ft = manager.beginTransaction();
//                        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();


                        if (P_mode.equals("cash")){
                            smsAPI_Cash(K_name,amount);
                        }
                        else if (P_mode.equals("cheque")){
                            smsAPI_Chek(K_name,amount);
                        }
                        else if (P_mode.equals("online")){
                            smsAPI_Online(K_name,amount);
                        }


                    }
                    else{
                        Util.errorDialog(PayNow.this,jsonObject.getString("message"));
                        //Toast.makeText(getApplicationContext(),jsonObject.optString("message") , Toast.LENGTH_SHORT).show();
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
                params.put("user_id",  MyPrefrences.getUserID(getApplicationContext()));
                params.put("user_name",  MyPrefrences.getUSENAME(getApplicationContext()));
                params.put("user_type",  MyPrefrences.getUserType(getApplicationContext()));
                params.put("kitty_name",  K_name.toLowerCase());
                params.put("kitty_id",  id.toString());
                params.put("package_id",  P_id.toString());
                params.put("package_name",  P_name.toString());

                 if (P_mode.equals("cash")){
                     params.put("payment_id",  "cash");
                     params.put("transaction_id",  "cash");
                     params.put("response", "cash");

                 }
                 else if (P_mode.equals("cheque")){
                     params.put("payment_id",  "cheque");
                     params.put("transaction_id",  "cheque");
                     params.put("response", "cheque");

                 }
                 else if (P_mode.equals("online")){
                     params.put("payment_id",  "online");
                     params.put("transaction_id",  "online");
                     params.put("response", "online");

                 }
                params.put("pay_amount",  amount.toString());
                params.put("payment_status", "success");


                Log.d("fsfsdfsdfsdfsf1",MyPrefrences.getUserID(getApplicationContext()));
                Log.d("fsfsdfsdfsdfsf2",K_name.toString());
                Log.d("fsfsdfsdfsdfsf3",id.toString());
                Log.d("fsfsdfsdfsdfsf4",P_id.toString());
                Log.d("fsfsdfsdfsdfsf5",P_name.toString());
                Log.d("fsfsdfsdfsdfsf6",amount.toString());

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

    }



    private void launchPayUMoneyFlow(String package_name, String total_value) {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
//        payUmoneyConfig.setDoneButtonText(((EditText) findViewById(R.id.status_page_et)).getText().toString());

        //Use this to set your custom title for the activity
//        payUmoneyConfig.setPayUmoneyActivityTitle(((EditText) findViewById(R.id.activity_title_et)).getText().toString());

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
//            amount = 1;
            amount = Double.parseDouble(total_value.toString());
            Log.d("sdfsdfsddsgdf",total_value.toString());
            // Log.d("fgdfgdfhdhdh", String.valueOf(Double.parseDouble(totlPrice.getText().toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }


     //   Log.d("sdfsfsdfgsgsd", Getseter.preferences.getString("mobile",""));

        String txnId = System.currentTimeMillis() + "";
//        String phone = mobile_til.getEditText().getText().toString().trim();
        String phone ="8791193678";
//        String productName = mAppPreference.getProductInfo();
//        String firstName = mAppPreference.getFirstName();
//        String email = email_til.getEditText().getText().toString().trim();
//        String email = MyPrefrences.getEMAILID(getApplicationContext()).toString();
        String email = "foravnish@gmail.com";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName("TravelPool  "+package_name)
                .setFirstName(MyPrefrences.getUSENAME(getApplicationContext()))
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
//                .setKey(appEnvironment.merchant_Key())
                .setKey(merKey.toString())
//                .setMerchantId(appEnvironment.merchant_ID());
                .setMerchantId(merId.toString());

        Log.d("dfdsfdsfsdfsd",merKey);
        Log.d("dfdsfdsfsdfsd",merId);

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * *//*
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

           if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,MainActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
            }*/

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            // payNowButton.setEnabled(true);
        }
    }

    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((AppController) getApplication()).getAppEnvironment();
//        stringBuilder.append(appEnvironment.salt());
        stringBuilder.append(salt.toString());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        Log.d("fgdfhgdfhdfhd",postParams);

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }
    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayNow.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL(Api.gethashCode);

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());
                Log.d("fgdfgdfgdfg",response.toString());
                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            // payNowButton.setEnabled(true);

            Log.d("fgdfgdfgdfg",merchantHash.toString());

            //  merchantHash="35eceef8992006b59b2f46d7ef6ce13b3dcgfhfgj258996b871f863ed7fghfgjhfgee9e76bb357209f04b488afcc6a687f354a13750421e0ec85bcb40006441df530b84831c69b4";

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(getApplicationContext(), "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);
                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayNow.this, AppPreference.selectedTheme, mAppPreference.isOverrideResultScreen());
                } else {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,  PayNow.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("gffgdfgfhdhdh","true");
        Log.d("MainActivity123", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                String payuResponse = transactionResponse.getPayuResponse();
                String merchantResponse = transactionResponse.getTransactionDetails();


                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Log.d("responseprint","success "+payuResponse);
                    Log.d("responseprint","success "+merchantResponse);

                    new AlertDialog.Builder(PayNow.this)
                            .setCancelable(false)
                            .setMessage("Payment Success...")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                    //  new BuyPackagesApi2(getApplicationContext(),"success","payment_success").execute();

                  //  placeOrder();

                    submitData(jsonObject1.optString("id"),jsonObject1.optString("name"),jsonObject2.optString("id"),jsonObject2.optString("name"),jsonObject1.optString("per_month_installment"),"online");


                } else {
                    //Failure Transaction
                    Log.d("responseprint","failed "+payuResponse);

                    new AlertDialog.Builder(PayNow.this)
                            .setCancelable(false)
                            .setMessage("Payment Failed...")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();

                    // new BuyPackagesApi2(getApplicationContext(),"failed","payment_failed").execute();
                }

                // Response from Payumoney

//                new AlertDialog.Builder(PayActivity.this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("dfsdgdfgdfg", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("dfsdgdfgdfg", "Both objects are null!");
            }
        }
    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void smsAPI_Cash(String kitty_name,String amount) {


//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=TEST%20MSG", null, new Response.Listener<JSONObject>() {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+MyPrefrences.getMobile(getApplicationContext())+"&sndr=TRPOOL&text=Thanks%20for%20the%20payment%20%20Rs."+amount+"Amount%20made%20of%20kitty%20"+kitty_name+"%20through%20cash%20payments.%20The%20same%20has%20been%20updated%20in%20your%20account.Contact%20Helpline%20no.%20for%20cash%20Payments.", null, new Response.Listener<JSONObject>() {
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


    private void smsAPI_Chek(String kitty_name,String amount) {


//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=TEST%20MSG", null, new Response.Listener<JSONObject>() {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+MyPrefrences.getMobile(getApplicationContext())+"&sndr=TRPOOL&text=Thanks%20for%20the%20payment%20%20Rs."+amount+"Amount%20made%20of%20"+kitty_name+"%20kitty%20through%20cheque%20no.chequeno.filed%20.%20The%20same%20has%20been%20updated%20in%20your%20account.Contact%20Helpline%20no.%20for%20cash%20Payments.", null, new Response.Listener<JSONObject>() {
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

    private void smsAPI_Online(String kitty_name,String amount) {


//        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=TEST%20MSG", null, new Response.Listener<JSONObject>() {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+MyPrefrences.getMobile(getApplicationContext())+"&sndr=TRPOOL&text=Thanks%20for%20the%20payment%20Rs."+amount+"%20Amount%20made%20of%"+kitty_name+"%20kitty%20through%20online%20payments.%20The%20same%20has%20been%20updated%20in%20your%20account.", null, new Response.Listener<JSONObject>() {
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



}
