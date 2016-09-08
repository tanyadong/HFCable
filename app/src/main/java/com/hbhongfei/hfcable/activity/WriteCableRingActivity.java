package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ImagePublishAdapter;
import com.hbhongfei.hfcable.pojo.ImageItem;
import com.hbhongfei.hfcable.util.CustomConstants;
import com.hbhongfei.hfcable.util.IntentConstants;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/*
发表说说
 */
public class WriteCableRingActivity extends AppCompatActivity{
    private GridView mGridView;
    private EditText text;
    private ImagePublishAdapter mAdapter;
    private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private String S_text,S_phoneNumber;
    private static final String USER = LoginConnection.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_cable_ring);

        Toast.makeText(WriteCableRingActivity.this, "WriteCableRingActivity", Toast.LENGTH_SHORT).show();
        //初始化数据
        initData();
        //初始化组件
        initView();
    }

    /**
     * 初始化界面
     */
    public void initView() {
        text = (EditText) findViewById(R.id.Etext_write_cable_ring);
        TextView titleTv = (TextView) findViewById(R.id.title_write_cable_ring);
        titleTv.setText("");
        mGridView = (GridView) findViewById(R.id.gridview_write_cable_ring);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if (position == getDataSize()) {
                    new PopupWindows(WriteCableRingActivity.this, mGridView);
                } else {
                    Intent intent = new Intent(WriteCableRingActivity.this,ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,(Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);
                }
            }
        });
        sendTv = (TextView) findViewById(R.id.action);
        sendTv.setText("发送");
        sendTv.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(WriteCableRingActivity.this,"11111111111",Toast.LENGTH_SHORT).show();
                S_text = text.getText().toString().trim();
                connect();
                removeTempFromPref();

                //TODO 这边以mDataList为来源做上传的动作
            }
        });
    }

    /**
     * 暂存数据
     */
    private void setValues(){
        S_text = text.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().putString(CustomConstants.PREF_TEMP_TEXT, S_text).commit();
    }

    /**
     * 初始化数据
     */
    @SuppressWarnings("unchecked")
    private void initData() {
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        getTempFromPref();
        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent().getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        if (incomingDataList != null) {
            mDataList.addAll(incomingDataList);
        }
    }


    /**
     * 保存缆圈信息
     */
    private void connect() {
        String url = Url.url("/androidCableRing/save");

        RequestQueue mQueue = Volley.newRequestQueue(this);
        Map<String, String> map = new HashMap<>();
        map.put("content", S_text);
        map.put("userName", S_phoneNumber);
        if (mDataList.size()!=0){
            String [] images = new String[mDataList.size()-1];
            for (int i=0;i<mDataList.size();i++){
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(mDataList.get(i).sourcePath);
                    //转换字符流
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    baos.close();
                    byte[] buffer = baos.toByteArray();
                    System.out.println("图片的大小："+buffer.length);
                    //将图片的字节流数据加密成base64字符输出
                    String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT);
                    map.put("image"+i,photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectAddCableRingListener, errorListener, map);
        mQueue.add(normalPostRequest);
    }

    /**
     * 添加购物车成功的监听器
     * 返回的添加购物车的状态信息
     */
    private Response.Listener<JSONObject> jsonObjectAddCableRingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("save");
                if (TextUtils.equals(msg, "success")) {
                    Toast.makeText(WriteCableRingActivity.this,"success",Toast.LENGTH_SHORT).show();
                    removeTempFromPref();
                } else {
                    Toast.makeText(WriteCableRingActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };
    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(WriteCableRingActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

    protected void onPause() {
        super.onPause();
        saveTempToPref();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveTempToPref();
    }
    /**
     * 保存照片
     */
    private void saveTempToPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = JSON.toJSONString(mDataList);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();

    }

    /**
     * 获取照片
     */
    private void getTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
        if (!TextUtils.isEmpty(prefStr)) {
            List<ImageItem> tempImages = JSON.parseArray(prefStr, ImageItem.class);
            mDataList = tempImages;
        }
    }

    /**
     * 清除照片缓存
     */
    private void removeTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
    }



    @Override
    protected void onResume() {
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
    }


    /**
     * 获取数据的大小
     * @return
     */
    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private int getAvailableSize() {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    public String getString(String s) {
        String path = null;
        if (s == null) return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }
    /**
     * 弹窗
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

            setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    takePhoto();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(WriteCableRingActivity.this,ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,getAvailableSize());
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()+ "/myimage/", String.valueOf(System.currentTimeMillis())+ ".jpg");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE&& resultCode == -1 && !TextUtils.isEmpty(path)) {
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    mDataList.add(item);
                }
                break;
        }
    }

    private void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

}
