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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);

        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);
        forgt=findViewById(R.id.forgt);

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

//                Intent intent=new Intent(Login.this,HomeAct.class);
//                startActivity(intent);
//                finish();

                if(validate()){

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


                                              //  Toast.makeText(getApplicationContext(), "Login Successfully...", Toast.LENGTH_SHORT).show();

                                                MyPrefrences.setUserLogin(getApplicationContext(), true);
                                                MyPrefrences.setUserID(getApplicationContext(), jsonObject1.optString("id").toString());
                                                MyPrefrences.setUSENAME(getApplicationContext(), jsonObject1.optString("name").toString());
                                                MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("email").toString());
//                                                MyPrefrences.setEMAILID(getApplicationContext(),jsonObject1.optString("address").toString());
                                                MyPrefrences.setMobile(getApplicationContext(),mobile.getText().toString());
                                                //MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("state").toString());
                                                //MyPrefrences.setImage(getApplicationContext(),jsonObject1.optString("pincode").toString());

                                            }
                                            Intent intent=new Intent(Login.this,HomeAct.class);
                                            startActivity(intent);
                                            finish();
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
                                    Toast.makeText(Login.this, "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                                    Util.cancelPgDialog(dialog);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("mobileno", mobile.getText().toString());
                            params.put("password", password.getText().toString());

                            return params;
                        }
                    };
                    postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    postRequest.setShouldCache(false);

                    AppController.getInstance().addToRequestQueue(postRequest);

                }

            }
        });


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

}
