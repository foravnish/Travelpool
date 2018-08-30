package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import travelpool.app.travelpool.Activity.PayNow;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyKittyDetial extends Fragment {


    public MyKittyDetial() {
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
    TextView luckyWinner,timing2;
    ImageView shareDetail;
    NetworkImageView flagIcon;
    String[] values;
    ScrollView scroll;
    LinearLayout linearLay1,linearLay2,linearLay3,linearLay4;
    boolean flag1=true;
    boolean flag2=true;
    boolean flag3=true;
    boolean flag4=true;
    JSONObject jsonObject;
    String yy,mm,dd;
    public static long seconds;
    String hr,min;
    String first,second;
    String luckyDrawWinner="Show Lucky Winner";
    JSONObject jsonObjectKitty;
    TextView txtEnd,txtFrom2;

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
        luckyWinner=view.findViewById(R.id.luckyWinner);
        txtEnd=view.findViewById(R.id.txtEnd);
        txtFrom2=view.findViewById(R.id.txtFrom2);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        try {
            jsonObject=new JSONObject(getArguments().getString("data"));

            getActivity().setTitle(jsonObject.optString("kitty_name"));

            // name.setText(jsonObject.optString("name"));
            kittyName.setText(jsonObject.optString("kitty_name"));
            txtEnd.setText(jsonObject.optString("kitty_name"));
            txtFrom2.setText(jsonObject.optString("kitty_name"));


            JSONArray jsonArrayKitty=jsonObject.getJSONArray("kitty_details");

            jsonObjectKitty=jsonArrayKitty.getJSONObject(0);


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

            final Calendar c = Calendar.getInstance();
            yy = String.valueOf(c.get(Calendar.YEAR));
            mm = String.valueOf(c.get(Calendar.MONTH));
            dd = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

            Log.d("fgdfgdfgdfgdf",dd);


            if (c.get(Calendar.HOUR_OF_DAY)<=9){
                hr= String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                hr="0"+hr;
            }
            else{
                hr= String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            }
            if (c.get(Calendar.MINUTE)<=9){
                min= String.valueOf(c.get(Calendar.MINUTE));
                min="0"+min;
            }
            else{
                min= String.valueOf(c.get(Calendar.MINUTE));
            }
            Log.d("hrhrrhrhrhrhrhrhrh",hr);
            Log.d("hrhrrhrhrhrhrhrhrh",min);



            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject2.optString("banner").toString().replace(" ","%20"),imageLoader);


            if (jsonObject.optString("this_month_renual").equals("Yes")){
                joinNow.setText("Renewed");
            }
            else{
                joinNow.setText("Renew Kitty");
            }


            String currentString = jsonObjectKitty.optString("lucky_draw_time");

            try {
                StringTokenizer tokens = new StringTokenizer(currentString, ":");
                first = tokens.nextToken();// this will contain "Fruit"
                second = tokens.nextToken();// this will contain " they taste good"
                Log.d("dfsdfsdfsdfsdfsdfsd",first);
                Log.d("dfsdfsdfsdfsdfsdfsd",second);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonObjectKitty.optString("lucky_draw_date").equals(dd)){
                luckyWinner.setVisibility(View.VISIBLE);
                Log.d("fdgdfgdfgdfgdf","true");
            }
            else{
                luckyWinner.setVisibility(View.GONE);
                Log.d("fdgdfgdfgdfgdf","false");
            }

            Timer updateTimer = new Timer();
            updateTimer.schedule(new TimerTask()
            {
                public void run()
                {
                    try
                    {

                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        Date date1 = format.parse(first+":"+second+":"+"00");///// Time of api
                        Date date2 = format.parse(hr+":"+min+":"+"00");  //// Time of Local
                        long mills = date1.getTime() - date2.getTime();
                        Log.v("Data1", ""+date1.getTime());
                        Log.v("Data2", ""+date2.getTime());
                        int hours = (int) (mills/(1000 * 60 * 60));
                        int mins = (int) (mills/(1000*60)) % 60;

                        seconds=(hours*60+mins)*60;

                        String diff = hours + ":" + mins; // updated value every1 second

                        Log.d("dfdsgfsdgfsdgdgfdgds", String.valueOf(seconds));
                        Log.d("trertyeryety", String.valueOf(diff));


                        // viewholder.luckyWinner.setText((hours*60+mins)*60+"");
//                        luckyWinner.setText(diff);



                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }, 0, 1000);

            Log.d("fdgdfgsdgdfgdfgdfgdfgd", String.valueOf(seconds));

            new CountDownTimer(seconds*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    luckyWinner.setText("Lucky Draw Remaining Time: " + millisUntilFinished / 1000);
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    luckyWinner.setText(luckyDrawWinner);
                }

            }.start();





            luckyWinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (luckyWinner.getText().toString().equals(luckyDrawWinner)){
                        showLuckyWinner(jsonObjectKitty.optString("id"));
                        Log.d("sdfsdfsdgdfgdf","true");
                    }
                    else{
                        Log.d("sdfsdfsdgdfgdf","false");
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (jsonObject.optString("this_month_renual").equals("Yes")){
                    Util.errorDialog(getActivity(),"You have Renewed of current month this Kitty.");
                }
                else{

                    Intent intent=new Intent(getActivity(), PayNow.class);
                    intent.putExtra("data",getArguments().getString("data"));
                    intent.putExtra("pay_amount",jsonObject.optString("pay_amount"));

                    startActivity(intent);
                }


//                Intent intent=new Intent(getActivity(), PayNow.class);
//                intent.putExtra("data",getArguments().getString("data"));
//                intent.putExtra("pay_amount",jsonObject.optString("pay_amount"));
//
//                startActivity(intent);

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

    private void showLuckyWinner(String id) {

        Util.showPgDialog(dialog);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Api.kittyWinner+"/"+id , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResposeTranscation", response.toString());

                Util.cancelPgDialog(dialog);
                try {

                    if (response.getString("status").equalsIgnoreCase("success")){


                        JSONArray jsonArray=response.getJSONArray("message");
                        JSONObject jsonObject1 = jsonArray.optJSONObject(0);

                        Util.errorDialog(getActivity(),"This Month Lucky Winner : "+jsonObject1.optString("user_name").toUpperCase());

                    }
                    else{

                        Toast.makeText(getActivity(),response.getString("message") , Toast.LENGTH_SHORT).show();
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

    }



}
