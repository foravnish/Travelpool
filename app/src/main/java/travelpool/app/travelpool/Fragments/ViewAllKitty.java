package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    JSONArray jsonArray;
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
        Util.showPgDialog(dialog);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Api.ViewKitty, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeKitty", response.toString());

                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);
                        imageNoListing.setVisibility(View.GONE);

                        jsonArray=response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
                            map.put("id",jsonObject.optString("id"));
                            map.put("package_id",jsonObject.optString("package_id"));
                            map.put("name",jsonObject.optString("name"));
                            map.put("no_of_month",jsonObject.optString("no_of_month"));
                            map.put("per_month_installment",jsonObject.optString("per_month_installment"));
                            map.put("no_of_max_members",jsonObject.optString("no_of_max_members"));
                            map.put("term_and_cond",jsonObject.optString("term_and_cond"));
                            map.put("lucky_draw_date",jsonObject.optString("lucky_draw_date"));
                            map.put("payment_due_date",jsonObject.optString("payment_due_date"));
                            map.put("penality_after_due_date",jsonObject.optString("penality_after_due_date"));
                            map.put("package_name",jsonObject.optString("package_name"));
                            map.put("banner",jsonObject.optString("banner"));

                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);
                        }
                    }
                    else{
                        expListView.setVisibility(View.GONE);
                        imageNoListing.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "No Record Found...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Util.cancelPgDialog(dialog);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Respose", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Error! Please Connect to the internet", Toast.LENGTH_SHORT).show();
                Util.cancelPgDialog(dialog);

            }
        });

        jsonObjReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq);


        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new ListingDetails();
                Bundle bundle=new Bundle();
                try {
                    bundle.putString("data", String.valueOf(jsonArray.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle.putString("name", AllProducts.get(i).get("name"));
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public class Viewholder{
        ImageView imgFav,stars;
        TextView penality_after,payment_due_date,lucky_draw_date,term_and_cond,instal,member,months,packageName,name;
        LinearLayout liner,linerLayoutOffer;

        NetworkImageView banerImg;
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


            convertView=inflater.inflate(R.layout.lsiting_all_kitty,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
            viewholder.packageName=convertView.findViewById(R.id.packageName);
//            viewholder.months=convertView.findViewById(R.id.months);
//            viewholder.member=convertView.findViewById(R.id.member);
//            viewholder.instal=convertView.findViewById(R.id.instal);
//            viewholder.term_and_cond=convertView.findViewById(R.id.term_and_cond);
//            viewholder.lucky_draw_date=convertView.findViewById(R.id.lucky_draw_date);
//            viewholder.payment_due_date=convertView.findViewById(R.id.payment_due_date);
//            viewholder.penality_after=convertView.findViewById(R.id.penality_after);
            viewholder.banerImg=convertView.findViewById(R.id.banerImg);


            viewholder.name.setText(AllProducts.get(position).get("name"));
            viewholder.packageName.setText(AllProducts.get(position).get("package_name"));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewholder.banerImg.setImageUrl(AllProducts.get(position).get("banner").toString().replace(" ","%20"),imageLoader);


//            viewholder.months.setText(AllProducts.get(position).get("no_of_month")+" Months");
//            viewholder.member.setText(AllProducts.get(position).get("no_of_max_members")+" Members");
//            viewholder.instal.setText("₹ "+AllProducts.get(position).get("per_month_installment")+" Per Month");
//            viewholder.term_and_cond.setText(AllProducts.get(position).get("term_and_cond"));
//            viewholder.lucky_draw_date.setText("Lucky Draw Date: "+AllProducts.get(position).get("lucky_draw_date"));
//            viewholder.payment_due_date.setText("Payment Due Date : "+AllProducts.get(position).get("payment_due_date"));
//            viewholder.penality_after.setText("After due Date : "+AllProducts.get(position).get("penality_after_due_date"));


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
