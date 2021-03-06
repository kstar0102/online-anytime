package com.austraila.online_anytime.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.austraila.online_anytime.LocalManage.DatabaseHelper;
import com.austraila.online_anytime.R;
import com.austraila.online_anytime.activitys.LoginDepartment.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        openHelper = new DatabaseHelper(this);
        db = openHelper.getWritableDatabase();

        String str = "<h5><b>If no internet connection is available your form will not Geo-Stamp. If no mobile sync, please ensure you complete the site location details</b></h5>";
        Matcher m = Pattern.compile("<b>(.+?)</b>").matcher(str);
        while(m.find()) {
            String v = m.group(1);
            System.out.println(v);
        }

        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if(cursor.getCount() > 0){
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                //}
            }
        }, 3000);
    }
}
