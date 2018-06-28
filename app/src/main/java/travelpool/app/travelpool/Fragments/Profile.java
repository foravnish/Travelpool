package travelpool.app.travelpool.Fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gelitenight.waveview.library.WaveView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import travelpool.app.travelpool.Activity.HomeAct;
import travelpool.app.travelpool.Activity.Login;
import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.Api;
import travelpool.app.travelpool.Utils.AppController;
import travelpool.app.travelpool.Utils.MyPrefrences;
import travelpool.app.travelpool.Utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    public Profile() {
        // Required empty public constructor
    }

    TextView tve_name,tve_mobile,tve_user_id,tve_email,tve_addres,tve_city,tve_state,tve_pincode;
    Button changeProfile;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        changeProfile=view.findViewById(R.id.changeProfile);

        tve_name=view.findViewById(R.id.tve_name);
        tve_mobile=view.findViewById(R.id.tve_mobile);
        tve_user_id=view.findViewById(R.id.tve_user_id);
        tve_email=view.findViewById(R.id.tve_email);
        tve_addres=view.findViewById(R.id.tve_addres);
        tve_city=view.findViewById(R.id.tve_city);
        tve_state=view.findViewById(R.id.tve_state);
        tve_pincode=view.findViewById(R.id.tve_pincode);

        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        getActivity().setTitle("My Profile");

        final WaveView waveView = (WaveView)view. findViewById(R.id.wave);
        waveView.setRotation(180);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveColor(
                Color.parseColor("#FFF49B59"),
                Color.parseColor("#FFF49B59"));
        WaveHelper mWaveHelper;
        mWaveHelper = new WaveHelper(waveView);
        mWaveHelper.start();


        getProfile();
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileEdit();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
        return  view;
    }

    private void getProfile() {

        Util.showPgDialog(dialog);

        StringRequest postRequest = new StringRequest(Request.Method.POST, Api.profile,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Responseprofile", response);
                        Util.cancelPgDialog(dialog);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")){

                                JSONArray jsonArray=jsonObject.getJSONArray("login");
                                for (int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);


                                    tve_name.setText(jsonObject1.optString("name").toUpperCase());
                                    tve_mobile.setText(jsonObject1.optString("mobile").toString());
                                    tve_user_id.setText(jsonObject1.optString("id").toString());
                                    tve_email.setText(jsonObject1.optString("email").toString());
                                    tve_addres.setText(jsonObject1.optString("address").toString());
                                    tve_city.setText(jsonObject1.optString("city").toString());
                                    tve_state.setText(jsonObject1.optString("state").toString());
                                    tve_pincode.setText(jsonObject1.optString("pincode").toString());


                                }

                            }
                            else{
                                Toast.makeText(getActivity(),jsonObject.getString("msg") , Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getActivity(), "Error! Please connect to the Internet.", Toast.LENGTH_SHORT).show();
                        Util.cancelPgDialog(dialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", MyPrefrences.getUserID(getActivity()));


                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(27000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(false);

        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public class WaveHelper {
        private WaveView mWaveView;

        private AnimatorSet mAnimatorSet;

        public WaveHelper(WaveView waveView) {
            mWaveView = waveView;
            initAnimation();
        }

        public void start() {
            mWaveView.setShowWave(true);
            if (mAnimatorSet != null) {
                mAnimatorSet.start();
            }
        }

        private void initAnimation() {
            List<Animator> animators = new ArrayList<>();

            // horizontal animation.
            // wave waves infinitely.
            ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                    mWaveView, "waveShiftRatio", 0f, 1f);
            waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
            waveShiftAnim.setDuration(1000);
            waveShiftAnim.setInterpolator(new LinearInterpolator());
            animators.add(waveShiftAnim);

            // vertical animation.
            // water level increases from 0 to center of WaveView
            ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                    mWaveView, "waterLevelRatio", 0.8f, 0.9f);
            waterLevelAnim.setDuration(10000);
            waterLevelAnim.setInterpolator(new DecelerateInterpolator());
            animators.add(waterLevelAnim);

            // amplitude animation.
            // wave grows big then grows small, repeatedly
            ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                    mWaveView, "amplitudeRatio", 0.01f, 0.05f);
            amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
            amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
            amplitudeAnim.setDuration(5000);
            amplitudeAnim.setInterpolator(new LinearInterpolator());
            animators.add(amplitudeAnim);

            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(animators);
        }

        public void cancel() {
            if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
                mAnimatorSet.end();
            }
        }
    }

}
