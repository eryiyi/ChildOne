package com.xiaogang.ChildOne.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.model.LatLng;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.OpenCarDATA;
import com.xiaogang.ChildOne.data.SuccessDATA;
import com.xiaogang.ChildOne.data.TraceDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.OpenCar;
import com.xiaogang.ChildOne.entity.Trace;
import com.xiaogang.ChildOne.util.CommonUtil;
import com.xiaogang.ChildOne.util.InternetURL;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class OpenglDemo extends BaseActivity implements OnMapDrawFrameCallback, View.OnClickListener {
    private ImageView schoolbusback;
    private TextView carstart;
    private TextView carstop;
	private static final String LTAG = OpenglDemo.class.getSimpleName();

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Bitmap bitmap;

	private List<LatLng> latLngPolygon = new ArrayList<LatLng>();

	private float[] vertexs;
	private FloatBuffer vertexBuffer;

    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 60000;
    private static int LOCATION_COUTNS = 0;

    private Double lat;
    private Double lon;
    private String line_id;//定义一个路线的ID

    private List<Trace> listDw  = new ArrayList<Trace>();
    private Toast mToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoolbus);
        initView();

        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
//        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("RIvp33GcGSGSwwntWPGXMxBs"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient = new LocationClient(getApplicationContext());
        locationClient.setLocOption(option);
        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                lat = location.getLatitude();
                lon = location.getLongitude();
                //定位到当期位置
                LatLng ll = new LatLng(lat, lon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.setMapStatus( u );
//                MydrawPointCurrentLocation(lat, lon);
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
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapDrawFrameCallback(this);
        bitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ground_overlay);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        // onResume 纹理失效
        textureId = -1;
        super.onResume();
    }

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
	}

    public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
        if (mBaiduMap.getProjection() != null && vertexBuffer!=null) {
            calPolylinePoint(drawingMapStatus);
            if(vertexBuffer != null){
                drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
                        drawingMapStatus);
            }
        }
    }

    public void calPolylinePoint(MapStatus mspStatus) {
        if(latLngPolygon!=null && latLngPolygon.size()>0){
            PointF[] polyPoints = new PointF[latLngPolygon.size()];
            vertexs = new float[3 * latLngPolygon.size()];
            int i = 0;
            for (LatLng xy : latLngPolygon) {
                polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
                        mspStatus);
                vertexs[i * 3] = polyPoints[i].x;
                vertexs[i * 3 + 1] = polyPoints[i].y;
                vertexs[i * 3 + 2] = 0.0f;
                i++;
            }
            for (int j = 0; j < vertexs.length; j++) {
                Log.d(LTAG, "vertexs[" + j + "]: " + vertexs[j]);
            }
            vertexBuffer = makeFloatBuffer(vertexs);
        }

    }
    private FloatBuffer makeFloatBuffer(float[] fs) {
        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(fs);
        fb.position(0);
        return fb;
    }

    private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
                              float lineWidth, int pointSize, MapStatus drawingMapStatus) {

        gl.glEnable(GL10.GL_BLEND);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        float colorA = Color.alpha(color) / 255f;
        float colorR = Color.red(color) / 255f;
        float colorG = Color.green(color) / 255f;
        float colorB = Color.blue(color) / 255f;

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
        gl.glColor4f(colorR, colorG, colorB, colorA);
        gl.glLineWidth(lineWidth);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

        gl.glDisable(GL10.GL_BLEND);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
	int textureId = -1;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.schoolbusback:
                finish();
                break;
            case R.id.carstart:
                //车辆出发
                startCar("1");
                break;
            case R.id.carstop:
                //车辆停止
                startCar("2");
                break;
        }
    }

    private void startCar(String typeid) {
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.CAR_OPEN_URL + "?uid=%s&class_id=%s&open=%s", account.getUid(), account.getClass_id(), typeid);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                OpenCarDATA data = getGson().fromJson(s, OpenCarDATA.class);
                                OpenCar openCar = data.getData();
                                line_id = openCar.getLine_id()==null?"":openCar.getLine_id();//获得路线的ID
                                //更新车辆位置数据
                                updateCar(line_id);
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
            );
            getRequestQueue().add(request);
        }
    }
    private void updateCar(String line_id) {
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.UPDATE_CAR_URL + "?uid=%s&line_id=%s&lng=%s&lat=%s", account.getUid(), line_id, lon, lat);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                SuccessDATA data = getGson().fromJson(s, SuccessDATA.class);
                                if(data.getCode() == 200){//成功
                                    Toast.makeText(mContext, "校车位置更新成功", Toast.LENGTH_SHORT).show();
                                    //开始获得校车路径，画路线
                                    getData();
                                }else{
                                    Toast.makeText(mContext, "校车位置更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
            );
            getRequestQueue().add(request);
        }
    }
    private void getData(){
        Account account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        if (account != null) {
            String uri = String.format(InternetURL.GET_LOCATION_URL + "?uid=%s&class_id=%s", account.getUid(), account.getClass_id());
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (CommonUtil.isJson(s)){
                                TraceDATA data = getGson().fromJson(s, TraceDATA.class);
                                listDw = data.getData();
                                if(listDw!=null){
                                    for(int i =0;i<listDw.size();i++){
                                        Trace trace=listDw.get(i);
                                        LatLng latlng = new LatLng(Double.parseDouble(trace.getLat()) , Double.parseDouble(trace.getLng()));
                                        latLngPolygon.add(latlng);
                                    }
                                }
                                //开始处理数据
                                if(latLngPolygon!=null && latLngPolygon.size()>0){
                                    //绘制起点
                                    MydrawPointEnd(latLngPolygon.get(0).latitude, latLngPolygon.get(0).longitude);
                                    if(latLngPolygon.size() > 1){
                                        //绘制终点
                                         MydrawPointStart(latLngPolygon.get(latLngPolygon.size() - 1).latitude, latLngPolygon.get(latLngPolygon.size() - 1).longitude);
                                    }
                                }
                            }else {
                                Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }
            );
            getRequestQueue().add(request);
        }
    }
    public void MydrawPointStart(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.startbutton);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }
    public void MydrawPointEnd(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.stopbutton);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }
    public void MydrawPointCurrentLocation(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.currentlocation);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

    }


}
