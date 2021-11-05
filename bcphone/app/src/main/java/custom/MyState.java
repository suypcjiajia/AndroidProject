package custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bcphone.R;

public class MyState extends LinearLayout {
    Button btn;
    TextView text;
    View root;
    public  MyState(Context context, AttributeSet attrs){
        super(context,attrs);
        // 加载布局
        root = LayoutInflater.from(context).inflate(R.layout.custom_state, this);
        btn = root.findViewById(R.id.button);
        text = root.findViewById(R.id.txtIpAddr);


//        GradientDrawable drawable=new GradientDrawable();
//        drawable.setShape(GradientDrawable.RECTANGLE);
//        drawable.setCornerRadius(10);
//        btn.setBackground(drawable);
    }

    public void setTemp(String v){
        text.setText(v);
        if(v.equals("-")){
            btn.setBackgroundResource(R.drawable.temp_normal);
            return;
        }
        float tmp = Float.parseFloat(v);
        if( tmp > 37 ) {
            btn.setBackgroundResource(R.drawable.temp_high);
        }else {
            btn.setBackgroundResource(R.drawable.temp_normal);
        }
    }

    public void openDoor(){
        btn.setBackgroundResource(R.drawable.door_open);
        text.setText("开");
    }

    public void closeDoor(){
        btn.setBackgroundResource(R.drawable.door_close);
        text.setText("关");
    }



//    public void setText(String value){
//        text.setText(value);
//        if(value.equals("开门")) {
//            GradientDrawable bk = (GradientDrawable) btn.getBackground();
//            bk.setColor(Color.parseColor("#D81B60"));
//        }
//        else{
//            GradientDrawable bk = (GradientDrawable)btn.getBackground();
//            bk.setColor(Color.parseColor("#008577"));
//        }
//    }
//
//    public  void setValue(int value){
//        text.setText(value + "度");
//        if(value > 37){
//            GradientDrawable bk = (GradientDrawable) btn.getBackground();
//            bk.setColor(Color.parseColor("#D81B60"));
//        }else{
//            GradientDrawable bk = (GradientDrawable)btn.getBackground();
//            bk.setColor(Color.parseColor("#008577"));
//        }
//    }
}
