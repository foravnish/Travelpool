package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import travelpool.app.travelpool.Activity.PayNow;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyKitty extends Fragment {


    public MyKitty() {
        // Required empty public constructor
    }
    List<HashMap<String,String>> AllProducts ;
    GridView expListView;
    HashMap<String,String> map;
    Dialog dialog;
    JSONObject jsonObject1;
    ImageView imageNoListing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_kitty, container, false);
        getActivity().setTitle("My Kitty");
        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
      //  Util.showPgDialog(dialog);


        submitData();


//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Api.searchCompany+"?keyword="+"4"+"&cityId=1", null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Respose123", response.toString());
//
//                Util.cancelPgDialog(dialog);
//                try {
//                    // Parsing json object response
//                    // response will be a json object
////                    String name = response.getString("name");
//
//                    if (response.getString("status").equalsIgnoreCase("success")){
//
//                        expListView.setVisibility(View.VISIBLE);
//                        imageNoListing.setVisibility(View.GONE);
//
//                        final JSONArray jsonArray=response.getJSONArray("message");
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//
//                            JSONArray jsonArray1=jsonObject.getJSONArray("rating");
//                            JSONArray jsonArray2=jsonObject.getJSONArray("payment_mode");
//
//                            for (int j=0;j<jsonArray1.length();j++){
//                                jsonObject1=jsonArray1.getJSONObject(j);
//                                Log.d("fdsgvfdh",jsonObject1.optString("ratingUser"));
//                                Log.d("fdsgvfdh",jsonObject1.optString("rating"));
//                            }
//
//
//                            map=new HashMap();
//                            map.put("id",jsonObject.optString("id"));
//                            map.put("cat_id",jsonObject.optString("cat_id"));
//                            map.put("company_name",jsonObject.optString("company_name"));
//                            map.put("address",jsonObject.optString("address"));
//                            map.put("c1_fname",jsonObject.optString("c1_fname"));
//                            map.put("c1_mname",jsonObject.optString("c1_mname"));
//                            map.put("c1_lname",jsonObject.optString("c1_lname"));
//                            map.put("c1_email",jsonObject.optString("c1_email"));
//                            map.put("c1_mobile1",jsonObject.optString("c1_mobile1"));
//                            map.put("c1_mobile2",jsonObject.optString("c1_mobile2"));
//                            map.put("website",jsonObject.optString("website"));
//                            map.put("totlauser",jsonObject1.optString("ratingUser"));
//                            map.put("rating",jsonObject1.optString("rating"));
//                            map.put("liking",jsonObject.optString("liking"));
//                            map.put("locationName",jsonObject.optString("locationName"));
//                            map.put("logo",jsonObject.optString("logo"));
//                            map.put("companyLogo",jsonObject.optString("companyLogo"));
//                            map.put("premium",jsonObject.optString("premium"));
//                            map.put("offer",jsonObject.optString("offer"));
//                            map.put("pincode",jsonObject.optString("pincode"));
//                            map.put("distance",jsonObject.optString("distance"));
//
//                            map.put("latitude",jsonObject.optString("latitude"));
//                            map.put("longitude",jsonObject.optString("longitude"));
//
//                            map.put("payment_mode",jsonObject.optString("payment_mode"));
//                            map.put("closing_time",jsonObject.optString("closing_time"));
//                            map.put("closing_time2",jsonObject.optString("closing_time2"));
//                            map.put("opening_time",jsonObject.optString("opening_time"));
//                            map.put("opening_time2",jsonObject.optString("opening_time2"));
//                            map.put("min_order_amnt",jsonObject.optString("min_order_amnt"));
//                            map.put("min_order_qty",jsonObject.optString("min_order_qty"));
//                            map.put("closing_days",jsonObject.optString("closing_days"));
//
//
//
//                            Adapter adapter=new Adapter();
//                            expListView.setAdapter(adapter);
//                            AllProducts.add(map);
//
//
////                            expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                @Override
////                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////
////
////                                    try {
////                                        Intent intent=new Intent(getActivity(),ProfileAct.class);
////                                        intent.putExtra("data",jsonArray.get(i).toString());
////                                        startActivity(intent);
////                                    } catch (JSONException e) {
////                                        e.printStackTrace();
////                                    }
////
////
////
////                                }
////                            });
//
//
//                        }
//                    }
//                    else{
//                        expListView.setVisibility(View.GONE);
//                        imageNoListing.setVisibility(View.VISIBLE);
//                        //Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
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
//                Toast.makeText(getActivity(),
//                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                Util.cancelPgDialog(dialog);
//
//            }
//        });


//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq);



        return view;

    }




    private void submitData() {

        Util.showPgDialog(dialog);


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Api.my_kitty, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Util.cancelPgDialog(dialog);
                Log.e("MyKitty", "Response: " + response);
                //parse your response here

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){

                        Toast.makeText(getApplicationContext(), ""+jsonObject.optString("msg") ,Toast.LENGTH_SHORT).show();

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
                params.put("userid",  MyPrefrences.getUserID(getActivity()));
              //  params.put("kitty_id",  id.toString());

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


    public class Viewholder{
        ImageView imgFav,stars;
        TextView address,name,totlareview,area,subcatListing,distance;
        LinearLayout liner,linerLayoutOffer;

        NetworkImageView imgaeView;
        CardView cardView;
        ImageView callNow1;
        //  ShimmerTextView offersText;
//        Shimmer shimmer;
        ImageView img1,img2,img3,img4,img5;

        LinearLayout footer_layout;

    }
    class Adapter extends BaseAdapter {

        LayoutInflater inflater;


        Boolean flag=false;
        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (inflater == null) {
//                throw new AssertionError("LayoutInflater not found.");
//            }
        }

        @Override
        public int getCount() {
            return AllProducts.size();
        }

        @Override
        public Object getItem(int position) {
            return AllProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            convertView=inflater.inflate(R.layout.list_my_kitty,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
            viewholder.address=convertView.findViewById(R.id.address);
            //viewholder.imgFav=convertView.findViewById(R.id.imgFav);
//            viewholder.stars=convertView.findViewById(R.id.stars);
            viewholder.liner=convertView.findViewById(R.id.liner);
            viewholder.totlareview=convertView.findViewById(R.id.totlareview);

            viewholder.area=convertView.findViewById(R.id.area);
//            viewholder.callNow1=convertView.findViewById(R.id.callNow1);
            viewholder.imgaeView=convertView.findViewById(R.id.imgaeView);
            viewholder.linerLayoutOffer=convertView.findViewById(R.id.linerLayoutOffer);
            viewholder.cardView=convertView.findViewById(R.id.cardView);
            // viewholder.offersText=convertView.findViewById(R.id.offersText);
            viewholder.subcatListing=convertView.findViewById(R.id.subcatListing);
            viewholder.distance=convertView.findViewById(R.id.distance);


//

            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.address.setText(AllProducts.get(position).get("address"));
            viewholder.totlareview.setText(AllProducts.get(position).get("totlauser")+" Reviews");
            viewholder.area.setText(AllProducts.get(position).get("locationName"));
            viewholder.subcatListing.setText(AllProducts.get(position).get("keywords"));
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewholder.imgaeView.setImageUrl(AllProducts.get(position).get("logo"),imageLoader);



//
//            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
//            Typeface face2=Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
//            viewholder.name.setTypeface(face);
//            viewholder.address.setTypeface(face2);
//            viewholder.totlareview.setTypeface(face2);
//            viewholder.area.setTypeface(face2);
//            viewholder.subcatListing.setTypeface(face2);
//            viewholder.distance.setTypeface(face2);
//

            return convertView;
        }
    }


}
