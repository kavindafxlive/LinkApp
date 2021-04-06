package com.krs.linkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        new Handler().postDelayed(new Runnable() { @Override public void run() { Intent i=new Intent(FirstPage.this,MainActivity.class); startActivity(i); } }, 3000);
    }
}