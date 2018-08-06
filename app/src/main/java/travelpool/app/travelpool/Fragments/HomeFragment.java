package travelpool.app.travelpool.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adefruandta.spinningwheel.SpinningWheelView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import travelpool.app.travelpool.Activity.WebViewOpen;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.Const;
import travelpool.app.travelpool.Utils.Util;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }
    SpinningWheelView wheelView;
    Dialog dialog, dialog4;
    ViewPager viewPager2;
    List<Const> AllBaner   = new ArrayList<>();
    CustomPagerAdapter2 mCustomPagerAdapter2;
    CirclePageIndicator  indicator2;
    int currentPage = 0;


    List<HashMap<String,String>> AllProducts ;
    GridView expListView;
    HashMap<String,String> map;
    JSONObject jsonObject1;
    //ImageView imageNoListing;
    JSONArray jsonArray;

    String[] value={"Abc","Xyx","ijk","lmn","poi","uud","jis","yys","usu","puf","etf"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        wheelView = (SpinningWheelView) view.findViewById(R.id.wheel);
        Button play = (Button) view.findViewById(R.id.play);

        // Can be array string or list of object
        wheelView.setItems(Arrays.asList(value));

        viewPager2 = (ViewPager) view.findViewById(R.id.slider2);
        indicator2 = (CirclePageIndicator)view.findViewById(R.id.indicat2);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
          Util.showPgDialog(dialog);

        AllProducts = new ArrayList<>();
        expListView = (GridView) view.findViewById(R.id.lvExp);


          getActivity().setTitle("Travel Pool");
        // Set listener for rotation event
        wheelView.setOnRotationListener(new SpinningWheelView.OnRotationListener<String>() {
            // Call once when start rotation
            @Override
            public void onRotation() {
                Log.d("XXXX", "On Rotation");

            }

            // Call once when stop rotation
            @Override
            public void onStopRotation(String item) {
                Log.d("XXXX", "On Rotation");
                Log.d("sdfsdfgsdfgds",item);
//                wheelView.rotate(50, 5000, 50);

            }

        });

        // If true: user can rotate by touch
        // If false: user can not rotate by touch
        wheelView.setEnabled(false);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Api.ViewKitty, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeKitty", response.toString());

                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){

                        expListView.setVisibility(View.VISIBLE);

                        jsonArray=response.getJSONArray("message");
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
                            map.put("banner", jsonObject.optString("banner"));
                            map.put("image", jsonObject.optString("image"));
                            map.put("purchased_kitty", jsonObject.optString("purchased_kitty"));
                            JSONArray jsonArray2=jsonObject.getJSONArray("package_details");


                            for (int j=0;j<jsonArray2.length();j++) {

                                JSONObject jsonObject2=jsonArray2.getJSONObject(j);

                                map.put("package_name", jsonObject2.optString("name"));
                                map.put("description", jsonObject2.optString("description"));


                                Adapter adapter = new Adapter();
                                expListView.setAdapter(adapter);
                                AllProducts.add(map);
                            }
                        }
                    }
                    else{
                        expListView.setVisibility(View.GONE);
                       // imageNoListing.setVisibility(View.VISIBLE);
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



        // Banner
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
                Api.bannerList, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeBaner", response.toString());
                Util.cancelPgDialog(dialog);
                try {
                    // Parsing json object response
                    // response will be a json object
//                    String name = response.getString("name");
                    HashMap<String,String> hashMap = null;
                    if (response.getString("status").equalsIgnoreCase("success")){
                        AllBaner.clear();
                        JSONArray jsonArray=response.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                                AllBaner.add(new Const(jsonObject.optString("id"), jsonObject.optString("image"), jsonObject.optString("url"), null, null, null, null,null,null,null));


                            mCustomPagerAdapter2=new CustomPagerAdapter2(getActivity());
                            viewPager2.setAdapter(mCustomPagerAdapter2);
                            indicator2.setViewPager(viewPager2);
                            mCustomPagerAdapter2.notifyDataSetChanged();



                        }
                        final Handler handler = new Handler();

                        final Runnable update = new Runnable() {

                            public void run() {
                                if (currentPage == AllBaner.size()) {
                                    currentPage = 0;
                                }
                                viewPager2.setCurrentItem(currentPage++);
                            }
                        };
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(update);
                            }
                        }, 100, 5000);


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
                // hide the progress dialog
                Util.cancelPgDialog(dialog);

            }
        });

        // Adding request to request queue
        jsonObjReq2.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(jsonObjReq2);
        ///// End baner APi



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheelView.rotate(50, 5000, 50);
            }
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {


                        Button Yes_action,No_action;
                        TextView heading;
                        dialog4 = new Dialog(getActivity());
                        dialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog4.setContentView(R.layout.update_state1);

                        Yes_action=(Button)dialog4.findViewById(R.id.Yes_action);
                        No_action=(Button)dialog4.findViewById(R.id.No_action);
                        heading=(TextView)dialog4.findViewById(R.id.heading);


                        heading.setText("Are you sure you want to exit?");
                        Yes_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //System.exit(0);
                                //getActivity().finish();
                                getActivity().finishAffinity();

                            }
                        });

                        No_action.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog4.dismiss();
                            }
                        });
                        dialog4.show();
