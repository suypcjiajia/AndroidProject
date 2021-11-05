package entity;

final public class HoleState {
    public final static int  hole_invalid =0;//无效
    public final static int  hole_empty = 1;//空瓶
    public final static int  hole_culture = 2;//培养中
    public final static int  hole_positive = 3;//阳性
    public final static int  hole_negative = 4;//阴性
    public final static int  hole_anoposivive = 5;//匿名阳性
    public final static int  hole_anonegative = 6;//匿名阴性


    public static String[] FinalState = {"无效","空瓶","培养中","阳性","阴性","匿名阳性","匿名阴性"};
}
