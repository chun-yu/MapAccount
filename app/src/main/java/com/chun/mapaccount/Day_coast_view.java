package com.chun.mapaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Day_coast_view extends DialogFragment {
    private static final String TAG = "Tab1Fragment";

    private Button btnTEST;
    private TextView dateText;
    private ImageButton btnleft;
    private ImageButton btnright;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private LinearLayout coast_listview, income_listview;
    private TextView all_coast_text, all_income_text, balance_text;
    MyDB myDb;
    Cursor res;
    Cursor res2;
    private double allcoast, allincome, allbalance;
    DecimalFormat df = new DecimalFormat("#.#");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coast_view, container, false);
        btnTEST = (Button) view.findViewById(R.id.btnTEST);
        dateText = (TextView) view.findViewById(R.id.dateText);
        btnleft = (ImageButton) view.findViewById(R.id.btnleft);
        btnright = (ImageButton) view.findViewById(R.id.btnright);
        dateText.setText(getToday());
        dateText.setEnabled(false);

        all_coast_text = (TextView) view.findViewById(R.id.all_coast_text);
        all_income_text = (TextView) view.findViewById(R.id.all_income_text);
        balance_text = (TextView) view.findViewById(R.id.balance_text);
        coast_listview = (LinearLayout) view.findViewById(R.id.coast_listview);
        income_listview = (LinearLayout) view.findViewById(R.id.income_listview);

        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(-1);
                listview_show();
                all_text();
            }
        });
        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(1);
                listview_show();
                all_text();
            }
        });
        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddItem.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), getString(R.string.go_new), Toast.LENGTH_SHORT).show();
            }
        });
        listview_show();
        all_text();
        return view;
    }

    private String getToday() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void changeDate(int days) {
        Calendar c = Calendar.getInstance();
        String nowDate = dateText.getText().toString();
        try {
            c.setTime(sdf.parse(nowDate));
            c.add(Calendar.DATE, days);
            dateText.setText(sdf.format(c.getTime()));
        } catch (Exception e) {
            dateText.setText(getToday());
        }
    }

    private void listview_show() {
        res = myDb.getCoastData();
        res2 = myDb.getIncomeData();
        String nowdate = dateText.getText().toString();
        int flag;
        for (int i = coast_listview.getChildCount(); i >= 0; i--) {
            coast_listview.removeView(coast_listview.getChildAt(i));
        }
        for (int i = income_listview.getChildCount(); i >= 0; i--) {
            income_listview.removeView(income_listview.getChildAt(i));
        }
        while (res.moveToNext()) {
            String s2 = res.getString(1);
            flag = nowdate.compareTo(s2);
            if (flag == 0) {
                add_table_show(coast_listview, res.getInt(0), res.getString(3), res.getDouble(2));
            }
        }
        while (res2.moveToNext()) {
            String s2 = res2.getString(1);
            flag = nowdate.compareTo(s2);
            if (flag == 0) {
                add_table_show(income_listview, res2.getInt(0), res2.getString(3), res2.getDouble(2));
            }
        }
    }

    private void all_text() {
        res = myDb.getCoastData();
        res2 = myDb.getIncomeData();
        String nowdate = dateText.getText().toString();
        int flag;
        while (res.moveToNext()) {
            String s2 = res.getString(1);
            flag = nowdate.compareTo(s2);
            if (flag == 0) {
                allcoast = allcoast + res.getDouble(2);
            }
        }
        while (res2.moveToNext()) {
            String s2 = res2.getString(1);
            flag = nowdate.compareTo(s2);
            if (flag == 0) {
                allincome = allincome + res2.getDouble(2);
            }
        }
        allbalance = allincome - allcoast;
        allcoast = Double.parseDouble(df.format(allcoast));
        allincome = Double.parseDouble(df.format(allincome));
        allbalance = Double.parseDouble(df.format(allbalance));
        all_coast_text.setText(allcoast + "");
        all_income_text.setText(allincome + "");
        balance_text.setText(allbalance + "");
        allbalance = 0;
        allincome = 0;
        allcoast = 0;
    }

    private void add_table_show(LinearLayout listview, int idd, String itemm, double numm) {
        final LinearLayout _view = listview;
        final int id = idd;
        final String item = itemm;
        final double num = numm;
        LinearLayout tr = new LinearLayout(getContext());
        tr.setClickable(true);
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_item(_view,id,item,num);
            }
        });
        tr.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                110));
                        /* Create a Button to be the row-content. */
        TextView tv1 = new TextView(getContext());
        tv1.setTextSize(20);
        tv1.setText(item);
        tv1.setTextColor(getResources().getColor(R.color.holo_blue_dark));
        tv1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT,2));
        TextView tv2 = new TextView(getContext());
        tv2.setTextSize(20);
        tv2.setText("$ "+num);
        tv2.setTextColor(getResources().getColor(R.color.holo_blue_dark));
        tv2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT,3));
                        /* Add Button to row. */
        tr.addView(tv1);
        tr.addView(tv2);
              /* Add row to TableLayout. */
        listview.addView(tr,new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                110));
    }
    public void setMyDB(MyDB myDB){
        this.myDb=myDB;
    }
    private void edit_item(LinearLayout listview,int idd,String itemm,double numm){
        final LinearLayout _view = listview;
        final int id = idd;
        final String item = itemm;
        final String num = String.valueOf(numm);
        AlertDialog.Builder editDialog = new AlertDialog.Builder(getActivity());
        editDialog.setTitle("你確定要 刪除 該項目嗎: ");

        final TextView editText = new TextView(getActivity());
        editText.setText("  "+item+"       $  "+num);
        editText.setTextColor(getResources().getColor(R.color.colorPrimary));
        editText.setTextSize(20);
        editDialog.setView(editText);

        editDialog.setPositiveButton("確 認", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                if(_view==coast_listview){
                    myDb.delete_coast(id);
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }else{
                    myDb.delete_income(id);
                    listview_show();
                    all_text();
                }
            }
        });
        editDialog.setNegativeButton("取 消",null);
        editDialog.show();
    }
}