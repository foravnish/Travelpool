package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

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
    JSONArray jsonArray;

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


        ReceiveData();

        return view;

    }


    private void ReceiveData() {

        Util.showPgDialog(dialog);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.myKitty+"/"+MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("MyKittyResponse", jsonObject.toString());

                Util.cancelPgDialog(dialog);
                try {
                   // JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);
                        imageNoListing.setVisibility(View.GONE);

                        jsonArray=jsonObject.getJSONArray("message");


                        for (int i=0;i<jsonArray.length();i++){
                            map=new HashMap();
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            JSONArray jsonArrayKitty=jsonObject1.getJSONArray("kitty_details");

                                JSONObject jsonObjectKitty = jsonArrayKitty.getJSONObject(0);

                                map.put("name", jsonObjectKitty.optString("name"));
                                map.put("tc", jsonObjectKitty.optString("term_and_cond"));
                                map.put("p_m_i", jsonObjectKitty.optString("per_month_installment"));
                                map.put("payment_due_date", jsonObjectKitty.optString("payment_due_date"));
                                map.put("lucky_d_d", jsonObjectKitty.optString("lucky_draw_date"));
                                map.put("no_of_max_members", jsonObjectKitty.optString("no_of_max_members"));

                            map.put("id",jsonObject1.optString("id"));
                            map.put("kitty_id",jsonObject1.optString("kitty_id"));
                            map.put("this_month_renual",jsonObject1.optString("this_month_renual"));

                            JSONArray jsonArrayPack=jsonObject1.getJSONArray("package_details");
                            JSONObject jsonObjectPack=jsonArrayPack.getJSONObject(0);

                            map.put("package_name",jsonObjectPack.optString("name"));
                            map.put("tc",jsonObjectPack.optString("term_and_cond"));
                            map.put("banner",jsonObjectPack.optString("banner"));
                            map.put("image",jsonObjectPack.optString("image"));



                            Adapter adapter=new Adapter();
                            expListView.setAdapter(adapter);
                            AllProducts.add(map);

                        }
                    }
                    else{

                        expListView.setVisibility(View.GONE);
                        imageNoListing.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ResposeConnect", "Error: " + error.getMessage());
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
                Fragment fragment = new MyKittyDetial();
                Bundle bundle=new Bundle();
                try {
                    bundle.putString("data", String.valueOf(jsonArray.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FragmentManager manager = getFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });





//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                Api.myKitty+"/"+MyPrefrences.getUserID(getActivity()), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Util.cancelPgDialog(dialog);
//                Log.e("MyKittyResponse", "Response: " + response);
//
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    if (jsonObject.getString("status").equalsIgnoreCase("success")){
//
//                        expListView.setVisibility(View.VISIBLE);
//                        imageNoListing.setVisibility(View.GONE);
//
//                        final JSONArray jsonArray=jsonObject.getJSONArray("message");
//
//                        map=new HashMap();
//
//                        for (int i=0;i<jsonArray.length();i++){
//                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
//
//                            JSONArray jsonArrayKitty=jsonObject1.getJSONArray("kitty_details");
//
//                                JSONObject jsonObjectKitty=jsonArrayKitty.getJSONObject(0);
//
//                                map.put("name",jsonObjectKitty.optString("name"));
//                                map.put("tc",jsonObjectKitty.optString("term_and_cond"));
//
//                                map.put("p_m_i",jsonObjectKitty.optString("per_month_installment"));
//                                map.put("m_m",jsonObjectKitty.optString("payment_due_date"));
//                                map.put("lucky_d_d",jsonObjectKitty.optString("lucky_draw_date"));
//
//
//
//                            map.put("id",jsonObject1.optString("id"));
//                            map.put("kitty_id",jsonObject1.optString("kitty_id"));
//
//                            JSONArray jsonArrayPack=jsonObject1.getJSONArray("package_details");
//
//                                JSONObject jsonObjectPack=jsonArrayPack.getJSONObject(0);
//
//                                map.put("package_name",jsonObjectPack.optString("name"));
//                                map.put("tc",jsonObjectPack.optString("term_and_cond"));
//                                map.put("banner",jsonObjectPack.optString("banner"));
//
//
//
//
//
//
//                            Adapter adapter=new Adapter();
//                            expListView.setAdapter(adapter);
//                            AllProducts.add(map);
//
//                        }
//                    }
//                    else{
//
//                        expListView.setVisibility(View.GONE);
//                        imageNoListing.setVisibility(View.VISIBLE);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Util.cancelPgDialog(dialog);
//                Log.e("fdgdfgdfgd", "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),"Please Connect to the Internet or Wrong Password", Toast.LENGTH_LONG).show();
//            }
//        }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Log.e("fgdfgdfgdf","Inside getParams");
//
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<>();
//                params.put("userid",  MyPrefrences.getUserID(getActivity()));
//              //  params.put("kitty_id",  id.toString());
//
//                return params;
//            }
//
//        };
//        // Adding request to request queue
//        queue.add(strReq);


    }


    public class Viewholder{
        ImageView imgFav,stars;
        TextView packageName,name,term_and_cond,lucky_draw_date,instal,dueDate,totalMember,joinNow;
        LinearLayout liner,linerLayoutOffer;

        NetworkImageView imgaeView;
        CardView cardView;
        ImageView callNow1;
        //  ShimmerTextView offersText;
//        Shimmer shimmer;
        ImageView img1,img2,img3,img4,img5;

        LinearLayout footer_layout;
        NetworkImageView banerImg,banerImg2;

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
            viewholder.packageName=convertView.findViewById(R.id.packageName);
//            viewholder.term_and_cond=convertView.findViewById(R.id.term_and_cond);
            viewholder.dueDate=convertView.findViewById(R.id.dueDate);
            viewholder.lucky_draw_date=convertView.findViewById(R.id.lucky_draw_date);
            viewholder.instal=convertView.findViewById(R.id.instal);
            viewholder.banerImg=convertView.findViewById(R.id.banerImg);
            viewholder.banerImg2=convertView.findViewById(R.id.banerImg2);
            viewholder.totalMember=convertView.findViewById(R.id.totalMember);
            viewholder.joinNow=convertView.findViewById(R.id.joinNow);



            viewholder.name.setText(AllProducts.get(position).get("name"));
            viewholder.packageName.setText(AllProducts.get(position).get("package_name"));
//            viewholder.term_and_cond.setText(AllProducts.get(position).get("tc"));
            viewholder.dueDate.setText("Due Date :"+AllProducts.get(position).get("payment_due_date"));
            viewholder.totalMember.setText("Total Members "+AllProducts.get(position).get("no_of_max_members"));
            viewholder.lucky_draw_date.setText("Lucky Draw Date: "+AllProducts.get(position).get("lucky_d_d"));
            viewholder.instal.setText(" Per Month ₹ : "+AllProducts.get(position).get("p_m_i"));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewholder.banerImg.setImageUrl(""+AllProducts.get(position).get("banner").toString().replace(" ","%20"),imageLoader);
            viewholder.banerImg2.setImageUrl(""+AllProducts.get(position).get("image").toString().replace(" ","%20"),imageLoader);

            if (AllProducts.get(position).get("this_month_renual").equals("Yes")){
                viewholder.joinNow.setText("Renewed");
            }
            else{
                viewholder.joinNow.setText("Renew Kitty");
            }

//            viewholder.joinNow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(getActivity(), PayNow.class);
//                    intent.putExtra("data",getArguments().getString("data"));
//                    startActivity(intent);
//                }
//            });
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
