package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.CableRingAdapter;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> list;
    private Map<String, Object> map;
    private Dialog dialog;
    private CableRingAdapter cableRingAdapter;

    public MineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(v);
        setOnClick();
//        //初始化数据
        initValues();
        return v;
    }

    @Override
    public void onResume() {
//        initValues();
        super.onResume();
    }


    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     */
    public void initView(View v) {
        dialog = new Dialog(MineFragment.this.getActivity());
        mListView = (ListView) v.findViewById(R.id.ListView_cableZone);
    }

    /**
     * 设置数据
     */
    private void setValues() {

    }

    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick() {
    }


    /**
     * 初始化数据
     * 苑雪元
     * 2016/07/21
     */
    private void initValues() {
        shaftConnInter();
    }

    /**
     * 跳转页面
     * 苑雪元
     * 2016/07/21
     */
    private void intent(Class c) {
        Intent i = new Intent();
        i.setClass(this.getActivity(), c);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
    }

    /**
     * 获取产品种类服务
     */
    public void shaftConnInter() {
        dialog.showDialog("正在记载中。。。");
        String url = Url.url("/androidCableRing/list");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, shaftjsonObjectListener, errorListener);
        RequestQueue mQueue = Volley.newRequestQueue(MineFragment.this.getActivity());
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> shaftjsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            list = new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("cableRings");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject cableRing = (JSONObject) jsonArray.getJSONObject(i);
                    String id = cableRing.getString("id");
                    map.put("id", id);
                    String content = cableRing.getString("content");
                    map.put("content", content);
                    String createTime = cableRing.getString("createTime");
                    map.put("createTime", createTime);

                    //图片
                    JSONArray ims=cableRing.getJSONArray("cableRingImages");
                    //有图片时加入到产品图片集合
                    ArrayList<String> images=new ArrayList<>();
                    if(ims.length()>0){
                        for(int j=0;j<ims.length();j++){
                            images.add((String) ims.get(j));
                        }
                    }
                    map.put("images", images);
                    JSONObject user = cableRing.getJSONObject("user");
                    String nickName = user.getString("nickName");
                    map.put("nickName", nickName);
                    String headPortrait = user.getString("headPortrait");
                    map.put("headPortrait", headPortrait);
                    list.add(map);
                }
                cableRingAdapter = new CableRingAdapter(MineFragment.this.getContext(), list);
                mListView.setAdapter(cableRingAdapter);
                dialog.cancle();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
            }
        }
    };

    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MineFragment.this.getActivity(), "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };


    /**
     * 选择拍照还是相册
     */
    private void showDialog() {
        /*if (tag==0){
            drawable = MineFragment.this.getActivity().getResources().getDrawable(R.mipmap.man);
        }else{
            drawable =new BitmapDrawable(b);
        }
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("选择途径")
                .setContentText("一张美美的头像")
                .setCancelText("拍照")
                .setConfirmText("相册")
                //设置标题图片
                .setCustomImage(drawable)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.PHOTONAME)));
                        startActivityForResult(intent, Constants.TAKE_PHOTO);
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //相册中选择
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image");
                        startActivityForResult(intent, Constants.PICK_PHOTO);
                        sDialog.cancel();
                    }
                })
                .show();*/
    }
//    @Override
//    public   void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == Constants.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(MineFragment.this.getActivity(), ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",Constants.PHOTONAME);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = MineFragment.this.getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(MineFragment.this.getActivity(), ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",picturePath);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.CROP_BEAUTY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                 b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    tag++;
                    myhead.setImageBitmap(b);
                    //上传
                    upLoadImage.load(b,S_phoneNumber);
                }
            }
        }*/
//    }
}
