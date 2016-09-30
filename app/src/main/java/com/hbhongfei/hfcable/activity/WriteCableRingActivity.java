package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ImagePublishAdapter;
import com.hbhongfei.hfcable.pojo.ImageItem;
import com.hbhongfei.hfcable.util.CustomConstants;
import com.hbhongfei.hfcable.util.CustomDialog;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.IAlertDialogListener;
import com.hbhongfei.hfcable.util.IntentConstants;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.MySingleton;
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

/*
发表说说
 */
public class WriteCableRingActivity extends AppCompatActivity implements IAlertDialogListener{
    private GridView mGridView;
    private EditText text;
    private ImagePublishAdapter mAdapter;
    private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<>();
    private String S_text,S_phoneNumber;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private String tag;
    private Map<String, String> map;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_cable_ring);

        //初始化数据
        initData();
        //初始化组件
        initView();
    }

    /**
     * 初始化数据
     */
    @SuppressWarnings("unchecked")
    private void initData() {
        Intent intent = getIntent();
        if (intent!=null){
            tag = intent.getStringExtra("tag");
            if (tag!=null&&!tag.equals("")){
                mDataList.clear();
                removeTextTempFromPref();
            }
        }
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        getTempFromPref();
        List<ImageItem> incomingDataList = (List<ImageItem>)intent.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        if (incomingDataList != null) {
            mDataList.addAll(incomingDataList);
        }
        super.onNewIntent(intent);
    }

    /**
     * 初始化界面
     */
    public void initView() {
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog = new Dialog(this);
        text = (EditText) findViewById(R.id.Etext_write_cable_ring);
        mGridView = (GridView) findViewById(R.id.gridview_write_cable_ring);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if (position == getDataSize()) {
                    //关闭软键盘
                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    CustomDialog.selectHead(WriteCableRingActivity.this,WriteCableRingActivity.this);
                } else {
                    Intent intent = new Intent(WriteCableRingActivity.this,ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,(Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);
                }
            }
        });
        getValues();
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
     * 展示数据
     */
    private void getValues(){
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String t =  sp.getString(CustomConstants.PREF_TEMP_TEXT,null);
        if (t!=null){
            text.setText(t);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_cable_ring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_write){
            S_text = text.getText().toString().trim();
            connect(S_text);
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 保存缆圈信息
     */
    private void connect(String s) {
        dialog.showDialog("正在发布....");
        if (s.equals("")&&mDataList.size()==0){
            Toast.makeText(this,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else{
            String url = Url.url("/androidCableRing/save");
            map = new HashMap<>();
            if (s!=null||!s.equals("")){
                map.put("content", s);
            }
            map.put("userName", S_phoneNumber);
            if (mDataList.size()!=0){
                Toast.makeText(this,"wwww",Toast.LENGTH_SHORT).show();
                String [] images = new String[mDataList.size()-1];
                Toast.makeText(WriteCableRingActivity.this,mDataList.size()+"",Toast.LENGTH_SHORT).show();
//                new BitmapThread().start();
                /*for (int i=0;i<mDataList.size();i++) {
                        Bitmap bitmap = BitmapFactory.decodeFile(mDataList.get(i).sourcePath);
                        //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                        //转换字符流
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        int options = 100;
                        while ( options>0&&baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                            baos.reset();//重置baos即清空baos
                            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                            options -= 10;//每次都减少10
                        }
                        byte[] buffer = baos.toByteArray();
                        System.out.println("图片的大小：" + buffer.length);
                        //将图片的字节流数据加密成base64字符输出
                        String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                        map.put("image" + i, photo);
                }*/

                new bitmapTask().execute("");
            }
            NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectAddCableRingListener, errorListener, map);
            MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
        }
    }
    class bitmapTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i=0;i<mDataList.size();i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(mDataList.get(i).sourcePath);
                //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                //转换字符流
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                int options = 100;
                while ( options>0&&baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                    baos.reset();//重置baos即清空baos
                    bitmap.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                    options -= 10;//每次都减少10
                }
                byte[] buffer = baos.toByteArray();
                System.out.println("图片的大小：" + buffer.length);
                //将图片的字节流数据加密成base64字符输出
                String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                map.put("image" + i, photo);
            }
            return "success";
        }
    }


    class BitmapThread extends Thread{
        @Override
        public void run() {
            for (int i=0;i<mDataList.size();i++) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(mDataList.get(i).sourcePath);
                    //转换字符流
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    baos.close();
                    byte[] buffer = baos.toByteArray();
                    System.out.println("图片的大小：" + buffer.length);
                    //将图片的字节流数据加密成base64字符输出
                    String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                    map.put("image" + i, photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发表缆圈成功后的状态
     */
    private Response.Listener<JSONObject> jsonObjectAddCableRingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("save");
                if (TextUtils.equals(msg, "success")) {
                    Toast.makeText(WriteCableRingActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                    SplashActivity.ID = 4;
                    removeTempFromPref();
                    removeTextTempFromPref();
                    Intent intent = new Intent(WriteCableRingActivity.this,MainActivity.class);
                    startActivity(intent);
                    dialog.cancle();
                } else {
                    Toast.makeText(WriteCableRingActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
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
            dialog.cancle();
            Toast.makeText(WriteCableRingActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

    protected void onPause() {
        super.onPause();
        saveTempToPref();
        setValues();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveTempToPref();
        setValues();
    }
    /**
     * 保存照片
     */
    private void saveTempToPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        if (sp!=null){
            String prefStr = JSON.toJSONString(mDataList);
            sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();
        }

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

    /**
     * 清除文字缓存
     */
    private void removeTextTempFromPref() {
        SharedPreferences sp = getSharedPreferences(CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_TEXT).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeTempFromPref();
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

    @Override
    public void onSureClick() {

    }

    /**
     * 选择照片
     */
    @Override
    public void selectFromAlbumClick() {
        Intent intent = new Intent(WriteCableRingActivity.this,ImageBucketChooseActivity.class);
        intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,getAvailableSize());
        startActivity(intent);
    }

    /**
     * 拍照
     */
    @Override
    public void photoClick() {
        takePhoto();
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
