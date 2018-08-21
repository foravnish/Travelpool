package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import travelpool.app.travelpool.Activity.ProfileAct;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyReward extends Fragment {


    public MyReward() {
        // Required empty public constructor
    }
    Dialog dialog;


    TextView myReward,myUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_reward, container, false);
        myReward=view.findViewById(R.id.myReward);
        myUser=view.findViewById(R.id.myUser);


        getActivity().setTitle("My Rewards");
//
        Log.d("sdfsdfsdfsdfs", MyPrefrences.getUserType(getActivity()));

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.userbyid+"/"+MyPrefrences.getUserID(getActivity())+"/"+MyPrefrences.getUserType(getActivity()), new Response.Listener<String>() {
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




                    }
                    else{
                        Toast.makeText(getActivity(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
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
                params.put("id", MyPrefrences.getUserID(getActivity()));
                params.put("user_type", MyPrefrences.getUserType(getActivity()).toLowerCase());


                Log.d("sfsdfsdfsdfs",MyPrefrences.getUserID(getActivity()));
                return params;
            }


        };
        queue.add(strReq);
        return  view;
    }






}
