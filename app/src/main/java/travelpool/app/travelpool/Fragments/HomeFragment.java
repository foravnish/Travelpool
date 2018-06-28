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
import android.widget.Button;
import android.widget.ImageView;
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

import travelpool.app.travelpool.R;
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


        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Request.Method.GET,
                "http://bizzcityinfo.com/Api/index.php/main/homeBannerImage?cityId=1", null, new Response.Listener<JSONObject>() {

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

//                            hashMap = new HashMap<>();
//                            hashMap.put("id",jsonObject.optString("id"));
//                            hashMap.put("cat_id",jsonObject.optString("cat_id"));
//                            hashMap.put("eventName",jsonObject.optString("eventName"));
//                            hashMap.put("photo",jsonObject.optString("photo"));
//
//                            viewPager2.setAdapter(mCustomPagerAdapter2);
//                            indicator.setViewPager(viewPager2);
//                            mCustomPagerAdapter2.notifyDataSetChanged();


                            if (jsonObject.optString("cat_id").equalsIgnoreCase("Home")) {
                                AllBaner.add(new Const(jsonObject.optString("id"), jsonObject.optString("cat_id"), jsonObject.optString("subcategory"), jsonObject.optString("image"), jsonObject.optString("meta_keywords"), jsonObject.optString("meta_description"), jsonObject.optString("meta_title"),null,null,null));
                            }

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

            imageView.setImageUrl(AllBaner.get(position).getPhoto().toString().replace(" ","%20"),imageLoader);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AllBaner.get(position).getOrgby().toString().isEmpty() ) {

                        //  Toast.makeText(getActivity(), "blank", Toast.LENGTH_SHORT).show();
                    }
                    else{
//                        Toast.makeText(getActivity(), AllBaner.get(position).getOrgby().toString(), Toast.LENGTH_SHORT).show();

//
//                        Intent intent=new Intent(getActivity(), WebViewOpen.class);
//                        intent.putExtra("link",AllBaner.get(position).getOrgby().toString());
//                        startActivity(intent);

//                        Fragment fragment=new WebViewOpen();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("link",AllBaner.get(position).getOrgby().toString());
//                        FragmentManager manager=getActivity().getSupportFragmentManager();
//                        FragmentTransaction ft=manager.beginTransaction();
//                        fragment.setArguments(bundle);
//                        ft.replace(R.id.content_frame,fragment).addToBackStack(null).commit();
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




}
