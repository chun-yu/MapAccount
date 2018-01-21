package com.chun.mapaccount;

/**
 * Created by Wayne on 2017/12/19.
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddItem extends Activity {
    private Button textView4, btn_setplace, btn_editObj,back_btm;
    private TextView calView, item_view, date_view, coordinate_view,switch_show,item_text_show;
    private Button btnclear, btn9, btn8, btn7, btn6, btn5, btn4, btn3, btn2, btn1, btn0;
    private Button btndel, btndiv, btnmul, btnsub, btnadd, btnpoint, btnequ, date_chose;
    private Switch switch1;
    private boolean point = false;
    private int requestCode = 1;
    private double la=25.035810 ,lo=121.513746 ;
    private static char ADD = '+';
    private static char SUB = '-';
    private static char MUL = '*';
    private static char DIV = '/';
    private static char MOD = '%';
    private char CALCULATE = '0'; //判斷計算位置0為無狀態1為已完成計算  剩餘為其他計算狀態
    private String name = "臺北市立大學";
    String save;

    private double one = Double.NaN;
    private double two = 0;
    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    LinearLayout layout_item;
    MyDB myDb;
    Cursor res;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        myDb = new MyDB(AddItem.this);
        //項目版面
        layout_item = (LinearLayout) findViewById(R.id.layout_item);
        item_view = (TextView) findViewById(R.id.item_view);
        item_text_show = (TextView) findViewById(R.id.item_text_show);
        switch_show = (TextView) findViewById(R.id.switch_show);
        //支出收入轉換
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    renew_table_show();
                    switch_show.setText(getString(R.string.coast));
                    Toast.makeText(AddItem.this, getString(R.string.coast_show), Toast.LENGTH_SHORT).show();
                } else {
                    renew_table_show();
                    switch_show.setText(getString(R.string.income));
                    Toast.makeText(AddItem.this, getString(R.string.income_show), Toast.LENGTH_SHORT).show();
                }
            }
        });
        date_view = (TextView) findViewById(R.id.date_view);
        date_view.setText(getToday());
        date_chose = (Button) findViewById(R.id.date_chose);
        date_chose.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        coordinate_view = (TextView) findViewById(R.id.coordinate);

        //完成按鈕
        textView4 = (Button) findViewById(R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myDb.addCoast("2018/01/02",Double.parseDouble(calView.getText().toString()),item_view.getText().toString(),23.5423,121.234);
                //myDb.delete(1);
                //myDb.storyDB();
                if (switch1.isChecked()) {
                    if (isNumeric(calView.getText().toString())) {
                        myDb.addCoast(date_view.getText().toString(), Double.parseDouble(calView.getText().toString()), item_view.getText().toString(), la, lo, coordinate_view.getText().toString(), name);
                        Toast.makeText(AddItem.this, getString(R.string.new_finish), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(AddItem.this, "輸入錯誤  請輸入正確金額\n  不含符號數 並請大於0", Toast.LENGTH_SHORT).show();
                } else {
                    if (isNumeric(calView.getText().toString())) {
                        myDb.addIncome(date_view.getText().toString(), Double.parseDouble(calView.getText().toString()), item_view.getText().toString());
                        Toast.makeText(AddItem.this, getString(R.string.new_finish), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(AddItem.this, "輸入錯誤  請輸入正確金額\n  不含符號數 並請大於0", Toast.LENGTH_SHORT).show();
                }
            }
        });
        back_btm = (Button) findViewById(R.id.finish_add_item);
        back_btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AddItem.this, MapAccount.class);
                startActivity(intent);
                AddItem.this.finish();
            }
        });

        //選項設定按鈕
        btn_editObj = (Button) findViewById(R.id.btn_editObj);
        btn_editObj.setBackgroundResource(R.drawable.sett);
        btn_editObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddItem.this, "設定選項", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(AddItem.this, EditObj.class), 2);
            }
        });
        //設定地點按鈕
        btn_setplace = (Button) findViewById(R.id.btn_setplace);
        btn_setplace.setBackgroundResource(R.drawable.earth);
        btn_setplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddItem.this, "選擇地點", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(AddItem.this, SetPlace.class), 1);
            }
        });
        //計算結果顯示
        calView = (TextView) findViewById(R.id.calView);
        //清除按鈕
        btnclear = (Button) findViewById(R.id.btnclear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                calView.setText("0");
                point = false;
                one = Double.NaN;
                CALCULATE = '0';
                one = 0;
                two = 0;
                save = "";
            }
        });
        //削減按鈕
        btndel = (Button) findViewById(R.id.btndel);
        btndel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText(calView.getText().toString().substring(0, calView.getText().length() - 1));
                    CALCULATE = '0';
                } else if (calView.getText().toString().equals("")) {
                    calView.setText("");
                } else {
                    calView.setText(calView.getText().toString().substring(0, calView.getText().length() - 1));
                }
            }
        });

        //除按鈕
        btndiv = (Button) findViewById(R.id.btndiv);
        btndiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (CALCULATE == '1') {
                    calView.setText(decimalFormat.format(one));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = DIV;
                    calView.setText(one + "/");
                    save = calView.getText().toString();
                }

                if (calView.getText().toString().equals("") && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = DIV;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "/");
                    save = calView.getText().toString();
                } else if (calView.getText().toString().equals("") && CALCULATE == '0') {
                    calView.setText("");
                } else if (calView.getText().toString().equals(save) && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = DIV;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "/");
                    save = calView.getText().toString();
                } else {
                    computeCalculation();
                    CALCULATE = DIV;
                    calView.setText(one + "/");
                    save = calView.getText().toString();
                }
            }
        });
        //乘按鈕
        btnmul = (Button) findViewById(R.id.btnmul);
        btnmul.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (CALCULATE == '1') {
                    calView.setText(decimalFormat.format(one));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = MUL;
                    calView.setText(one + "*");
                    save = calView.getText().toString();
                }

                if (calView.getText().toString().equals("") && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = MUL;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "*");
                    save = calView.getText().toString();
                } else if (calView.getText().toString().equals("") && CALCULATE == '0') {
                    calView.setText("");
                } else if (calView.getText().toString().equals(save) && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = MUL;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "*");
                    save = calView.getText().toString();
                } else {
                    computeCalculation();
                    CALCULATE = MUL;
                    calView.setText(one + "*");
                    save = calView.getText().toString();
                }
            }
        });
        //減按鈕
        btnsub = (Button) findViewById(R.id.btnsub);
        btnsub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (CALCULATE == '1') {
                    calView.setText(decimalFormat.format(one));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = SUB;
                    calView.setText(one + "-");
                    save = calView.getText().toString();
                }

                if (calView.getText().toString().equals("") && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = SUB;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "-");
                    save = calView.getText().toString();
                } else if (calView.getText().toString().equals("") && CALCULATE == '0') {
                    calView.setText("");
                } else if (calView.getText().toString().equals(save) && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = SUB;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "-");
                    save = calView.getText().toString();
                } else {
                    computeCalculation();
                    CALCULATE = SUB;
                    calView.setText(one + "-");
                    save = calView.getText().toString();
                }
            }
        });
        //加按鈕
        btnadd = (Button) findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (CALCULATE == '1') {
                    calView.setText(decimalFormat.format(one));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = ADD;
                    calView.setText(one + "+");
                    save = calView.getText().toString();
                }

                if (calView.getText().toString().equals("") && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = ADD;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "+");
                    save = calView.getText().toString();
                } else if (calView.getText().toString().equals("") && CALCULATE == '0') {
                    calView.setText("");
                } else if (calView.getText().toString().equals(save) && CALCULATE != '0') {
                    calView.setText(save.toString().substring(0, save.length() - 1));
                    one = 0;
                    CALCULATE = '0';
                    computeCalculation();
                    CALCULATE = ADD;
                    calView.setText(save.toString().substring(0, save.length() - 1) + "+");
                    save = calView.getText().toString();
                } else {
                    computeCalculation();
                    CALCULATE = ADD;
                    calView.setText(one + "+");
                    save = calView.getText().toString();
                }
            }
        });
        //點按鈕
        btnpoint = (Button) findViewById(R.id.btnpoint);
        btnpoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (point)
                    calView.setText(calView.getText());
                else
                    calView.setText(calView.getText() + ".");
                point = true;
            }
        });
        //等於按鈕
        btnequ = (Button) findViewById(R.id.btnequ);
        btnequ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                //判斷案等於鍵  CALCULATE = '1' 是以防使用者重複按等於鍵  判斷save是看是否為一開始就按等於
                if (save != null && CALCULATE != '1' && calView.getText().toString().equals(save)){
                    calView.setText(decimalFormat.format(one));
                    CALCULATE = '1';
                }else if (save != null && CALCULATE != '1') {
                    computeCalculation();
                    calView.setText(decimalFormat.format(one));
                    CALCULATE = '1';
                } else if (CALCULATE == '1') {
                    calView.setText(calView.getText());
                    CALCULATE = '1';
                }else{
                    computeCalculation();
                    calView.setText(decimalFormat.format(one));
                    CALCULATE = '1';
                }
            }
        });


        //數字按鈕
        btn9 = (Button) findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("9");
                    point = false;
                } else {
                    calView.append("9");
                }
            }
        });
        btn8 = (Button) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("8");
                    point = false;
                } else {
                    calView.append("8");
                }
            }
        });
        btn7 = (Button) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("7");
                    point = false;
                } else {
                    calView.append("7");
                }
            }
        });
        btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("6");
                    point = false;
                } else {
                    calView.append("6");
                }
            }
        });
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("5");
                    point = false;
                } else {
                    calView.append("5");
                }
            }
        });
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("4");
                    point = false;
                } else {
                    calView.append("4");
                }
            }
        });
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("3");
                    point = false;
                } else
                    calView.append("3");
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("2");
                    point = false;
                } else {
                    calView.append("2");
                }
            }
        });
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("1");
                    point = false;
                } else {
                    calView.append("1");
                }
            }
        });
        btn0 = (Button) findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (calView.getText().toString().equals("0") || calView.getText().toString().equals(save)) {
                    calView.setText("0");
                    point = false;
                } else {
                    calView.append("0");
                }
            }
        });
        renew_table_show();
    }

    private void computeCalculation() {
        if (!Double.isNaN(one)) {
            two = Double.parseDouble(calView.getText().toString());

            if (CALCULATE == ADD)
                one = this.one + two;
            else if (CALCULATE == SUB)
                one = this.one - two;
            else if (CALCULATE == MUL)
                one = this.one * two;
            else if (CALCULATE == DIV)
                one = this.one / two;
            else if (CALCULATE == MOD)
                one = this.one % two;
            else
                one = two;
        } else {
            try {
                one = Double.parseDouble(calView.getText().toString());
            } catch (Exception e) {
            }
        }
    }

    public void showDatePickerDialog() {
        // 設定初始日期
        Calendar c = Calendar.getInstance();
        String nowDate = date_view.getText().toString();
        try {
            c.setTime(sdf.parse(nowDate));
            // 跳出日期選擇器
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // 完成選擇，顯示日期
                    if (monthOfYear <= 8 && dayOfMonth <= 9) {
                        date_view.setText(year + "/0" + (monthOfYear + 1) + "/0" + dayOfMonth);
                    } else if (monthOfYear <= 8 && dayOfMonth > 9) {
                        date_view.setText(year + "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                    } else if (monthOfYear > 8 && dayOfMonth <= 9) {
                        date_view.setText(year + "/" + (monthOfYear + 1) + "/0" + dayOfMonth);
                    } else {
                        date_view.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        } catch (Exception e) {
            date_view.setText(getToday());
        }

    }

    private String getToday() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void renew_table_show() {
        res = myDb.getItemData();
        String coast = "支出";
        String income = "收入";
        int num = 0;
        String[] item_arrary = new String[60];
        int flag;
        while (res.moveToNext()) {
            String s1 = res.getString(1);
            if (switch1.isChecked()) {
                flag = coast.compareTo(s1);
                if (flag == 0) {
                    item_arrary[num] = res.getString(2);
                    num++;
                }
            } else {
                flag = income.compareTo(s1);
                if (flag == 0) {
                    item_arrary[num] = res.getString(2);
                    num++;
                }
            }
        }
        add_layout_show(num, item_arrary);
    }

    private void add_layout_show(int num, String[] item_arrary) {
        final String s;
        int count = 0;
        int num1 = num / 6;
        int num2 = num % 6;
        for (int i = layout_item.getChildCount(); i >= 0; i--) {
            layout_item.removeView(layout_item.getChildAt(i));
        }
        for (int i = 0; i < num1; i++) {
            LinearLayout tr = new LinearLayout(this);
            tr.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    140));
                        /* Create a Button to be the row-content. */
            final Button b1 = new Button(this);
            b1.setText(item_arrary[count]);
            b1.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b1.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b1.getText().toString());
                    item_text_show.setText(b1.getText().toString());
                }
            });
            b1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
            final Button b2 = new Button(this);
            b2.setText(item_arrary[count]);
            b2.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b2.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b2.getText().toString());
                    item_text_show.setText(b2.getText().toString());
                }
            });
            b2.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
            final Button b3 = new Button(this);
            b3.setText(item_arrary[count]);
            b3.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b3.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b3.getText().toString());
                    item_text_show.setText(b3.getText().toString());
                }
            });
            b3.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
            final Button b4 = new Button(this);
            b4.setText(item_arrary[count]);
            b4.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b4.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b4.getText().toString());
                    item_text_show.setText(b4.getText().toString());
                }
            });
            b4.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
            final Button b5 = new Button(this);
            b5.setText(item_arrary[count]);
            b5.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b5.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b5.getText().toString());
                    item_text_show.setText(b5.getText().toString());
                }
            });
            b5.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
            final Button b6 = new Button(this);
            b6.setText(item_arrary[count]);
            b6.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b6.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b6.getText().toString());
                    item_text_show.setText(b6.getText().toString());
                }
            });
            b6.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            count++;
                        /* Add Button to row. */

            tr.addView(b1);
            tr.addView(b2);
            tr.addView(b3);
            tr.addView(b4);
            tr.addView(b5);
            tr.addView(b6);
              /* Add row to TableLayout. */
            layout_item.addView(tr, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    140));
        }
        LinearLayout tr2 = new LinearLayout(this);
        tr2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                140));
        for (int i = 0; i < num2; i++) {
                        /* Create a Button to be the row-content. */
            final Button b7 = new Button(this);
            b7.setText(item_arrary[count]);
            b7.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            b7.getBackground().setColorFilter(getResources().getColor(R.color.coloritem), android.graphics.PorterDuff.Mode.MULTIPLY);
            b7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_view.setText(b7.getText().toString());
                    item_text_show.setText(b7.getText().toString());
                }
            });
            b7.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT, 3));
            tr2.addView(b7);
            count++;
        }
        layout_item.addView(tr2, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                140));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(resultCode){//resultCode是剛剛妳A切換到B時設的resultCode
            case 1://當B傳回來的Intent的requestCode 等於當初A傳出去的話
                double longitude = data.getExtras().getDouble("longitude");
                double latitude = data.getExtras().getDouble("latitude");
                String address = data.getExtras().getString("address");
                String local = data.getExtras().getString("localName");
                coordinate_view.setText(address);
                coordinate_view.setTextSize(20);
                lo = longitude;
                la = latitude;
                name = local;
                break;
            case 2://當B傳回來的Intent的requestCode 等於當初A傳出去的話
                renew_table_show();
                break;
        }

    }
    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        if(null == str || "".equals(str)){
            return false;
        }
        if(Double.parseDouble(str) <= 0){
            return false;
        }
        return true;
    }
}
