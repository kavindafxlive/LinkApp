package com.krs.linkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.PermissionStep;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnBoardActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //oneslide
        addFragment(new Step.Builder().setTitle("This is header 1")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FFE6E6")) // int background color
                .setDrawable(R.drawable.img1) // int top drawable
                .setSummary("This is summary")
                .build());


        //second slide
        addFragment(new Step.Builder().setTitle("This is header 2")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FFE6E6")) // int background color
                .setDrawable(R.drawable.img2) // int top drawable
                .setSummary("This is summary")
                .build());

        //third slide
        addFragment(new Step.Builder().setTitle("This is header 3")
                .setContent("This is content")
                .setBackgroundColor(Color.parseColor("#FFE6E6")) // int background color
                .setDrawable(R.drawable.img3) // int top drawable
                .setSummary("This is summary")
                .build());


        setContentView(R.layout.activity_on_board);



    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}