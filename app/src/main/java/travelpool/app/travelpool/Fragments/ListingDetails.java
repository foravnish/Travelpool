package travelpool.app.travelpool.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import travelpool.app.travelpool.Activity.PayNow;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.Util;

import static android.text.Html.fromHtml;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListingDetails extends Fragment {
//

    public ListingDetails() {
        // Required empty public constructor
    }
    TextView penality_after_due_date,payment_due_date,lucky_draw_date,term_and_cond,desreption,instal,instal2,member,months,packageName,kittyName;

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
    TextView EmiPrice,timing2;
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
    TextView viewFull;
    LinearLayout linerLayout;
    boolean flagView=true;
    TextView txtEnd,txtFrom2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listing_details, container, false);

        Log.d("sdsasafsdf",getArguments().getString("data"));
        Log.d("fdfsd",getArguments().getString("name"));


        getActivity().setTitle(getArguments().getString("name"));
        //HomeAct.title.setText(getArguments().getString("company_name").toString());
       // name=view.findViewById(R.id.name);
        packageName=view.findViewById(R.id.packageName);
        months=view.findViewById(R.id.months);
        viewFull=view.findViewById(R.id.viewFull);

        member=view.findViewById(R.id.member);
       // instal=view.findViewById(R.id.instal);
        instal2=view.findViewById(R.id.instal2);
       // term_and_cond=view.findViewById(R.id.term_and_cond);
        desreption=view.findViewById(R.id.desreption);

        //lucky_draw_date=view.findViewById(R.id.lucky_draw_date);
        //payment_due_date=view.findViewById(R.id.payment_due_date);
        //penality_after=view.findViewById(R.id.penality_after);
        joinNow=view.findViewById(R.id.joinNow);
        imageView=view.findViewById(R.id.imageView);
        kittyName=view.findViewById(R.id.kittyName);
        purchased=view.findViewById(R.id.purchased);
        remain=view.findViewById(R.id.remain);
        linearLay1=view.findViewById(R.id.linearLay1);
        linearLay2=view.findViewById(R.id.linearLay2);
        linearLay3=view.findViewById(R.id.linearLay3);
        linearLay4=view.findViewById(R.id.linearLay4);
        EmiPrice=view.findViewById(R.id.EmiPrice);
        txtEnd=view.findViewById(R.id.txtEnd);
        txtFrom2=view.findViewById(R.id.txtFrom2);

        flightDetails=view.findViewById(R.id.flightDetails);
        hotelDetails=view.findViewById(R.id.hotelDetails);
        desc=view.findViewById(R.id.desc);
        tnc=view.findViewById(R.id.tnc);
        linerLayout=view.findViewById(R.id.linerLayout);

        penality_after_due_date=view.findViewById(R.id.penality_after_due_date);
        payment_due_date=view.findViewById(R.id.payment_due_date);
        lucky_draw_date=view.findViewById(R.id.lucky_draw_date);

        viewFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagView==true) {
                    linerLayout.setVisibility(View.VISIBLE);
                    flagView=false;
                    viewFull.setText("HIDE ITINERARY");
                }
                else if (flagView==false){
                    linerLayout.setVisibility(View.GONE);
                    flagView=true;
                    viewFull.setText("VIEW FULL ITINERARY");
                }
            }
        });
        try {
            jsonObject=new JSONObject(getArguments().getString("data"));

           // name.setText(jsonObject.optString("name"));
            kittyName.setText(jsonObject.optString("name"));
            txtEnd.setText(jsonObject.optString("name"));
            txtFrom2.setText(jsonObject.optString("name"));

            months.setText("Total Months: "+jsonObject.optString("no_of_month"));
            member.setText("Total Members: "+jsonObject.optString("no_of_max_members"));
            //instal.setText("₹ "+jsonObject.optString("per_month_installment"));
            EmiPrice.setText("₹ "+jsonObject.optString("per_month_installment"));
            instal2.setText("Per Month ₹ "+jsonObject.optString("per_month_installment"));
           // term_and_cond.setText(jsonObject.optString("term_and_cond"));

            lucky_draw_date.setText("Lucky Draw Date : "+jsonObject.optString("lucky_draw_date"));
            payment_due_date.setText("Payment Due Date: "+jsonObject.optString("payment_due_date"));
            penality_after_due_date.setText("Payment After Date: ₹  "+jsonObject.optString("penality_after_due_date"));

           /// lucky_draw_date.setText("Lucky Draw Date: "+jsonObject.optString("lucky_draw_date"));
            //payment_due_date.setText("Payment Due Date: "+jsonObject.optString("payment_due_date"));
            //penality_after.setText("After Due Date: "+jsonObject.optString("penality_after_due_date"));
            purchased.setText("Joined Entries "+jsonObject.optString("purchased_kitty"));


            int totalMember= Integer.parseInt(jsonObject.optString("no_of_max_members"));
            int joined= Integer.parseInt(jsonObject.optString("purchased_kitty"));

            int remainval=totalMember-joined;
            remain.setText(""+remainval);

            JSONArray jsonArray2=jsonObject.getJSONArray("package_details");
            JSONObject jsonObject2=jsonArray2.getJSONObject(0);
            packageName.setText(jsonObject2.optString("name"));
            desreption.setText(jsonObject2.optString("description"));

            hotelDetails.setText(jsonObject2.optString("hotel_details"));
            flightDetails.setText(jsonObject2.optString("flight_details"));
            desc.setText(jsonObject2.optString("description"));
            tnc.setText(jsonObject2.optString("term_and_cond"));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageView.setImageUrl(jsonObject2.optString("banner").toString().replace(" ","%20"),imageLoader);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), PayNow.class);
                intent.putExtra("data",getArguments().getString("data"));
                intent.putExtra("pay_amount",jsonObject.optString("per_month_installment"));
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




    private void errorDialog(String res) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialogcustom2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.msg_txv);
        text.setText(fromHtml(res));
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();


            }
        });
        dialog.show();

    }














}
