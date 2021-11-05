package entity;

/**
 * 设备结构体
 */
public class DeviceEntity extends Object {
    public  String Id; //设备Id
    public  String msg; //信息
    public  String ip;//IP地址
    public DeviceEntity(){

    }
    public  DeviceEntity(String Id,String msg,String ip){
        this.Id = Id;
        this.msg = msg;
        this.ip = ip;
    }
}

