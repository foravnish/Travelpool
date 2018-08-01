package travelpool.app.travelpool.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import travelpool.app.travelpool.Activity.Login;
import travelpool.app.travelpool.Activity.Registration;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUser extends Fragment {


    public AddUser() {
        // Required empty public constructor
    }
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_user, container, false);


        editPassword=view.findViewById(R.id.editPassword);
        editPasswordCon=view.findViewById(R.id.editPasswordCon);
        editmobile=view.findViewById(R.id.editmobile);
        editTextname=view.findViewById(R.id.editTextname);

        editTextdob=view.findViewById(R.id.editTextdob);
        loginNow=view.findViewById(R.id.loginNow);
        editEmail=view.findViewById(R.id.editEmail);
        editAddress=view.findViewById(R.id.editAddress);
        editCity=view.findViewById(R.id.editCity);
        editState=view.findViewById(R.id.editState);
        editPincode=view.findViewById(R.id.editPincode);

        aadharNo=view.findViewById(R.id.aadharNo);
        editpanNo=view.findViewById(R.id.editpanNo);
        editapasport=view.findViewById(R.id.editapasport);
        btnSubReg=view.findViewById(R.id.btnSubReg);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        DataLoc=new ArrayList<>();


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


        return view;
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
            Toast.makeText(getActivity(), "Both Password should be match", Toast.LENGTH_SHORT).show();
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

    private void submitRegistration() {

        Util.showPgDialog(dialog);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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

//                        Log.d("fsdfsdfsdfs","true");
//
////                        Toast.makeText(getApplicationContext(), "Registration Successfully...", Toast.LENGTH_SHORT).show();
//                        Util.errorDialog(Registration.this,"Registration Successfully...");
//
                        smsAPI();
//
//                        Intent intent = new Intent(getActivity(), Login.class);
//                        startActivity(intent);
//                        finish();
//                        Toast.makeText(getActivity(), "Please Login...", Toast.LENGTH_SHORT).show();

                    }
                    else if (jsonObject.optString("status").equals("failure")){
                        Util.errorDialog(getActivity(),jsonObject.getString("message"));
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
                Toast.makeText(getActivity(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
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
                params.put("type", MyPrefrences.getUserID(getActivity()));
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



}
