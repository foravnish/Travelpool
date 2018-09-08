package tbholidays.app.holidays.Activity;

import android.Manifest;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import tbholidays.app.holidays.CameraAct.ImagePickerActivity;
import tbholidays.app.holidays.R;
import tbholidays.app.holidays.Utils.Api;
import tbholidays.app.holidays.Utils.JSONParser;
import tbholidays.app.holidays.Utils.MyPrefrences;
import tbholidays.app.holidays.Utils.Util;

public class EditProfile extends AppCompatActivity {

    TextView tve_name,tve_mobile,tve_user_id,tve_email;
    EditText tve_addres,tve_city,tve_state,tve_pincode,tve_aadharNo,tve_panNo;
    TextView pan2,pass2,aadhar2;
    Button UpdateProfile;
    Dialog dialog;
    CircleImageView aadharImage;
    CircleImageView panImage;
    CircleImageView passportImage;
    ImageView editProfile;

    //private static final int REQUEST_PICK_IMAGE = 1002;
    Bitmap imageBitmap;
    File f=null;
    File f2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        UpdateProfile=findViewById(R.id.UpdateProfile);

        tve_name=findViewById(R.id.tve_name);
        tve_mobile=findViewById(R.id.tve_mobile);
        tve_user_id=findViewById(R.id.tve_user_id);
        tve_email=findViewById(R.id.tve_email);
        tve_addres=findViewById(R.id.tve_addres);
        tve_city=findViewById(R.id.tve_city);
        tve_state=findViewById(R.id.tve_state);
        tve_pincode=findViewById(R.id.tve_pincode);
        tve_aadharNo=findViewById(R.id.tve_aadharNo);
        tve_panNo=findViewById(R.id.tve_panNo);
        editProfile=findViewById(R.id.editProfile);
        ImageView textBack= findViewById(R.id.textBack);


        pan2=findViewById(R.id.pan2);
        pass2=findViewById(R.id.pass2);
        aadhar2=findViewById(R.id.aadhar2);

        passportImage=findViewById(R.id.passportImage);
        panImage=findViewById(R.id.panImage);
        aadharImage=findViewById(R.id.aadharImage);

