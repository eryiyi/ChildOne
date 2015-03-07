package com.xiaogang.ChildOne.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaogang.ChildOne.R;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/14
 * Time: 8:33
 * 类的功能、说明写在此处.
 */
public class SchoolBusActivity extends BaseActivity implements View.OnClickListener {

    private ImageView schoolbusback;
    private TextView carstart;
    private TextView carstop;

    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;
    private TextView locationInfoTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shoolbus);
        initView();

        locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
//        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("RIvp33GcGSGSwwntWPGXMxBs"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());
                }
                LOCATION_COUTNS ++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));

            }
        });
        locationClient.start();
        locationClient.requestLocation();

    }

    private void initView() {
        schoolbusback = (ImageView) this.findViewById(R.id.schoolbusback);
        schoolbusback.setOnClickListener(this);
        carstart = (TextView) this.findViewById(R.id.carstart);
        carstart.setOnClickListener(this);
        carstop = (TextView) this.findViewById(R.id.carstop);
        carstop.setOnClickListener(this);
//        locationInfoTextView = (TextView) this.findViewById(R.id.tv_loc_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.schoolbusback:
                finish();
                break;
            case R.id.carstart:
                //车辆出发
                break;
            case R.id.carstop:
                //车辆停止
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }
}