//

                        //Toast.makeText(getActivity(), "back", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        return view;

    }

    class CustomPagerAdapter2 extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter2(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return AllBaner.size();
        }



        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }



        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);

            NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.imageView);




            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            imageView.setImageUrl(AllBaner.get(position).getCatid().toString().replace(" ","%20"),imageLoader);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AllBaner.get(position).getEventName().toString().isEmpty() ) {

                        //  Toast.makeText(getActivity(), "blank", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), AllBaner.get(position).getOrgby().toString(), Toast.LENGTH_SHORT).show();


                        Intent intent=new Intent(getActivity(), WebViewOpen.class);
                        intent.putExtra("link",AllBaner.get(position).getEventName().toString());
                        startActivity(intent);


                    }
                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    //// end baner





    public class Viewholder{
        ImageView imgFav,stars;
        TextView totalMember,EmiPrice,termCondition,purchased,desreption,NoOfMember,months,packageName,name;
        LinearLayout liner,linerLayoutOffer;

        NetworkImageView banerImg,banerImg2;
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
            viewholder.EmiPrice=convertView.findViewById(R.id.EmiPrice);
            viewholder.totalMember=convertView.findViewById(R.id.totalMember);
            viewholder.termCondition=convertView.findViewById(R.id.termCondition);
            viewholder.purchased=convertView.findViewById(R.id.purchased);
            viewholder.desreption=convertView.findViewById(R.id.desreption);
            viewholder.NoOfMember=convertView.findViewById(R.id.NoOfMember);
//            viewholder.months=convertView.findViewById(R.id.months);
//            viewholder.member=convertView.findViewById(R.id.member);
//            viewholder.instal=convertView.findViewById(R.id.instal);
//            viewholder.term_and_cond=convertView.findViewById(R.id.term_and_cond);
//            viewholder.lucky_draw_date=convertView.findViewById(R.id.lucky_draw_date);
//            viewholder.payment_due_date=convertView.findViewById(R.id.payment_due_date);
//            viewholder.penality_after=convertView.findViewById(R.id.penality_after);
            viewholder.banerImg=convertView.findViewById(R.id.banerImg);
            viewholder.banerImg2=convertView.findViewById(R.id.banerImg2);


            viewholder.name.setText(AllProducts.get(position).get("name"));
            viewholder.packageName.setText(AllProducts.get(position).get("package_name"));
            viewholder.EmiPrice.setText("Per Months ₹ "+AllProducts.get(position).get("per_month_installment"));
            viewholder.totalMember.setText("Months of Kitty "+AllProducts.get(position).get("no_of_month"));
            viewholder.termCondition.setText(AllProducts.get(position).get("term_and_cond"));
            viewholder.purchased.setText("Member Purchased "+AllProducts.get(position).get("purchased_kitty"));
            viewholder.desreption.setText(AllProducts.get(position).get("description"));
            viewholder.NoOfMember.setText("Total Members "+AllProducts.get(position).get("no_of_max_members"));

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            viewholder.banerImg.setImageUrl(AllProducts.get(position).get("banner").toString().replace(" ","%20"),imageLoader);
            viewholder.banerImg2.setImageUrl(AllProducts.get(position).get("image").toString().replace(" ","%20"),imageLoader);



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
