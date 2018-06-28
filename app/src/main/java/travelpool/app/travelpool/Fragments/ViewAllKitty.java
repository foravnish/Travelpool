package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllKitty extends Fragment {


    public ViewAllKitty() {
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
        View view= inflater.inflate(R.layout.fragment_view_all_kitty, container, false);
        getActivity().setTitle("All Kitty");

        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
       // Util.showPgDialog(dialog);


//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                Api.searchCompany+"?keyword="+"4"+"&cityId=1", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Respose123", response.toString());
//
//                Util.cancelPgDialog(dialog);
//                try {
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
//                Util.cancelPgDialog(dialog);
//
//            }
//        });


//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq);

        return view;

    }

    public class Viewholder{
        ImageView imgFav,stars;
        TextView address,name,totlareview,area,subcatListing,distance;
        LinearLayout liner,linerLayoutOffer;

        NetworkImageView imgaeView;
        CardView cardView;


        LinearLayout footer_layout;

    }
    class Adapter extends BaseAdapter {

        LayoutInflater inflater;


        Boolean flag=false;
        Adapter() {
            inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


            convertView=inflater.inflate(R.layout.list_lsiting,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
            viewholder.address=convertView.findViewById(R.id.address);
            viewholder.liner=convertView.findViewById(R.id.liner);
            viewholder.totlareview=convertView.findViewById(R.id.totlareview);

            viewholder.area=convertView.findViewById(R.id.area);
            viewholder.imgaeView=convertView.findViewById(R.id.imgaeView);
            viewholder.linerLayoutOffer=convertView.findViewById(R.id.linerLayoutOffer);
            viewholder.cardView=convertView.findViewById(R.id.cardView);
            viewholder.subcatListing=convertView.findViewById(R.id.subcatListing);
            viewholder.distance=convertView.findViewById(R.id.distance);


            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.name.setText(AllProducts.get(position).get("company_name"));
            viewholder.address.setText(AllProducts.get(position).get("address"));
            viewholder.totlareview.setText(AllProducts.get(position).get("totlauser")+" Reviews");
            viewholder.area.setText(AllProducts.get(position).get("locationName"));
            viewholder.subcatListing.setText(AllProducts.get(position).get("keywords"));
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewholder.imgaeView.setImageUrl(AllProducts.get(position).get("logo"),imageLoader);




//            Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "muli_semibold.ttf");
//            Typeface face2=Typeface.createFromAsset(getActivity().getAssets(), "muli.ttf");
//            viewholder.name.setTypeface(face);
//            viewholder.address.setTypeface(face2);
//            viewholder.totlareview.setTypeface(face2);
//            viewholder.area.setTypeface(face2);
//            viewholder.subcatListing.setTypeface(face2);
//            viewholder.distance.setTypeface(face2);


            return convertView;
        }
    }



}
