package com.xiaogang.ChildOne.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.BabyDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.entity.Baby;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuzwei on 2014/11/22.
 */
public class PublishImageActivity extends BaseActivity implements View.OnClickListener{
    private ImageView back;
    private TextView publish;
    private EditText content;
    private Spinner spinner;
    private GridView gridView;
    private ArrayAdapter<String> spinnerAdapter;

    private String babyId;//要发布的宝宝ID
    private Account account;
    private int res[] = new int[]{R.drawable.txhc};
    private List<Baby> babies = new ArrayList<Baby>();//下拉列表宝宝
    private ProgressDialog progressDialog;
    private CheckBox isShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_image_layout);
        initView();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        getBabyList();

        List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
        for(int i=0;i<res.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("imageView",res[i]);
            data.add(map);
        }
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.image_cell_layout , new String[]{"imageView"}, new int[]{R.id.imageView1});
//        gridView.setAdapter(simpleAdapter);
    }

    private void initView(){
        back = (ImageView) findViewById(R.id.publish_image_back);
        publish = (TextView) findViewById(R.id.publish_image_run);
        spinner = (Spinner) findViewById(R.id.publish_image_spinner);
        content = (EditText) findViewById(R.id.publish_image_content);
        gridView = (GridView) findViewById(R.id.publish_image_gv);
        isShare = (CheckBox) findViewById(R.id.publish_image_cb);

        back.setOnClickListener(this);
        publish.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_image_back://后退按钮
                finish();
                break;
            case R.id.publish_image_run://发布按钮
                progressDialog = new ProgressDialog(PublishImageActivity.this);
                progressDialog.setMessage("正在发布，请稍后...");
                push();
                break;
        }
    }

    private void  getBabyList(){
    	 String uri ="";
    	if(HomeBabyApplication.is_student.equals("1")){
    		uri = String.format(InternetURL.GET_BABY_URL + "?uid=%s", account.getUid());
    	}else if(HomeBabyApplication.is_student.equals("0")){
    		uri = String.format(InternetURL.TEACHEAR_GET_BABY_URL+"?uid=%s&class_id=%s", account.getUid(),account.getClass_id());
    	}
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        List<String> names = null;
                        try{
                        	
                        	//{"code":"200","msg":"sucess","data":{"child":[
                        	//{"child_id":"1","uid":"73","name":"\u5fae\u7b11\u5427","cover":"http:\/\/yey4.xqb668.com\/Uploads\/14193855247344.png"},
                        //	{"child_id":"5","uid":"102","name":"\u5927\u5934\u513f\u5b50","cover":"\/Public\/Index\/image\/default_cover.png"},
                        //	{"child_id":"7","uid":"105","name":"Tao","cover":"\/Public\/Index\/image\/default_cover.png"},
                        //	{"child_id":"8","uid":"103","name":"\u5b9d\u5b9d1","cover":"\/Public\/Index\/image\/default_cover.png"}],
                        //	"sclass":{"id":"1","name":"\u7231\u5b9d\u5b9d\u73ed","intro":"adf","school_id":"1"}}}
                        	if(HomeBabyApplication.is_student.equals("1")){
                        		 BabyDATA data = gson.fromJson(s, BabyDATA.class);
                                 babies.addAll(data.getData());
                                 names = new ArrayList<String>();
                                 for (int i=0; i<babies.size(); i++){
                                     names.add(babies.get(i).getName());
                                 }
                        	}else{
                        		JSONObject jsonObject = new JSONObject(s);
                            	String result_code = jsonObject.getString("code");
                            	if(result_code.equals("200")){
                            		JSONArray jsonDatas = jsonObject.getJSONObject("data").getJSONArray("child");
                                	babies = gson.fromJson(jsonDatas.toString(), new TypeToken<List<Baby>>(){}.getType());
                                	names = new ArrayList<String>();
                                     for (int i=0; i<babies.size(); i++){
                                         names.add(babies.get(i).getName());
                                     }
                            	}
                        	}
                        	
                            spinnerAdapter = new ArrayAdapter<String>(PublishImageActivity.this, android.R.layout.simple_spinner_item, names);
                           // spinnerAdapter = new TestArrayAdapter(mContext);
                        	spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinner.setAdapter(spinnerAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Baby baby = babies.get(position);
                                    babyId = baby.getChild_id();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }catch (Exception e){
//                            ErrorDATA data = gson.fromJson(s, ErrorDATA.class);
//                            if (data.getCode() == 500){
//                                Log.i("ErrorData", "获取baby信息数据错误");
//                            }
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

    private void push(){
        final String pushContent = content.getText().toString();
        if (StringUtil.isNullOrEmpty(pushContent)){
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(mContext, "文字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isNullOrEmpty(babyId)){
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Toast.makeText(mContext, "请选择宝宝", Toast.LENGTH_SHORT).show();
            return;
        }
        final String identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GROWING_PUSH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ErrorDATA data = getGson().fromJson(s, ErrorDATA.class);
                        if (data.getCode() == 200){
                            if (progressDialog != null){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(mContext, "网络错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("content",pushContent);
                params.put("uid",account.getUid());
                params.put("user_type", identity);
                params.put("type","0");
                params.put("child_id", babyId);
                if (isShare.isChecked()){
                    params.put("is_share", "1");
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }
}
