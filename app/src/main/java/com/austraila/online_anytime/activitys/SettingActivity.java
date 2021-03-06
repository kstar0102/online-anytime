package com.austraila.online_anytime.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.austraila.online_anytime.Common.Common;
import com.austraila.online_anytime.LocalManage.DatabaseHelper;
import com.austraila.online_anytime.LocalManage.ElementValueDatabaeHelper;
import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.LoginDepartment.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private SQLiteDatabase db, VDb;
    private SQLiteOpenHelper openHelper, ElementValueHelper;
    DrawerLayout settinglayout;
    private NavigationView navigation;
    RelativeLayout loading;
    TextView setting_name, setting_email,setting_time,sidemenu_email;
    ImageView side_menu_setting;
    Button sync_btn;
    String useremail, username, userpass, token, result, formId,ElementValue, ElementId;
    RequestQueue queue;
    ArrayList<String> groupkeyList = new ArrayList<String>();
    Map<String, List<String>> elementData = new HashMap<String, List<String>>();
    List<String> value = new ArrayList<String>();
    Map<String, Map<String, String>> elementdata = new HashMap<String, Map<String, String>>();
    Map<String, String> Data = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().hide();

        loading = findViewById(R.id.loadingSetting);

        openHelper = new DatabaseHelper(this);
        ElementValueHelper = new ElementValueDatabaeHelper(this);
        db = openHelper.getWritableDatabase();
        VDb = ElementValueHelper.getReadableDatabase();

        //get Form element data from local database.
        try{final Cursor Vcursor = VDb.rawQuery("SELECT *FROM " + ElementValueDatabaeHelper.VTABLE_NAME,  null);
        if(Vcursor != null){
            if (Vcursor.moveToFirst()){
                do{
                    formId = Vcursor.getString(Vcursor.getColumnIndex("ElementFormId"));
                    ElementId = Vcursor.getString(Vcursor.getColumnIndex("ElementId"));
                    ElementValue = Vcursor.getString(Vcursor.getColumnIndex("ElementValue"));
                    Data.put(ElementId,ElementValue);
                    elementdata.put(formId, Data);
                    value.add(formId);
                }while(Vcursor.moveToNext());
            }
            Vcursor.close();
        }}catch (Exception e){
            AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
            alertDialog.setTitle("Catch the error");
            alertDialog.setMessage("The setting add the Photo error:" + e.getMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        System.out.println(elementdata.get(formId));

        for(int i = 0; i < value.size(); i++){
            String key = value.get(i);
            if(!groupkeyList.contains(key)){
                groupkeyList.add(key);
            }else{
                continue;
            }
        }
        System.out.println(groupkeyList);

        //get User information form local database.
        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    useremail = cursor.getString(cursor.getColumnIndex("Gmail"));
                    username = cursor.getString(cursor.getColumnIndex("username"));
                    userpass = cursor.getString(cursor.getColumnIndex("Password"));
                    token = cursor.getString(cursor.getColumnIndex("token"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        //define element
        setting_name = findViewById(R.id.setting_username);
        setting_email = findViewById(R.id.setting_email);
        setting_time = findViewById(R.id.setting_time);
        side_menu_setting = findViewById(R.id.menu_btn_setting);
        sync_btn = findViewById(R.id.setting_submit);
        sidemenu_email = findViewById(R.id.sidemenu_email);

        sidemenu_email.setText(useremail);

        sync_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                for(int i = 0; i < groupkeyList.size(); i++){
                    sendData(groupkeyList.get(i));
                }
            }
        });

        settinglayout = findViewById(R.id.setting_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        findViewById(R.id.menu_btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settinglayout.openDrawer(Gravity.LEFT);
            }
        });

        navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sidebar_course:
                        Intent intent_main = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent_main);
                        return true;

                    case R.id.sidebar_setting:
                        Intent intent_setting = new Intent(SettingActivity.this, SettingActivity.class);
                        startActivity(intent_setting);
                        return true;

                    case R.id.sidebar_logout:
                        Intent intent_logout = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent_logout);
                        return true;
                }
                return false;
            }
        });
        navigationView.setItemIconTintList(null);
    }

    private void sendData(final String formid) {
        String url = Common.getInstance().getSaveUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            if (result.equals("true")){
                                loading.setVisibility(View.GONE);
                                setting_email.setText(useremail);
                                setting_name.setText(username);

                                Date date = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String strDate = dateFormat.format(date);
                                setting_time.setText(strDate);

                                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                                alertDialog.setTitle("Notivce");
                                alertDialog.setMessage("Data was transferred successfully.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(SettingActivity.this, "Oops, Request failed.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        System.out.println(error);
                        Toast.makeText(SettingActivity.this, "It is currently offline.", Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams()
            {
                elementdata.get(formid).put("formId", formid);
                return elementdata.get(formid);
            }
        };
        queue = Volley.newRequestQueue(SettingActivity.this);
        queue.add(postRequest);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
