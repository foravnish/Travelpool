package travelpool.app.travelpool.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

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
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import travelpool.app.travelpool.CameraAct.ImagePickerActivity;
import travelpool.app.travelpool.Fragments.AddUser;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.JSONParser;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

public class AddUserAct extends AppCompatActivity {


    Button btnSubReg;
    EditText editTextname,editmobile,editPassword,editPasswordCon,editEmail,editAddress,editCity,editState,editPincode,editapasport,editpanNo,aadharNo;
    TextView editTextdob,skipNow;
    //Spinner editLocation;
    Dialog dialog;
    List<String> location = new ArrayList<String>();
    List<HashMap<String, String>> DataLoc;
    ArrayAdapter aa,subcat;
    DatePickerDialog datePickerDialog;
    TextView loginNow;
    Spinner gender;
    String[] cat ={"Male","Female"};
    CircleImageView regiImage;
    private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;
    CheckBox checkBox;

    String refVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);


        btnSubReg = findViewById(R.id.btnSubReg);

        // editLocation=findViewById(R.id.editLocation);
//        editPassword = findViewById(R.id.editPassword);
//        editPasswordCon = findViewById(R.id.editPasswordCon);
        editmobile = findViewById(R.id.editmobile);
        editTextname = findViewById(R.id.editTextname);

        editTextdob = findViewById(R.id.editTextdob);
        loginNow = findViewById(R.id.loginNow);
        editEmail = findViewById(R.id.editEmail);
        editAddress = findViewById(R.id.editAddress);
        editCity = findViewById(R.id.editCity);
        editState = findViewById(R.id.editState);
        editPincode = findViewById(R.id.editPincode);

        aadharNo = findViewById(R.id.aadharNo);
        editpanNo = findViewById(R.id.editpanNo);
        editapasport = findViewById(R.id.editapasport);
        regiImage = findViewById(R.id.regiImage);
        checkBox = findViewById(R.id.checkBox);


        dialog = new Dialog(AddUserAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        DataLoc = new ArrayList<>();

        if (MyPrefrences.getRefer(getApplicationContext()).equals("")){
            refVal="No";
        }
        else {
            refVal=MyPrefrences.getRefer(getApplicationContext());

        }

        regiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("fsdfsdfsdf", "main");
                if (isPermissionGranted()) {
                    Log.d("fsdfsdfdfdfsdf", "true");
                    pickImage();
                } else {
                    Log.d("fsdfsdfdfdfsdf", "false");
                    ActivityCompat.requestPermissions(AddUserAct.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });


        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUserAct.this, Login.class);
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
                datePickerDialog = new DatePickerDialog(AddUserAct.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                editTextdob.setText(i2 + "/" + (i1 + 1) + "/" + i);
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

//                    otpAPi(editmobile.getText().toString());

//                    submitRegistration();

                        String path = null;
                        String filename = null;

                        try {
                            path = f.toString();
                            filename = path.substring(path.lastIndexOf("/") + 1);
                            Log.d("dsfdfsdfsfs", filename);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (filename == null) {
                            Util.errorDialog(AddUserAct.this, "Please Select Image");
                        } else {
                            //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                            PostData(path, filename);

                        }

                    }
                } else {
                    Util.errorDialog(AddUserAct.this, "Please check to read to all Terms & Conditions");
                }
            }
        });


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

//        else if (TextUtils.isEmpty(editPassword.getText().toString()))
//        {
//            editPassword.setError("Oops! Password blank");
//            editPassword.requestFocus();
//            return false;
//        }

//        else if (!editPassword.getText().toString().equals(editPasswordCon.getText().toString())){
//            Toast.makeText(getApplicationContext(), "Both Password should be match", Toast.LENGTH_SHORT).show();
//            return false;
//        }

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

                jsonObject = uploadImageFile(AddUserAct.this, val,path, fName);

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

                    Intent intent = new Intent(AddUserAct.this, Login.class);
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

                    .addFormDataPart("name", editTextname.getText().toString())
                    .addFormDataPart("email", editEmail.getText().toString())
                    .addFormDataPart("mobile", editmobile.getText().toString())
                    .addFormDataPart("password", "123456")
                    .addFormDataPart("address",editAddress.getText().toString())
                    .addFormDataPart("city", editCity.getText().toString())
                    .addFormDataPart("state", editState.getText().toString())
                    .addFormDataPart("pincode", editPincode.getText().toString())

                    .addFormDataPart("aadhar_no", aadharNo.getText().toString())
                    .addFormDataPart("pan_no", editpanNo.getText().toString())
                    .addFormDataPart("agent_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("referer_by", refVal)
                    .addFormDataPart("aadhar_image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))
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
