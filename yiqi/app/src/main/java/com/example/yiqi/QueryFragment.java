package com.example.yiqi;


import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.example.tool.Http;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends Fragment {


    View root = null;
    MapView mMapView = null;
    AMap aMap;
    GeocodeSearch geocoderSearch ;

    private String mCity;
    private String mProvince;
    private String mCountry;

    HashMap<String,JsonObject> mHospitals = new HashMap<>();//医院信息


    public void toHospitalPos(String address,String name){
        JsonObject json = new JsonObject();
        json.addProperty("name",name);
        json.addProperty("Address",name);
        json.addProperty("device",8);
        json.addProperty("cmd",1);

        mHospitals.remove(name);
        mHospitals.put(name,json);
        geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(address, name);
        geocoderSearch.getFromLocationNameAsyn(query);


    }





    public QueryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_query, container, false);
        initMap(savedInstanceState);
        return root;
    }

    private void initMap(Bundle savedInstanceState){
        mMapView = root.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnCameraChangeListener(onCameraChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        System.out.println("QueryFragment::onDestroy");
        if( mMapView != null)
            mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        System.out.println("QueryFragment::onResume");
        if( mMapView != null)
            mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        System.out.println("QueryFragment::onPause");
        if( mMapView != null)
            mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if( mMapView != null)
            mMapView.onSaveInstanceState(outState);

    }



    /**
     * 地址描述数据结果回调
     */
    GeocodeSearch.OnGeocodeSearchListener  onGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener(){

        private HashMap<String,Integer> mCameraCitys = new HashMap<>();//记录哪些城市调用过了http接口
        /**
         * 坐标转地址
         * @param var1
         * @param var2
         */
        @Override
        public void onRegeocodeSearched(RegeocodeResult var1, int var2){
            RegeocodeAddress  addrs = var1.getRegeocodeAddress();

            System.out.println( "onRegeocodeSearched:" + addrs.getCountry() + ","
                    + addrs.getProvince() + ","
                    + addrs.getCity()  + "," + addrs.getDistrict());
            mCity = addrs.getCity();
            mCountry = addrs.getCountry();
            mProvince = addrs.getProvince();

            if( mCameraCitys.containsKey(mCity)){
                return;
            }else{
                mCameraCitys.put(mCity,0);
            }
            new MyThread().start();
        }

        /**
         * 地址转坐标
         * @param var1
         * @param var2
         */
        @Override
        public void onGeocodeSearched(GeocodeResult var1, int var2){

            List<GeocodeAddress>  addrs= var1.getGeocodeAddressList();
            for( GeocodeAddress addr : addrs){
                LatLonPoint latlon = addr.getLatLonPoint();

                String name = var1.getGeocodeQuery().getCity();

                System.out.println("onGeocodeSearched:" + latlon.toString()  + " "  + name);

                if( mHospitals.containsKey(name) ){
                    String device = mHospitals.get(name).get("device").getAsString();

                    System.out.println("onGeocodeSearched value:" + mHospitals.get(name).toString());


                    if( mHospitals.get(name).get("cmd") != null ){
                        System.out.println("onGeocodeSearched cmd:" + mHospitals.get(name).get("cmd").getAsInt());
                        if( mHospitals.get(name).get("cmd").getAsInt() == 1){
                            CameraUpdate cameraUpdate2 = CameraUpdateFactory.newCameraPosition(
                                    new CameraPosition(new LatLng(latlon.getLatitude(),latlon.getLongitude()),17,0,0));
                            aMap.animateCamera(cameraUpdate2);
                        }
                    }

                    LatLng latLng2 = new LatLng(latlon.getLatitude(),latlon.getLongitude());
                    aMap.addMarker(new MarkerOptions().position(latLng2).title(name).snippet( "设备数量:"  + device));
                }

                break;//取第一个
            }

        }
    };

    /**
     * 地图状态变化回调
     */
    AMap.OnCameraChangeListener onCameraChangeListener = new AMap.OnCameraChangeListener(){



        public void onCameraChange(CameraPosition var1){

        }

        /**
         * 地图状态变化完成后回调
         * @param var1
         */
        public void onCameraChangeFinish(CameraPosition var1){
            //var1.target是中心点GPS
            System.out.println( "onCameraChangeFinish:" + var1.target.toString() );
            geocoderSearch = new GeocodeSearch(getContext());
            geocoderSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            LatLonPoint p = new LatLonPoint(var1.target.latitude,var1.target.longitude);
            RegeocodeQuery query = new RegeocodeQuery(p,200,GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        }

    };

    class MyThread extends Thread{
        @Override
        public void run(){

            JsonObject ret  = Http.getHospitalByCity(mProvince,mCity,mCountry);
            if( Http.isSucess(ret)){

                JsonArray lists = ret.get("lists").getAsJsonArray();//医院列表
                for( int i = 0; i < lists.size(); i++){
                    JsonObject  an  = lists.get(i).getAsJsonObject();
                    String address  = an.get("Address").getAsString();//医院地址
                    String name  = an.get("name").getAsString();//医院名称
                    an.addProperty("device",6);
                    mHospitals.put(name,an);

                    geocoderSearch = new GeocodeSearch(getContext());
                    geocoderSearch.setOnGeocodeSearchListener(onGeocodeSearchListener);
                    // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
                    GeocodeQuery query = new GeocodeQuery(address, name);
                    geocoderSearch.getFromLocationNameAsyn(query);
                }

            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        System.out.println("onHiddenChanged:" + hidden);
//        if( hidden){
//            mMapView.setVisibility(View.INVISIBLE);
//        }else{
//            mMapView.setVisibility(View.VISIBLE);
//        }
    }
}


