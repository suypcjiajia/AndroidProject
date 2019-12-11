package com.example.yiqi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("MyService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        System.out.println("MyService onStartCommand");
        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
