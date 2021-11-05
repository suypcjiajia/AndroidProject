package custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.bcphone.R;

/**
 * 设备View
 */
public class MyDevice extends LinearLayout {
    MyDevice myself;
    View root;
    ImageButton imageDevice;
    TextView txtIpAddr;
    TextView txtMsg;
    View.OnClickListener onClickListener;
    String Id = new String();
    public  MyDevice(Context context, AttributeSet attrs){
        super(context,attrs);
        // 加载布局
        root = LayoutInflater.from(context).inflate(R.layout.custom_device, this);
        myself = this;

        imageDevice = root.findViewById(R.id.imageDevice);
        txtIpAddr = root.findViewById(R.id.txtIpAddr);
        txtMsg = root.findViewById(R.id.txtMsg);

        imageDevice.setOnClickListener(onBtnImageClick);

    }

    public void setOnMyClickListener(View.OnClickListener l){
        onClickListener = l;
    }

    /**
     * 设置IP地址
     * @param txt
     */
    public  void setIpAddr(String txt){
        txtIpAddr.setText(txt);
    }

    /**
     * 设置提示信息
     * @param txt
     */
    public  void setMsg(String txt){
        txtMsg.setText(txt);
    }

    public  void setMyId(String txt){
        Id = txt;
        setVisibility(VISIBLE);
    }

    public  void clear(){
        txtMsg.setText("");
        txtIpAddr.setText("");
        Id = new String();
        setVisibility(INVISIBLE);
    }

    public String getMyId(){
        return Id;
    }

    public String getIpAddr(){
        return txtIpAddr.getText().toString();
    }

    /**
     *
     * @param v
     */
    View.OnClickListener onBtnImageClick = new View.OnClickListener() {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            onClickListener.onClick(myself);
        }
    };
}
