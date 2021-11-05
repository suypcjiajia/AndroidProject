package com.example.android.bcphone;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import custom.BottolView;
import custom.MyState;
import entity.HoleState;
import entity.ShareData;

import com.google.gson.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class StateFragment extends Fragment {

    View root;
    MyState stateTmp;
    MyState stateTmp2;
    MyState stateTmp3;
    MyState stateDoor;
    TextView textViewCulture;
    TextView textViewPositive;
    TextView textViewNegative;
    TextView textViewAnoPositive;
    TextView textViewAnoNegative;
    TextView txtHouseCount;
    TextView txtHoleCount;
    Spinner spinHouse;
    int mHoleCount = -1;//面板共有多少瓶子
    int mHouseCount = -1;//有多少仓室
    int ExtensionNum = 0;//当前选择的仓室
    BottolView currentHoleView = null; //用户选了哪一个瓶子(view)
    int mBaoyangHoleNum;
    public StateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_state, container, false);
        stateTmp = root.findViewById(R.id.stateTmp);
        stateTmp2 = root.findViewById(R.id.stateTmp2);
        stateTmp3 = root.findViewById(R.id.stateTmp3);
        stateDoor = root.findViewById(R.id.stateDoor);
        txtHouseCount  = root.findViewById(R.id.txtHouseCount);
        txtHoleCount = root.findViewById(R.id.txtHoleCount);
        spinHouse = root.findViewById(R.id.spinHouse);
        View viewHole = root.findViewById(R.id.board_holenums);
        textViewCulture = viewHole.findViewById(R.id.textViewCulture);
        textViewPositive = viewHole.findViewById(R.id.textViewPositive);
        textViewNegative = viewHole.findViewById(R.id.textViewNegative);
        textViewAnoPositive = viewHole.findViewById(R.id.textViewAnoPositive);
        textViewAnoNegative = viewHole.findViewById(R.id.textViewAnoNegative);

        stateTmp.setTemp("-");
        stateTmp2.setTemp("-");
        stateTmp3.setTemp("-");
        stateDoor.closeDoor();

        txtHouseCount.setText( String.valueOf( mHouseCount) );
        txtHoleCount.setText(  String.valueOf( mHoleCount ) );


        spinHouse.setOnItemSelectedListener(onItemSelectedListener);

        return root;
    }

    private void initSpinHouse(){
        String[] items = new String[mHouseCount];
        for(int i = 0; i < mHouseCount ; i++){
            if( i == 0){
                items[i] = "主仓";
            }else
                items[i] = "分仓" + i;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHouse.setAdapter(arrayAdapter);
    }

    private void initBoard() {
        GridLayout gridLayout = root.findViewById(R.id.gridLayoutBottle);
        gridLayout.removeAllViews();
        int gridWidth = gridLayout.getWidth();
        System.out.println("GridLayout width:" + gridWidth);
        int columnCount = 6;
        if(mHoleCount > 24){
            columnCount = 8;
        }
        int rowCount = mHoleCount % columnCount == 0 ? mHoleCount / columnCount : mHoleCount / columnCount + 1;
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);
        int holeWidth = gridWidth / columnCount;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i * columnCount + j + 1 > mHoleCount) {
                    break;
                }
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec cloumnSpec = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, cloumnSpec);

                params.width = holeWidth/2;
                params.height = params.width;
                int margin = (holeWidth - params.width) / 2;
                if(i == 0){
                    //第一行的topMargin要多holeWidth空间
                    params.setMargins(margin , margin/3 + holeWidth, margin, margin/3);
                }else{
                    params.setMargins(margin , margin/3 , margin, margin/3);
                }

                BottolView view = new BottolView(getContext());
                view.setId(10000 + i * columnCount + j );
                view.setText(String.valueOf(i * columnCount + j + 1));
                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setGravity(Gravity.CENTER_VERTICAL);

                view.setMyColor(0xff999999);
                view.setMyState(HoleState.hole_empty);
                view.back();

                gridLayout.addView(view, params);
                view.setOnClickListener(onHoleClick);
            }
        }
    }

    /**
     * 更新面板状态
     */
    public void updateHoleState() {

        if(ShareData.getBoard == null){
            return;
        }
        if(mHouseCount == -1 || mHoleCount == -1){
            return;
        }

        int nums = ShareData.getBoard.size();
        GridLayout gridLayout = root.findViewById(R.id.gridLayoutBottle);

        int cultureNum = 0;
        int positiveNum = 0;
        int negativeNum = 0;
        int anopositiveNum = 0;
        int anonegativeNum = 0;


        for (int i = 0; i < nums; i++) {

            JsonObject hole = ShareData.getBoard.get(i).getAsJsonObject();

            int  extensionNum = hole.get("ExtensionNum").getAsInt();
            if(extensionNum != ExtensionNum){
                continue;
            }

            int HoleNum = hole.get("HoleNum").getAsInt();
            if (HoleNum + 1 > mHoleCount) {
                continue;
            }

            BottolView view = gridLayout.findViewById(10000 + HoleNum);
            if (view == null) {
                continue;
            }

            int state = hole.get("CurrentState").getAsInt();
            view.setMyState(state);
            if (state == HoleState.hole_invalid) {
                view.setMyColor(0xffB5B510);

            } else if (state == HoleState.hole_empty) {
                view.setMyColor(0xff999999);

            } else if (state == HoleState.hole_culture) {
                view.setMyColor(0xff98B2E4);
                cultureNum++;
            } else if (state == HoleState.hole_positive) {
                view.setMyColor(0xffC90606);
                positiveNum++;
            } else if (state == HoleState.hole_negative) {
                view.setMyColor(0xffA2B792);
                negativeNum++;
            } else if (state == HoleState.hole_anoposivive) {
                view.setMyColor(0xffF2B76E);
                anopositiveNum++;
            } else if (state == HoleState.hole_anonegative) {
                view.setMyColor(0xffBDD391);
                anonegativeNum++;
            }
            if( HoleNum == mBaoyangHoleNum){
                view.selected();
                currentHoleView = view;
            }else {
                view.back();
            }
            view.ExtensionNum = extensionNum;
            view.HoleNum = HoleNum;
            view.PutInTime = hole.get("PutInTime").getAsString();
        }


        textViewCulture.setText(String.valueOf(cultureNum));
        textViewPositive.setText(String.valueOf(positiveNum));
        textViewNegative.setText(String.valueOf(negativeNum));
        textViewAnoPositive.setText(String.valueOf(anopositiveNum));
        textViewAnoNegative.setText(String.valueOf(anonegativeNum));

    }


    public void updateState(){
        if(ShareData.getState == null){
            return;
        }
        if(mHouseCount == -1 || mHoleCount == -1){
            return;
        }
        String DoorState = String.format("DoorState%d",ExtensionNum);
        if(ShareData.getState.has(DoorState)){
            if(ShareData.getState.get(DoorState).getAsInt() == 0){
                stateDoor.closeDoor();
            }else{
                stateDoor.openDoor();
            }
        }
        String Temperature1 = String.format("Temperature-%d-1",ExtensionNum);
        String Temperature2 = String.format("Temperature-%d-2",ExtensionNum);
        String Temperature3 = String.format("Temperature-%d-3",ExtensionNum);

        if(ShareData.getState.has(Temperature1)){
            stateTmp.setTemp(ShareData.getState.get(Temperature1).getAsString());
        }
        if(ShareData.getState.has(Temperature2)){
            stateTmp2.setTemp(ShareData.getState.get(Temperature2).getAsString());
        }
        if(ShareData.getState.has(Temperature3)){
            stateTmp3.setTemp(ShareData.getState.get(Temperature3).getAsString());
        }
    }

    public void initHouse(){
        if(ShareData.getHouse == null){
            return;
        }
        mHoleCount = ShareData.getHouse.get("hole").getAsInt();
        mHouseCount = ShareData.getHouse.get("house").getAsInt();
        txtHouseCount.setText( String.valueOf( mHouseCount ));
        txtHoleCount.setText( String.valueOf( mHoleCount ) );
        ExtensionNum = 0;
        initSpinHouse();
        initBoard();

    }


    View.OnClickListener onHoleClick = new View.OnClickListener() {
        public void onClick(View v) {
            BottolView clickView = (BottolView)v;
//            if(clickView.equals(currentHoleView)){
//                return;
//            }
            if( currentHoleView != null){
                currentHoleView.back();
            }

            clickView.selected();
            currentHoleView = clickView;

            Intent intent = new Intent(getContext(), CurveActivity.class);
            ShareData.PutInTime = clickView.PutInTime;
            ShareData.HoleNum = clickView.HoleNum;
            ShareData.ExtensionNum = clickView.ExtensionNum;
            startActivity(intent);
        }
    };




    /**
     * Interface definition for a callback to be invoked when
     * an item in this view has been selected.
     */
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener(){

        public  void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            ExtensionNum = position;
            updateHoleState();
            updateState();
        }

        public  void onNothingSelected(AdapterView<?> parent){

        }


    };


}
