package com.chun.mapaccount;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.Collections;

public class My_mapFragment extends DialogFragment implements OnMapReadyCallback {
    private static final String TAG = "Tab1Fragment";

    private GoogleMap mMap;
    private MapView mMapView;
    private View mView;
    Marker check;
    LatLng latLng_check;
    private Button btnTEST;
    private LinearLayout cost_show;
    private Cursor res;
    MyDB myDb;
    String s = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.my_mapfragment, container, false);
        cost_show = (LinearLayout) mView.findViewById(R.id.cost_show);
        myDb = new MyDB(getActivity());
        show_cost_to_click();
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        latLng_check = new LatLng(25.035810, 121.513746);
        //createMarker(25.035810, 121.513746, "2018/1/11", "好吃的晚餐", 2000, "天大地大我家最大的北市大");
        res = myDb.getCoastData();
        while (res.moveToNext()) {
            latLng_check = new LatLng(res.getDouble(4), res.getDouble(5));
            check = mMap.addMarker(new MarkerOptions()
                    .position(latLng_check)
                    .title(res.getString(1))
                    .snippet("項目:" + res.getString(3) + "金額:" + res.getDouble(2) + "地址:" + res.getString(6) + "地名:" + res.getString(7)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_check, 17));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

    }

    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
//                // This means that getInfoContents will be called.
//                return null;
//            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
//            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//                // This means that the default info contents will be used.
//                return null;
//            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
                titleUi.setText(titleText);
                titleUi.setTextSize(20);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            StringBuilder sb = new StringBuilder(snippet);
            TextView snippetObj = (TextView) view.findViewById(R.id.snippetObj);
            TextView snippetPrice = (TextView) view.findViewById(R.id.snippetPrice);
            TextView snippetName = (TextView) view.findViewById(R.id.snippetName);
            TextView snippetAddress = (TextView) view.findViewById(R.id.snippetAddress);
            if (snippet != null) {
                int count , count2, count3;
                count = snippet.indexOf(":", 3) - 2;
                count2 = snippet.indexOf(":", count + 3) - 2;
                count3 = snippet.indexOf(":", count2 + 3) - 2;
                SpannableString spanObj = new SpannableString(snippet.substring(0, count));
                SpannableString spanPrice = new SpannableString(snippet.substring(count, count2));
                SpannableString spanAddress = new SpannableString(snippet.substring(count2, count3));
                SpannableString spanName = new SpannableString(snippet.substring(count3, snippet.length()));
//                spanObj.setSpan(new BackgroundColorSpan(Color.RED), spanObj.toString().indexOf(":") + 1, spanObj.length(), 0);
//                spanObj.setSpan(new ForegroundColorSpan(Color.WHITE), spanObj.toString().indexOf(":") + 1, spanObj.length(), 0);
//                spanPrice.setSpan(new BackgroundColorSpan(Color.RED), spanPrice.toString().indexOf(":") + 1, spanPrice.length(), 0);
//                spanPrice.setSpan(new ForegroundColorSpan(Color.WHITE), spanPrice.toString().indexOf(":") + 1, spanPrice.length(), 0);
                snippetObj.setText(spanObj);
                snippetPrice.setText(spanPrice);
                snippetName.setText(spanName);
                snippetAddress.setText(spanAddress);

            } else {
                snippetObj.setText("");
                snippetPrice.setText("");
                snippetAddress.setText("");
                snippetName.setText("");
            }
            snippetObj.setTextColor(Color.BLACK);
            snippetPrice.setTextColor(Color.BLACK);
            snippetAddress.setTextColor(Color.BLACK);
            snippetName.setTextColor(Color.BLACK);
        }
    }

    private void show_cost_to_click() {
        res = myDb.getCoastData();
        int[] datearrary = new int[100];
        int count = 0;
        boolean putid = false;
        int[] idarrary = new int[100];
        String[] splitarrary = new String[3];
        for (int i = cost_show.getChildCount(); i >= 0; i--) {
            cost_show.removeView(cost_show.getChildAt(i));
        }
        while (res.moveToNext()) {
            String s2 = res.getString(1);
            splitarrary = s2.split("/");
            int year = Integer.parseInt(splitarrary[0]);
            int month = Integer.parseInt(splitarrary[1]);
            int day = Integer.parseInt(splitarrary[2]);
            datearrary[count] = year * 10000 + month * 100 + day;
            count++;
        }
        Arrays.sort(datearrary);
        count = 0;
        for (int i = datearrary.length; i > 0; i--) {
            int k = datearrary[i - 1];
            res = myDb.getCoastData();
            while (res.moveToNext()) {
                String s2 = res.getString(1);
                splitarrary = s2.split("/");
                int year = Integer.parseInt(splitarrary[0]);
                int month = Integer.parseInt(splitarrary[1]);
                int day = Integer.parseInt(splitarrary[2]);
                int date = year * 10000 + month * 100 + day;
                if (date == k) {
                    putid = true;
                    for (int j = 0; j < idarrary.length; j++) {
                        if (idarrary[j] == res.getInt(0)) {
                            putid = false;
                        }
                    }
                    if (putid) {
                        idarrary[count] = res.getInt(0);
                        putid = false;
                        count++;
                    }
                }
            }
        }
        for (int i = 0; i < idarrary.length; i++) {
            if (idarrary[i] == 0) {
            }
            res = myDb.getCoastData();
            while (res.moveToNext()) {
                if (idarrary[i] == res.getInt(0)) {
                    add_table_show(res.getString(1), res.getString(3), res.getDouble(2), res.getString(6), new LatLng(res.getDouble(4), res.getDouble(5)), res.getString(7));
                }
            }
        }
    }

    private void add_table_show(String datee, String itemm, double numm, String placee, final LatLng coordinate, final String name) {
        final String date = datee;
        final String item = itemm;
        final double num = numm;
        final String place = placee;
        LinearLayout tr = new LinearLayout(getContext());
        tr.setClickable(true);
        tr.setOrientation(LinearLayout.VERTICAL);
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), item + "  $ " + num, Toast.LENGTH_SHORT).show();
                //mMap.clear();
                createMarker(coordinate.latitude, coordinate.longitude, date, item, num, place, name);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));
            }
        });
        tr.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                60));
        LinearLayout ttr1 = new LinearLayout(getContext());
        ttr1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                60));
        LinearLayout ttr2 = new LinearLayout(getContext());
        ttr2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                60));
                        /* Create a Button to be the row-content. */
        TextView tv0 = new TextView(getContext());
        tv0.setTextSize(18);
        tv0.setText(date);
        tv0.setTextColor(getResources().getColor(R.color.holo_blue_dark));
        tv0.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 1));
        TextView tv1 = new TextView(getContext());
        tv1.setTextSize(18);
        tv1.setText(item);
        tv1.setTextColor(getResources().getColor(R.color.holo_blue_dark));
        tv1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 1));
        TextView tv2 = new TextView(getContext());
        tv2.setTextSize(18);
        tv2.setText("$ " + num);
        tv2.setTextColor(getResources().getColor(R.color.holo_blue_dark));
        tv2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 1));
        TextView tv3 = new TextView(getContext());
        tv3.setTextSize(18);
        tv3.setText("地點:  " + place);
        tv3.setTextColor(getResources().getColor(R.color.colorAccent));
        tv3.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 1));
                        /* Add Button to row. */
        ttr1.addView(tv0);
        ttr1.addView(tv1);
        ttr1.addView(tv2);
        ttr2.addView(tv3);
        tr.addView(ttr1);
        tr.addView(ttr2);
              /* Add row to TableLayout. */
        cost_show.addView(tr, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                120));
    }

    public void createMarker(double latitude, double longitude, String date, String obj, double price, String address, String localName) {
        //mMap.clear();
        latLng_check = new LatLng(latitude, longitude);
        MarkerOptions makerOption = new MarkerOptions().position(latLng_check).title(date).snippet("項目:" + obj + "金額:" + price + "地址:" + address + "地名:" + localName);
        check = mMap.addMarker(makerOption);
        check.showInfoWindow();
    }

}
