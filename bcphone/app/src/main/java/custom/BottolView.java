package custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.android.bcphone.R;

import tool.ShapeUtil;


/**
 * 瓶子View
 */
public class BottolView extends TextView {
    private int state;//瓶子状态
    private int color;//边框颜色
    private int radius = 45;//瓶子圆角
    private int strokeWidth = 5;//边框大小

    public  String PutInTime;
    public  int HoleNum;
    public  int ExtensionNum;


    private int sltstrokeWidth = 15;//被选中时边框大小
    public BottolView(Context context){
        super(context);
        setTextColor(getResources().getColor(R.color.holeText));
    }
    public BottolView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottolView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMyState(int state){
        this.state = state;
    }

    public void setMyColor(int color){
        this.color = color;
    }

    public int getMyState(){
        return state;
    }

    public int getMyColor(){
        return color;
    }

    /**
     * 恢复原来的样式
     */
    public void back(){
        setBackground(ShapeUtil.getRoundRectDrawable(radius, color,false,strokeWidth));
    }

    /**
     * 选中时的样式
     */
    public void selected(){
        setBackground(ShapeUtil.getRoundRectDrawable(radius, color,false,sltstrokeWidth));
    }

}
