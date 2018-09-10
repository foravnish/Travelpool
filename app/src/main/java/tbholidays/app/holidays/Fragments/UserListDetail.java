package tbholidays.app.holidays.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tbholidays.app.holidays.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListDetail extends Fragment {


    TextView aadharNo,panNo,address,creadedDate,userId,email,mobile,name;
    public UserListDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_list_detail, container, false);

        getActivity().setTitle("User Detail");
        Log.d("fdgdfgdfgdfg",getArguments().getString("jsonArray"));

        name=view.findViewById(R.id.name);
        mobile=view.findViewById(R.id.mobile);
        email=view.findViewById(R.id.email);
        userId=view.findViewById(R.id.userId);
        creadedDate=view.findViewById(R.id.creadedDate);
        address=view.findViewById(R.id.address);
        panNo=view.findViewById(R.id.panNo);
        aadharNo=view.findViewById(R.id.aadharNo);

        try {
            JSONObject jsonObject=new JSONObject(getArguments().getString("jsonArray"));

            name.setText(jsonObject.optString("name").toUpperCase());
            mobile.setText(jsonObject.optString("mobile"));
            email.setText(jsonObject.optString("email"));
            userId.setText(jsonObject.optString("id"));
            address.setText(jsonObject.optString("address")+", "+jsonObject.optString("city")
                    +", "+jsonObject.optString("state")+", "+jsonObject.optString("pincode  "));
            panNo.setText(jsonObject.optString("pan_no"));
            aadharNo.setText(jsonObject.optString("aadhar_no"));

            String year=jsonObject.optString("created_date").substring(0,4);
            String month=jsonObject.optString("created_date").substring(5,7);
            String day=jsonObject.optString("created_date").substring(8,10);
            creadedDate.setText(day+"-"+month+"-"+year);




        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  view;
    }

}
