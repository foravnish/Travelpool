package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class ShowUser extends Fragment {


    public ShowUser() {
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
        View  view= inflater.inflate(R.layout.fragment_show_user, container, false);


        getActivity().setTitle("My Users");

        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);
        imageNoListing = (ImageView) view.findViewById(R.id.imageNoListing);
//        fabButton = (FloatingActionButton) view.findViewById(R.id.fab);
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Util.showPgDialog(dialog);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.usersByAgentId+"/"+ MyPrefrences.getUserID(getActivity()), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeUse", response.toString());

                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);
                        imageNoListing.setVisibility(View.GONE);

                        jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            map=new HashMap();
                            map.put("id",jsonObject.optString("id"));
                            map.put("agent_id",jsonObject.optString("agent_id"));
                            map.put("name",jsonObject.optString("name"));
                            map.put("mobile",jsonObject.optString("mobile"));
                            map.put("pan_no",jsonObject.optString("pan_no"));
                            map.put("aadhar_no",jsonObject.optString("aadhar_no"));
                            map.put("email",jsonObject.optString("email"));
                            map.put("address",jsonObject.optString("address"));
                            map.put("city",jsonObject.optString("city"));
                            map.put("state",jsonObject.optString("state"));
                            map.put("pincode", jsonObject.optString("pincode"));
                            map.put("created_date", jsonObject.optString("created_date"));

                            Adapter adapter = new Adapter();
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


//        expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Fragment fragment = new ListingDetails();
//                Bundle bundle=new Bundle();
//                try {
//                    bundle.putString("data", String.valueOf(jsonArray.get(i)));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                bundle.putString("name", AllProducts.get(i).get("name"));
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                fragment.setArguments(bundle);
//                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//            }
//        });


        return view;
    }


    public class Viewholder{
        ImageView imgFav,stars;
        TextView penality_after,email,address,city,pincode,date,mobile,packageName,name;
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


            convertView=inflater.inflate(R.layout.lsiting_all_my_users,parent,false);

            final Viewholder viewholder=new Viewholder();

            viewholder.name=convertView.findViewById(R.id.name);
            viewholder.mobile=convertView.findViewById(R.id.mobile);
            viewholder.date=convertView.findViewById(R.id.date);
            viewholder.pincode=convertView.findViewById(R.id.pincode);
            viewholder.city=convertView.findViewById(R.id.city);
            viewholder.address=convertView.findViewById(R.id.address);
            viewholder.email=convertView.findViewById(R.id.email);


            viewholder.name.setText(AllProducts.get(position).get("name"));
            viewholder.mobile.setText(AllProducts.get(position).get("mobile"));
            viewholder.date.setText(AllProducts.get(position).get("created_date"));
            viewholder.pincode.setText(AllProducts.get(position).get("pincode"));
            viewholder.city.setText(AllProducts.get(position).get("city")+" , "+AllProducts.get(position).get("state"));
            viewholder.address.setText(AllProducts.get(position).get("address"));
            viewholder.email.setText(AllProducts.get(position).get("email"));




            return convertView;
        }
    }





}
