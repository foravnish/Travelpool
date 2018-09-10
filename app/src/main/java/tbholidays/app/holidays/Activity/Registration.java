package tbholidays.app.holidays.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import tbholidays.app.holidays.CameraAct.ImagePickerActivity;
import tbholidays.app.holidays.R;
import tbholidays.app.holidays.Utils.Api;
import tbholidays.app.holidays.Utils.AppController;
import tbholidays.app.holidays.Utils.JSONParser;
import tbholidays.app.holidays.Utils.MyPrefrences;
import tbholidays.app.holidays.Utils.Util;


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
    CircleImageView regiImage;
    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;
    CheckBox checkBox;
    String refVal;
    TextView tncBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSubReg=findViewById(R.id.btnSubReg);
        tncBtn=findViewById(R.id.tncBtn);

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
        regiImage=findViewById(R.id.regiImage);
        checkBox=findViewById(R.id.checkBox);


        dialog=new Dialog(Registration.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        DataLoc=new ArrayList<>();


        tncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this,TermandConditions.class));
            }
        });

        if (MyPrefrences.getRefer(getApplicationContext()).equals("")){
            refVal="No";
        }
        else {
            refVal=MyPrefrences.getRefer(getApplicationContext());

        }

        regiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fsdfsdfsdf","main");
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });


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

                if (checkBox.isChecked()) {
                    if (validate()) {
                        PostData("", "");
                    }
                }
                else{
                    Util.errorDialog(Registration.this, "Please check to read to all Terms & Conditions");
                }

