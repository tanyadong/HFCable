package com.hbhongfei.hfcable.util;

import android.content.Context;

import com.baidu.wallet.core.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CaheInterceptor implements Interceptor {
    private Context context;

public CaheInterceptor(Context context) {
    this.context = context;
}

@Override
public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
//    if (NetUtils.isConnected(context)) {
//        Response response = chain.proceed(request);
//        int maxAge = 300;
//        String cacheControl = request.cacheControl().toString();
//        Log.e("Tamic", maxAge+ "s load cahe:" + cacheControl);
//        return response.newBuilder()
//                .removeHeader("Pragma")
//                .removeHeader("Cache-Control")
//                .header("Cache-Control", "public, max-age=" + maxAge)
//                .build();
//    } else {
//        Log.e("Tamic", " no network load cahe");
//        request = request.newBuilder()
//                .cacheControl(CacheControl.FORCE_CACHE)
//                .build();
//        Response response = chain.proceed(request);
//        //set cahe times is 3 days
//        int maxStale = 60 * 60 * 24 * 3;
//        return response.newBuilder()
//                .removeHeader("Pragma")
//                .removeHeader("Cache-Control")
//                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                .build();
//    }
    //如果没有网络，则启用 FORCE_CACHE
    if (!NetworkUtils.isNetworkConnected(context))
    {
        request = request
                .newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
    }
    Response originalResponse = chain.proceed(request);
    return originalResponse
            .newBuilder()
            //在这里生成新的响应并修改它的响应头
            .header("Cache-Control", "public,max-age=3600")
            .removeHeader("Pragma").build();
}
}