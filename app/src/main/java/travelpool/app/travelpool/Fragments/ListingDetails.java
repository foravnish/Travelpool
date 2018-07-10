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
    TextView penality_after,payment_due_date,lucky_draw_date,term_and_cond,instal,member,months,packageName,name;

    TextView closingday,timing,min_distance,min_order,textPmt,isTiming;
    Button joinNow;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listing_details, container, false);

        Log.d("sdsasafsdf",getArguments().getString("data"));
        Log.d("fdfsd",getArguments().getString("name"));


        getActivity().setTitle(getArguments().getString("name"));
        //HomeAct.title.setText(getArguments().getString("company_name").toString());
        name=view.findViewById(R.id.name);
        packageName=view.findViewById(R.id.packageName);
        months=view.findViewById(R.id.months);

        member=view.findViewById(R.id.member);
        instal=view.findViewById(R.id.instal);
        term_and_cond=view.findViewById(R.id.term_and_cond);
        lucky_draw_date=view.findViewById(R.id.lucky_draw_date);
        payment_due_date=view.findViewById(R.id.payment_due_date);
        penality_after=view.findViewById(R.id.penality_after);
        joinNow=view.findViewById(R.id.joinNow);


        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("data"));

            name.setText(jsonObject.optString("name"));
            packageName.setText(jsonObject.optString("package_name"));
            months.setText("Total Months: "+jsonObject.optString("no_of_month"));
            member.setText("Total Members: "+jsonObject.optString("no_of_max_members"));
            instal.setText("Per Month ₹ : "+jsonObject.optString("per_month_installment"));
            term_and_cond.setText(jsonObject.optString("term_and_cond"));
            lucky_draw_date.setText("Lucky Draw Date: "+jsonObject.optString("lucky_draw_date"));
            payment_due_date.setText("Payment Due Date: "+jsonObject.optString("payment_due_date"));
            penality_after.setText("After Due Date: "+jsonObject.optString("penality_after_due_date"));

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
