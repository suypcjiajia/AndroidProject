package com.example.bloodculture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityPepole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pepole);

        View viewTop = findViewById(R.id.top_in_pepole);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);
    }

    View.OnClickListener onBtnBackClickListener =  new  View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v){
            Intent intent = new Intent(ActivityPepole.this, ActivityBoard.class);
            startActivity(intent);
        }
    };
}
