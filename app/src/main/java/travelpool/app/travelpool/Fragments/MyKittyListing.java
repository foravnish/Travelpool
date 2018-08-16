package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import travelpool.app.travelpool.Activity.PayNow;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.AppController;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyKittyListing extends Fragment {


    public MyKittyListing() {
        // Required empty public constructor
    }


    TextView penality_after,payment_due_date,lucky_draw_date,term_and_cond,desreption,instal,instal2,member,months,packageName,kittyName;

    TextView purchased,remain,hotelDetails,flightDetails,desc,tnc;
    TextView joinNow;
    List<String> subCat =new ArrayList<>();
    //MaterialRatingBar rating;
    ListView listview;
    ImageView imgFav,stars;
    List<HashMap<String,String>> list=new ArrayList<>();
    Boolean flag=false;
    Dialog dialog,dialog4;
    LinearLayout servicesLay;
    NetworkImageView imageView;
    String val="1";
    ViewPager viewPager2;
    // CustomPagerAdapter2 mCustomPagerAdapter2;
    List<HashMap<String,String>> AllBaner;
    ListView  lvExp;
    List<HashMap<String,String>> AllProducts ;
    List<HashMap<String,String>> AllProducts2 ;
    List<HashMap<String,String>> AllProducts3 ;

    RecyclerView mRecyclerView,mRecyclerView3;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager2;
    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapter3;
    RelativeLayout relativeLayout1;
    LinearLayout closingDay,modePaymnt,minOrder,linerMode;
    TextView reviewShow;
    LinearLayout linerlay;
    TextView timeTv1,timing2;
    ImageView shareDetail;
    NetworkImageView flagIcon;
    String[] values;
    ScrollView scroll;
    LinearLayout linearLay1,linearLay2,linearLay3,linearLay4;
    boolean flag1=true;
    boolean flag2=true;
    boolean flag3=true;
    boolean flag4=true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_kitty_listing, container, false);

        Log.d("sdsasafsdf",getArguments().getString("data"));
//        Log.d("fdfsd",getArguments().getString("name"));


        //HomeAct.title.setText(getArguments().getString("company_name").toString());
        // name=view.findViewById(R.id.name);
        packageName=view.findViewById(R.id.packageName);
        months=view.findViewById(R.id.months);

        member=view.findViewById(R.id.member);
        instal=view.findViewById(R.id.instal);
        instal2=view.findViewById(R.id.instal2);
        term_and_cond=view.findViewById(R.id.term_and_cond);
        desreption=view.findViewById(R.id.desreption);
        lucky_draw_date=view.findViewById(R.id.lucky_draw_date);
        payment_due_date=view.findViewById(R.id.payment_due_date);
        penality_after=view.findViewById(R.id.penality_after);
        joinNow=view.findViewById(R.id.joinNow);
        imageView=view.findViewById(R.id.imageView);
        kittyName=view.findViewById(R.id.kittyName);
        purchased=view.findViewById(R.id.purchased);
        remain=view.findViewById(R.id.remain);
        linearLay1=view.findViewById(R.id.linearLay1);
        linearLay2=view.findViewById(R.id.linearLay2);
        linearLay3=view.findViewById(R.id.linearLay3);
        linearLay4=view.findViewById(R.id.linearLay4);

        flightDetails=view.findViewById(R.id.flightDetails);
        hotelDetails=view.findViewById(R.id.hotelDetails);
        desc=view.findViewById(R.id.desc);
        tnc=view.findViewById(R.id.tnc);

        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("data"));

            getActivity().setTitle(jsonObject.optString("kitty_name"));

            // name.setText(jsonObject.optString("name"));
            kittyName.setText(jsonObject.optString("kitty_name"));


            JSONArray jsonArrayKitty=jsonObject.getJSONArray("kitty_details");

            JSONObject jsonObjectKitty=jsonArrayKitty.getJSONObject(0);


            months.setText("Total Months: "+jsonObjectKitty.optString("no_of_month"));
            member.setText("Total Members: "+jsonObjectKitty.optString("no_of_max_members"));
            instal.setText("₹ "+jsonObjectKitty.optString("per_month_installment"));
            instal2.setText("Per Month ₹ "+jsonObjectKitty.optString("per_month_installment"));
            term_and_cond.setText(jsonObjectKitty.optString("term_and_cond"));
            lucky_draw_date.setText("Lucky Draw Date: "+jsonObjectKitty.optString("lucky_draw_date"));
            payment_due_date.setText("Payment Due Date: "+jsonObjectKitty.optString("payment_due_date"));
            penality_after.setText("After Due Date: "+jsonObjectKitty.optString("penality_after_due_date"));
            purchased.setText("Joined Entries "+jsonObject.optString("purchased_kitty"));
            remain.setText("45");

            JSONArray jsonArray2=jsonObject.getJSONArray("package_details");
            JSONObject jsonObject2=jsonArray2.getJSONObject(0);
            desreption.setText(jsonObject2.optString("description"));
            packageName.setText(jsonObject.optString("package_name"));

            hotelDetails.setText(jsonObject2.optString("hotel_details"));
            flightDetails.setText(jsonObject2.optString("flight_details"));
            desc.setText(jsonObject2.optString("description"));
            tnc.setText(jsonObject2.optString("term_and_cond"));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject2.optString("banner").toString().replace(" ","%20"),imageLoader);


            if (jsonObject.optString("this_month_renual").equals("Yes")){
                joinNow.setText("Renewed");
            }
            else{
                joinNow.setText("Renew Kitty");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PayNow.class);
                intent.putExtra("data",getArguments().getString("data"));
                startActivity(intent);
            }
        });



        linearLay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag1==true) {
                    ViewGroup.LayoutParams params = linearLay1.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    linearLay1.setLayoutParams(params);

                    flag1=false;
                }
                else if (flag1==false){

                    ViewGroup.LayoutParams params = linearLay1.getLayoutParams();
                    params.height = 80;
                    linearLay1.setLayoutParams(params);

                    flag1=true;
                }
//                linearLay1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            }
        });

        linearLay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag2==true) {
                    ViewGroup.LayoutParams params = linearLay2.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    linearLay2.setLayoutParams(params);

                    flag2=false;
                }
                else if (flag2==false){

                    ViewGroup.LayoutParams params = linearLay2.getLayoutParams();
                    params.height = 80;
                    linearLay2.setLayoutParams(params);

                    flag2=true;
                }
//                linearLay1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            }
        });


        linearLay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag3==true) {
                    ViewGroup.LayoutParams params = linearLay3.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    linearLay3.setLayoutParams(params);

                    flag3=false;
                }
                else if (flag3==false){

                    ViewGroup.LayoutParams params = linearLay3.getLayoutParams();
                    params.height = 80;
                    linearLay3.setLayoutParams(params);

                    flag3=true;
                }
//                linearLay1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            }
        });


        linearLay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag4==true) {
                    ViewGroup.LayoutParams params = linearLay4.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    linearLay4.setLayoutParams(params);

                    flag4=false;
                }
                else if (flag4==false){

                    ViewGroup.LayoutParams params = linearLay4.getLayoutParams();
                    params.height = 80;
                    linearLay4.setLayoutParams(params);

                    flag4=true;
                }
//                linearLay1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            }
        });



        return view;
    }

}
