package com.chun.mapaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Wayne on 2017/12/20.
 */

public class EditObj extends Activity {
    private Button finish_edit_object, btn_new;
    private LinearLayout table_item;
    private TextView edit_text;
    private Switch switch2;
    MyDB myDb;
    Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_object);
        myDb = new MyDB(EditObj.this);
        table_item = (LinearLayout) findViewById(R.id.table_item);
        edit_text = (TextView) findViewById(R.id.edit_text);
        switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    renew_table_show();
                    InputMethodManager inputMethodManager = (InputMethodManager) EditObj.this.getApplicationContext().
                            getSystemService(EditObj.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
                    edit_text.setText("");
                    Toast.makeText(EditObj.this, getString(R.string.coast_show), Toast.LENGTH_SHORT).show();
                } else {
                    renew_table_show();
                    InputMethodManager inputMethodManager = (InputMethodManager) EditObj.this.getApplicationContext().
                            getSystemService(EditObj.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
                    edit_text.setText("");
                    Toast.makeText(EditObj.this, getString(R.string.income_show), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_new = (Button) findViewById(R.id.btn_new);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_DB_item();
                renew_table_show();
            }
        });
        finish_edit_object = (Button) findViewById(R.id.finish_edit_object);
        finish_edit_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                setResult(2,intent);
                EditObj.this.finish();
            }
        });
        renew_table_show();
    }

    private void renew_table_show() {
        res = myDb.getItemData();
        String coast = "支出";
        String income = "收入";
        int flag;
        for (int i = table_item.getChildCount(); i >= 0; i--) {
            table_item.removeView(table_item.getChildAt(i));
        }
        while (res.moveToNext()) {
            String s1 = res.getString(1);
            if (switch2.isChecked()) {
                flag = coast.compareTo(s1);
                if (flag == 0) {
                    add_table_show(res.getInt(0), res.getString(2));
                }
            } else {
                flag = income.compareTo(s1);
                if (flag == 0) {
                    add_table_show(res.getInt(0), res.getString(2));
                }
            }
        }
    }

    private void add_DB_item() {
        res = myDb.getItemData();
        boolean check = true;
        boolean check2 = true;
        String coast = "支出";
        String income = "收入";
        String dbitem, dbclass;
        String s = edit_text.getText().toString();
        int flag, flag2, flag3;
        while (res.moveToNext()) {
            dbclass = res.getString(1);
            dbitem = res.getString(2);
            flag = coast.compareTo(dbclass);
            flag2 = income.compareTo(dbclass);
            flag3 = s.compareTo(dbitem);
            if (switch2.isChecked()) {
                if (flag == 0 && flag3 == 0) {
                    check = false;
                    break;
                }
            } else {
                if (flag2 == 0 && flag3 == 0) {
                    check = false;
                    break;
                }
            }
        }
        if (s.equals("")) {
            check2 = false;
        } else {
            check2 = true;
        }
        if (check == true && check2 == true) {
            if (switch2.isChecked()) {
                Toast.makeText(EditObj.this, getString(R.string.new_finish), Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) EditObj.this.getApplicationContext().
                        getSystemService(EditObj.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
                myDb.addItem("支出", s);
                edit_text.setText("");
            } else {
                Toast.makeText(EditObj.this, getString(R.string.new_finish), Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) EditObj.this.getApplicationContext().
                        getSystemService(EditObj.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
                myDb.addItem("收入", s);
                edit_text.setText("");
            }
        } else if (check == true && check2 == false) {
            Toast.makeText(EditObj.this, "新增失敗 不能為空", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(EditObj.this, "新增失敗 已經有囉!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void add_table_show(int idd, String itemm) {
        final int id = idd;
        final String item = itemm;
        final String coast = "支出";
        final String income = "收入";
        LinearLayout tr = new LinearLayout(this);
        tr.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                130));
                        /* Create a Button to be the row-content. */
        TextView tv = new TextView(this);
        tv.setTextSize(30);
        tv.setText(item);
        tv.setTextColor(getResources().getColor(R.color.itemcolor));
        tv.setTypeface(null,Typeface.BOLD);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 1));
        Button b1 = new Button(this);
        b1.setBackgroundResource(R.drawable.pen);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch2.isChecked()) {
                    edit_item(id, coast, item);
                } else {
                    edit_item(id, income, item);
                }
            }
        });
        b1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 3));
        Button b2 = new Button(this);
        b2.setBackgroundResource(R.drawable.delete);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_item(id);
            }
        });
        b2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT, 3));
                        /* Add Button to row. */
        tr.addView(tv);
        tr.addView(b1);
        tr.addView(b2);
              /* Add row to TableLayout. */
        table_item.addView(tr, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                130));
    }

    private void edit_item(int idd, String itemm_class, String itemm) {
        final int id = idd;
        final String item = itemm;
        final String item_class = itemm_class;
        AlertDialog.Builder editDialog = new AlertDialog.Builder(this);
        editDialog.setTitle("請編輯選項 : ");

        final EditText editText = new EditText(this);
        editText.setText(item);
        editText.setTextColor(getResources().getColor(R.color.colorPrimary));
        editText.setTextSize(16);
        editDialog.setView(editText);

        editDialog.setPositiveButton("確 認", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                myDb.updateItemData(id, item_class, editText.getText().toString());
                InputMethodManager inputMethodManager = (InputMethodManager) EditObj.this.getApplicationContext().
                        getSystemService(EditObj.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                renew_table_show();
            }
        });
        editDialog.setNegativeButton("取 消", null);
        editDialog.show();
    }

    private void delete_item(int id) {
        myDb.delete_item(id);
        renew_table_show();
    }
}
