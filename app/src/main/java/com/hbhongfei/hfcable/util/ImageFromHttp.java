package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageFromHttp {
    Context context;
    String url;

    public static void getImg(final Context context, final String url, final ImageView imageView){
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest request=new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                ImageFromFileCache.saveBitmap(url, bitmap);
                ImageFromMemoryCache.addBitmapToCache(url, bitmap);

                imageView.setImageBitmap(bitmap);

            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }


//    private static final String LOG_TAG = "ImageGetFromHttp";
//    public static Bitmap downloadBitmap(String url) {
//        final HttpClient client = new DefaultHttpClient();
//        final HttpGet getRequest = new HttpGet(url);
//
//        try {
//            HttpResponse response = client.execute(getRequest);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                Log.w(LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
//                return null;
//            }
//
//            final HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream inputStream = null;
//                try {
//                    inputStream = entity.getContent();
//                    FilterInputStream fit = new FlushedInputStream(inputStream);
//                    return BitmapFactory.decodeStream(fit);
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                        inputStream = null;
//                    }
//                    entity.consumeContent();
//                }
//            }
//        } catch (IOException e) {
//            getRequest.abort();
//            Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
//        } catch (IllegalStateException e) {
//            getRequest.abort();
//            Log.w(LOG_TAG, "Incorrect URL: " + url);
//        } catch (Exception e) {
//            getRequest.abort();
//            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
//        } finally {
//            client.getConnectionManager().shutdown();
//        }
//        return null;
//    }
                                                       
    /*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}