        dialog=new Dialog(EditProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        textBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ProfileAct.class));
            }
        });

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = null;
                String filename = null;
                try {
                    path = f.toString();
                    filename = path.substring(path.lastIndexOf("/") + 1);
                    Log.d("dsfdfsdfsfs", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Toast.makeText(AddProduct.this, "yes", Toast.LENGTH_SHORT).show();
                PostData( path, filename);



               // PostData("", "");

            }
        });
        pan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });

        aadhar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPermissionGranted()){
                    Log.d("fsdfsdfdfdfsdf","true");
                    pickImage();
                }else{
                    Log.d("fsdfsdfdfdfsdf","false");
                    ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });


        getProfile();


    }

    private void getProfile() {

        Util.showPgDialog(dialog);
//
        Log.d("sdfsdfsdfsdfs", MyPrefrences.getUserType(getApplicationContext()));


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userbyid+"/"+MyPrefrences.getUserID(getApplicationContext())+"/"+MyPrefrences.getUserType(getApplicationContext()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("Responseprofile", "" + response);


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){

                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        //  for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.optJSONObject(0);


                        tve_name.setText(jsonObject1.optString("name").toUpperCase());
                        tve_mobile.setText(jsonObject1.optString("mobile").toString());
                        tve_user_id.setText(jsonObject1.optString("name").toString());
                        tve_email.setText(jsonObject1.optString("email").toString());
                        tve_addres.setText(jsonObject1.optString("address").toString());
                        tve_city.setText(jsonObject1.optString("city").toString());
                        tve_state.setText(jsonObject1.optString("state").toString());
                        tve_pincode.setText(jsonObject1.optString("pincode").toString());
                        tve_aadharNo.setText(jsonObject1.optString("aadhar_no").toString());
                        tve_panNo.setText(jsonObject1.optString("pan_no").toString());

//                        ImageLoader imageLoader= AppController.getInstance().getImageLoader();
//                        aadharImage.setImageUrl(jsonObject1.optString("aadhar_image").replace(" ","%20"),imageLoader);


                        Picasso.with(getApplicationContext())
                                .load(jsonObject1.optString("aadhar_image").replace(" ","%20"))
                                .fit()
                                // .transform(transformation)
                                .into(aadharImage);


                        if (jsonObject1.optString("pan_image").toString().equals("")){
                            pan2.setVisibility(View.VISIBLE);
                        }
                        else  if (!jsonObject1.optString("pan_image").toString().equals("")){
                            // pan2.setVisibility(View.GONE);

                            Picasso.with(getApplicationContext())
                                    .load(jsonObject1.optString("pan_image").replace(" ","%20"))
                                    .fit()
                                    // .transform(transformation)
                                    .into(panImage);
                        }



                        if (jsonObject1.optString("passport_image").toString().equals("")){
                            pass2.setVisibility(View.VISIBLE);
                        }
                        else  if (!jsonObject1.optString("passport_image").toString().equals("")){
                            // pass2.setVisibility(View.GONE);

                            Picasso.with(getApplicationContext())
                                    .load(jsonObject1.optString("passport_image").replace(" ","%20"))
                                    .fit()
                                    // .transform(transformation)
                                    .into(passportImage);
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
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
                params.put("id", MyPrefrences.getUserID(getApplicationContext()));
                params.put("user_type", MyPrefrences.getUserType(getApplicationContext()).toLowerCase());


                Log.d("sfsdfsdfsdfs",MyPrefrences.getUserID(getApplicationContext()));
                return params;
            }


        };
        queue.add(strReq);

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

                jsonObject = uploadImageFile(EditProfile.this, val,path, fName);

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

                    Intent intent = new Intent(EditProfile.this, ProfileAct.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Profile Successfully Updated.", Toast.LENGTH_SHORT).show();



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

                    .addFormDataPart("user_id", MyPrefrences.getUserID(getApplicationContext()))
                    .addFormDataPart("type",MyPrefrences.getUserType(getApplicationContext()) )
                    .addFormDataPart("name", tve_user_id.getText().toString())
                    .addFormDataPart("email", tve_email.getText().toString())
                    .addFormDataPart("mobile", MyPrefrences.getMobile(getApplicationContext()))
                    //.addFormDataPart("password", editPassword.getText().toString())
                    .addFormDataPart("address",tve_addres.getText().toString())
                    .addFormDataPart("city", tve_city.getText().toString())
                    .addFormDataPart("state", tve_state.getText().toString())
                    .addFormDataPart("pincode", tve_pincode.getText().toString())

                    .addFormDataPart("aadhar_no", tve_aadharNo.getText().toString())
                    .addFormDataPart("pan_no", tve_panNo.getText().toString())
                    .addFormDataPart("agent_id", "No")
                    //.addFormDataPart("referer_by", refVal)
                    .addFormDataPart("pan_image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))


                    //.addFormDataPart("aadhar_image", fileName1, RequestBody.create(MEDIA_TYPE_PNG, sourceFile1))

                    .build();



//            Log.d("fvfgdgdfhgghfhgdfh", amounts.getText().toString().replace("₹ ", ""));
//            Log.d("fvfgdgdfhgdfhqwdfs",amounts.getText().toString().replace("₹ ", ""));

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
//                     .url("http://divpreetsingh.info/app/ManiUploadsImageHere")
                    .header("Authorization", "Client-ID " + "...")
//                    .url("http://bizzcityinfo.com/AndroidApi.php?function=insertGalleryPhoto")
                    .url(Api.updateProfile)
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
        startActivityForResult(new Intent(this, ImagePickerActivity.class),1);
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
                case 1:
                    String imagePath1 = data.getStringExtra("image_path");
                    setImage(imagePath1);
                    //isImage1 = true;
                    break;
//                case 2:
//                    String imagePath2 = data.getStringExtra("image_path");
//                    setImage2(imagePath2);
//                    isImage2 = true;
//                    break;
            }
        } else {
            System.out.println("Failed to load image");
        }
    }

    private void setImage(String imagePath) {

        panImage.setImageBitmap(getImageFromStorage(imagePath));
    }

//    private void setImage2(String imagePath2) {
//
//        passportImage.setImageBitmap(getImageFromStorage2(imagePath2));
//    }

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


            String path2 = null;
            String filename = null;





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




}
