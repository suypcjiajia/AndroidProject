package com.example.bloodculture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityPepole extends AppCompatActivity {

    View viewTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pepole);

        viewTop = findViewById(R.id.top_in_pepole);
        ImageButton back = viewTop.findViewById(R.id.back);
        back.setOnClickListener(onBtnBackClickListener);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        String MachineID = intent.getStringExtra("MachineID");
        String extensionNum = intent.getStringExtra("ExtensionNum");
        String holeNum = intent.getStringExtra("HoleNum");
        ((TextView) viewTop.findViewById(R.id.txtDeviceType)).setText(MachineID + "-" + extensionNum);

        ((TextView)findViewById(R.id.textView13)).setText("孔号:" +  holeNum);
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