//                if (checkBox.isChecked()) {
//                    if (validate()) {
//
////                    otpAPi(editmobile.getText().toString());
//
////                    submitRegistration();
//
//                        String path = null;
//                        String filename = null;
//
//                        try {
//                            path = f.toString();
//                            filename = path.substring(path.lastIndexOf("/") + 1);
//                            Log.d("dsfdfsdfsfs", filename);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (filename == null) {
//                            Util.errorDialog(Registration.this, "Please Select Image");
//                        } else {
//                            //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
//                            PostData(path, filename);
//
//                        }
//
//                    }
//                }
//                else{
//                    Util.errorDialog(Registration.this, "Please check to read to all Terms & Conditions");
//                }


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

                      //  smsAPI();

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
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, "http://103.27.87.89/send.php?usr=4866&pwd=trpool@travel&ph="+editmobile.getText().toString()+"&sndr=TRPOOL&text=Congratulations%20!!%20"+MyPrefrences.getUSENAME(getApplicationContext())+"%20you%20have%20successfully%20become%20a%20member%20of%20Travel%20Blasters%20Family.", null, new Response.Listener<JSONObject>() {
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
            editTextname.setError("Oops! Name blank");
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
        else if (TextUtils.isEmpty(editEmail.getText().toString()))
        {
            editEmail.setError("Oops! Email Id blank");
            editEmail.requestFocus();
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

//        else if (TextUtils.isEmpty(editAddress.getText().toString()))
//        {
//            editAddress.setError("Oops! Address blank");
//            editAddress.requestFocus();
//            return false;
//        }

        else if (TextUtils.isEmpty(editCity.getText().toString()))
        {
            editCity.setError("Oops! City blank");
            editCity.requestFocus();
            return false;
        }
//        else if (TextUtils.isEmpty(editState.getText().toString()))
//        {
//            editState.setError("Oops! State blank");
//            editState.requestFocus();
//            return false;
//        }
//        else if (TextUtils.isEmpty(editPincode.getText().toString()))
//        {
//            editPincode.setError("Oops! Pincode blank");
//            editPincode.requestFocus();
//            return false;
//        }
//
//        else if (TextUtils.isEmpty(aadharNo.getText().toString()))
//        {
//            aadharNo.setError("Oops! Aadhar Card blank");
//            aadharNo.requestFocus();
//            return false;
//        }
//        else if (TextUtils.isEmpty(editpanNo.getText().toString()))
//        {
//            editpanNo.setError("Oops!Pan Card blank");
//            editpanNo.requestFocus();
//            return false;
//        }

        return true;

    }




    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public void pickImage() {
        startActivityForResult(new Intent(this, ImagePickerActivity.class), REQUEST_PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    String imagePath = data.getStringExtra("image_path");

                    setImage(imagePath);
                    break;
            }
        } else {
            System.out.println("Failed to load image");
        }
    }

    private void setImage(String imagePath) {

        regiImage.setImageBitmap(getImageFromStorage(imagePath));
    }

    private Bitmap getImageFromStorage(String path) {
        try {
            f = new File(path);
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 512, 512);

            Log.d("sdfasafsdfsdfsdfsdf",f.toString());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void PostData(String filePath,String fileName) {

        try {
            Log.d("sdfsdfasdfsdfsdf1",filePath);
            Log.d("sdfsdfasdfsdfsdf2",fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        new AddProductData(filePath,fileName).execute();

    }


    private class AddProductData extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();

        private static final String TAG_STATUS = "status";
        private static final String TAG_MESSAGE = "msg";

        String val, path, fName, min, kmsDone, mobile, emailID, brand;
        HashMap<String, String> params = new HashMap<>();

        //EditText descreption,ageOfProd,headline,min,kmsDone,mobile,emailID;
        AddProductData(String path,String fName) {
            this.val = val;
            this.path = path;
            this.fName = fName;

        }

        @Override
        protected void onPreExecute() {
            Util.showPgDialog(dialog);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject jsonObject = null;
            try {

                jsonObject = uploadImageFile(Registration.this, val,path, fName);

                if (jsonObject != null) {

                    return jsonObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String message = "";
            String data = "";

//            if (progress.isShowing())
//                progress.dismiss();

            Util.cancelPgDialog(dialog);
            if (json != null) {


                if (json.optString("status").equalsIgnoreCase("success")) {

                    smsAPI();

                    Intent intent = new Intent(Registration.this, Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Please Login...", Toast.LENGTH_SHORT).show();



                } else {
                    Toast.makeText(getApplicationContext(), ""+json.optString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    private JSONObject uploadImageFile(Context context, String value, String filepath1, String fileName1) {

        // sourceFile2= new File("");

        File sourceFile1 = new File(filepath1);

        String result = null;
        Log.e("FindPlayerPageAsync", "File...::::" + sourceFile1 + " : " + sourceFile1.exists());
        Log.e("file name", ": " + fileName1);
        JSONObject jsonObject = null;

        try {

            ////for image
            final MediaType MEDIA_TYPE_PNG = filepath1.endsWith("png") ? MediaType.parse("image/png") : MediaType.parse("image");

            Log.e("file name", ": " + fileName1);

            //   Log.d("fgdgdfgdfgdf1",getIntent().getStringExtra("areatypenum"));

            //Log.d("dfsdfsdgfsdgd",id.toString());
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)

//                    .addFormDataPart("name", editTextname.getText().toString())
//                    .addFormDataPart("email", editEmail.getText().toString())
//                    .addFormDataPart("mobile", editmobile.getText().toString())
//                    .addFormDataPart("password", editPassword.getText().toString())
//                    .addFormDataPart("address",editAddress.getText().toString())
//                    .addFormDataPart("city", editCity.getText().toString())
//                    .addFormDataPart("state", editState.getText().toString())
//                    .addFormDataPart("pincode", editPincode.getText().toString())
//
//                    .addFormDataPart("aadhar_no", aadharNo.getText().toString())
//                    .addFormDataPart("pan_no", editpanNo.getText().toString())
//                    .addFormDataPart("agent_id", "No")
//                    .addFormDataPart("referer_by", refVal)
//                    .addFormDataPart("aadhar_image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))


                    .addFormDataPart("name", editTextname.getText().toString())
                    .addFormDataPart("email", editEmail.getText().toString())
                    .addFormDataPart("mobile", editmobile.getText().toString())
                    .addFormDataPart("password", editPassword.getText().toString())
                    .addFormDataPart("address","NA")
                    .addFormDataPart("city", editCity.getText().toString())
                    .addFormDataPart("state", "NA")
                    .addFormDataPart("pincode", "NA")
                    .addFormDataPart("aadhar_no", "NA")
                    .addFormDataPart("pan_no", "NA")
                    .addFormDataPart("agent_id", "No")
                    .addFormDataPart("referer_by", refVal)
                    //.addFormDataPart("aadhar_image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))

                    .build();



//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.signup)
//                    .url("http://templatestheme.com/demo/tradeone/ws/post_offer.php")
                    // .addHeader("enctype", "multipart/form-data")
                    .post(requestBody)
                    .build();


            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(15, TimeUnit.SECONDS);


            Log.e("request1", ":url:  " + request.urlString() + ", header: " + request.headers() + ", body " + request.body());
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            result = response.body().string();
            Log.e("responseMultipart", ": " + result);
            jsonObject = new JSONObject(result);
            Log.e("result", ": " + result);
            return jsonObject;
        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("FindPlayerPageAsync", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("FindPlayerPageAsync", "Other Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
        }
        return jsonObject;
    }



}
