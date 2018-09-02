package travelpool.app.travelpool.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

public class Login extends AppCompatActivity {

    EditText mobile,password;
    Button btnLogin ;
    TextView btnReg,skipNow,forgt;
    Dialog dialog;

    private static final String EMAIL = "email";
    LoginButton loginButton;
    CallbackManager  callbackManager;
    ProfileTracker profileTracker;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);

        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);
        forgt=findViewById(R.id.forgt);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        dialog=new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });


        forgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popForgotpwd();
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("dfsdfsdfgsdg",loginResult.toString());
                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("dfsdfsdfgsdg","cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("dfsdfsdfgsdg","error");

            }
        });


        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("travelpool.app.travelpool", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectedId);

                //Toast.makeText(getApplicationContext(), ""+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();

                if(validate()){


//                    loginDataUser();

                    if (radioButton.getText().toString().equals("User Login")){
                        loginDataUser("user");
                        Log.d("dfsdfsdfsdfs","user");
                    }
                    else if (radioButton.getText().toString().equals("Agent Login")){
                        loginDataUser("agent");
                        Log.d("dfsdfsdfsdfs","agent");

                    }


                }

            }
        });


    }

    private void popForgotpwd() {


        final Dialog dialog2 = new Dialog(Login.this);
//                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.get_mobile_no);
        //  dialog2.setCancelable(false);

        final EditText mobile_edit= (EditText) dialog2.findViewById(R.id.mobile_edit);
        mobile_edit.setText(mobile.getText().toString());
        //TextView recieve= (TextView) dialog2.findViewById(R.id.recieve);
        //recieve.setText("Sent OTP on "+mob);
        Button submit2=(Button)dialog2.findViewById(R.id.submit2);
        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mobile_edit.getText().toString())){

                    Toast.makeText(Login.this, "Please Enter Mobile No.", Toast.LENGTH_SHORT).show();
                }

                else {

                    mobileVerify_API(mobile_edit.getText().toString(),dialog2);

                }
            }
        });

        dialog2.show();



    }

    private void mobileVerify_API(final String mobileNo, final Dialog dialog) {

        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.forgetPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("fdsfsdfsdgsdfgdsf", "forgetPassword Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {


                      //  dialog.dismiss();


                    }
                    else{
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message") , Toast.LENGTH_SHORT).show();
                        Util.errorDialog(Login.this,jsonObject.getString("message"));
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
                Toast.makeText(getApplicationContext(),"Please Connect to the Internet", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("fgdfgdfgdf","Inside getParams");

                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("mobile_number", mobileNo);


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


    private void loginDataUser(final String  type) {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(Login.this);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userlogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("dfsjfdfsdfgd", "Login Response: " + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                   // if (jsonObject.getString("status").equalsIgnoreCase("success")){

                    if (jsonObject.optString("status").equals("success")) {

                        JSONArray jsonArray=jsonObject.getJSONArray("message");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);


                        //  Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_SHORT).show();

                        MyPrefrences.setUserLogin(getApplicationContext(), true);
                        MyPrefrences.setUserID(getApplicationContext(), jsonObject1.optString("id").toString());
                        MyPrefrences.setUSENAME(getApplicationContext(), jsonObject1.optString("name").toString());
                        MyPrefrences.setEMAILID(getApplicationContext(), jsonObject1.optString("email").toString());
//                                                MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("address").toString());
                        MyPrefrences.setMobile(getApplicationContext(), jsonObject1.optString("mobile").toString());
                        MyPrefrences.setMyRefrel(getApplicationContext(),jsonObject1.optString("referer").toString());
                        //MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("pincode").toString());



                        if (type.equalsIgnoreCase("user")) {
                            Intent intent = new Intent(Login.this, HomeAct.class);
                            intent.putExtra("userType", "user");
                            startActivity(intent);
                            finish();
                            MyPrefrences.setUserType(getApplicationContext(),"user");
                        }
                        else if (type.equalsIgnoreCase("agent")) {
                            Intent intent = new Intent(Login.this, HomeAct.class);
                            intent.putExtra("userType", "agent");
                            startActivity(intent);
                            finish();
                            MyPrefrences.setUserType(getApplicationContext(),"agent");

                        }


                        registerGCM();

                    }
                    else{
                       // Toast.makeText(getApplicationContext(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
                        Util.errorDialog(Login.this,jsonObject.getString("message"));
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
                params.put("mobile", mobile.getText().toString());
                params.put("password", password.getText().toString());
                params.put("type", type);

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



//        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.user_login,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("ResponseLoginUser", response);
//                        Util.cancelPgDialog(dialog);
//                        try {
//                            JSONObject jsonObject=new JSONObject(response);
//                            if (jsonObject.getString("status").equalsIgnoreCase("success")){
//
//                                JSONArray jsonArray=jsonObject.getJSONArray("login");
//                                for (int i=0;i<jsonArray.length();i++) {
//                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
//
//
//                                    //  Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_SHORT).show();
//
//                                    MyPrefrences.setUserLogin(getApplicationContext(), true);
//                                    MyPrefrences.setUserID(getApplicationContext(), jsonObject1.optString("id").toString());
//                                    MyPrefrences.setUSENAME(getApplicationContext(), jsonObject1.optString("name").toString());
//                                    MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("email").toString());
////                                                MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("address").toString());
//                                    MyPrefrences.setMobile(getApplicationContext(),mobile.getText().toString());
//                                    //MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("state").toString());
//                                    //MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("pincode").toString());
//
//                                }
//                                Intent intent=new Intent(Login.this,HomeAct.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(Login.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
//                        Util.cancelPgDialog(dialog);
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("mobileno", mobile.getText().toString());
//                params.put("password", password.getText().toString());
//
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        postRequest.setShouldCache(false);
//
//        AppController.getInstance().addToRequestQueue(postRequest);


    }



    private boolean validate(){

        if (TextUtils.isEmpty(mobile.getText().toString()))
        {
            mobile.setError("Oops! Mobile field blank");
            mobile.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError("Oops! Password field blank");
            password.requestFocus();
            return false;
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2)
          Log.d("sdfsdfsdfsdfsf","2");

            else if (requestCode == 1)
                Log.d("sdfsdfsdfsdfsf","1");

        }

        Log.d("sdfsdfsdfsdf1", String.valueOf(requestCode));
        Log.d("sdfsdfsdfsdf2", String.valueOf(resultCode));
        Log.d("sdfsdfsdfsdf3", String.valueOf(data));
    }


    private void setFacebookData(final LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            Log.i("Response", response.toString());

                            final String email = response.getJSONObject().optString("email");

                            final String firstName = response.getJSONObject().optString("first_name");
                            String lastName = response.getJSONObject().optString("last_name");
                            String gender = response.getJSONObject().optString("gender");
                            String ids = response.getJSONObject().getString("id");
                            //String id = profile.getId();

                            if (Profile.getCurrentProfile() != null) {
                                Profile profile = Profile.getCurrentProfile();
                              /*  MyPreference.saveUserPicUrl(LoginActivity.this, "" + Profile.getCurrentProfile().
                                        getProfilePictureUri(200, 200));
                                Log.i("Login", "ProfilePic..." + Profile.getCurrentProfile().getProfilePictureUri(200, 200)+", id "+profile.getId());
                                MyPreference.saveUsername(LoginActivity.this, profile.getFirstName()
                                        + " " + profile.getLastName());
                          */
                               // new LoginPage(email,"", "facebook").execute();

                            } else {
                                profileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                       /* MyPreference.saveUserPicUrl(LoginActivity.this, "" + currentProfile.getCurrentProfile().
                                                getProfilePictureUri(200, 200));
                                        MyPreference.saveUsername(LoginActivity.this, currentProfile.getFirstName()
                                                + " " + currentProfile.getLastName());
                                     */
                                        //Log.i("Login", "ProfilePic" + currentProfile.getCurrentProfile().getProfilePictureUri(200, 200) + ", id " + currentProfile.getId());
                                       // new LoginPage(email,"", "facebook").execute();
                                        profileTracker.stopTracking();
                                    }
                                };
                                profileTracker.startTracking();
                            }

                            Log.i("Login" + "Email", email);
                            Log.i("Login" + "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);
                            Log.i("Login" + "id", ids);

                            // MyPreference.saveUserEmail(LoginActivity.this, email);

                           /* SplashScreen splashScreen = new SplashScreen();
                            SplashScreen.CricketPage cricketPage = splashScreen.new CricketPage(LoginActivity.this);
                            cricketPage.execute();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }



    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {
            token= MyPrefrences.getgcm_token(this);
            Log.d("fgsdgsdfgdfgsd",MyPrefrences.getgcm_token(getApplicationContext()));
            //token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Log.w("GCMRegIntentService", "token:" + token);

            sendRegistrationTokenToServer(token);

        } catch (Exception e) {
            // Log.w("GCMRegIntentService", "Registration error");
        }
    }


    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        final String id = MyPrefrences.getUserID(getApplicationContext());
        //Log.w("GCMRegIntentService", "loadUserid:" + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.updateFcm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {

                            Log.d("dfsdfsdfsdfsdfs",s);
                            JSONObject jsonObject=new JSONObject(s);
                            if (jsonObject.optString("status").equalsIgnoreCase("failure")){
                                //Toast.makeText(getApplicationContext(), "Some Error! Contact to Admin...", Toast.LENGTH_SHORT).show();
                            }
                            // String msg=jsonObject.getString("msg");
                            //Log.w("GCMRegIntentService", "sendRegistrationTokenToServer:" );
                            //Toast.makeText(getApplicationContext(), "sendRegistrationTokenToServer!", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), "Rakesh"+msg, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.w("GCMRegIntentService", "sendRegistrationTokenToServer! ErrorListener:" );
                        Toast.makeText(getApplicationContext(), "sendRegistrationTokenToServer! ErrorListener", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", MyPrefrences.getUserID(getApplicationContext()));
                params.put("type", MyPrefrences.getUserType(getApplicationContext()));
                params.put("fcm_id", token);

                Log.d("fdsfsdgfsdgdfgd",token);
                Log.d("fdsfsdgfsdgdfgd",MyPrefrences.getUserID(getApplicationContext()));
                Log.d("fdsfsdgfsdgdfgd",MyPrefrences.getUserType(getApplicationContext()));

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }




}
