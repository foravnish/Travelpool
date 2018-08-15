package travelpool.app.travelpool.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.MyPrefrences;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranscationDetails extends Fragment {



    TextView name,kittyName,packName,orderNo,date,PackId,txnNo,amount;

    public TranscationDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transcation_details, container, false);

        name=view.findViewById(R.id.name);
        kittyName=view.findViewById(R.id.kittyName);
        packName=view.findViewById(R.id.packName);
        orderNo=view.findViewById(R.id.orderNo);
        txnNo=view.findViewById(R.id.txnNo);
        PackId=view.findViewById(R.id.PackId);
        amount=view.findViewById(R.id.amount);
        date=view.findViewById(R.id.date);
        Log.d("fdbgdfdfhdfhdf",getArguments().getString("jsonArray"));



        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("jsonArray"));

            name.setText(jsonObject.optString("user_name").toUpperCase());
            kittyName.setText(jsonObject.optString("kitty_name"));
            packName.setText(jsonObject.optString("package_name"));
            orderNo.setText(jsonObject.optString("package_id"));
            txnNo.setText(jsonObject.optString("transaction_id"));
            PackId.setText(jsonObject.optString("package_id"));
            amount.setText("₹ "+jsonObject.optString("pay_amount"));

            String year=jsonObject.optString("purchase_date").substring(0,4);
            String month=jsonObject.optString("purchase_date").substring(5,7);
            String day=jsonObject.optString("purchase_date").substring(8,10);
            date.setText(day+"-"+month+"-"+year);




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

}
