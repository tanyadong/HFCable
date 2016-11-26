package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;

public class ToastUtil extends Toast {
    private static boolean isShowFlag = true;
    private static Toast toast;

    public ToastUtil(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }

    /**
     * 字符串类型，短吐丝
     *
     * @param context 上下文
     * @param message CharSequence类型的信息
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShowFlag)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 以"资源ID"为消息的"短显示吐司"
     *
     * @param context 上下文
     * @param message 消息的资源ID
     */
    public static void showShort(Context context, int message) {
        if (isShowFlag)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 以"字符串类型"为消息的"长显示吐司"
     *
     * @param context 上下文
     * @param message "字符串类型的消息"
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShowFlag)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 以"字符串资源ID"为消息的"长显示吐司"
     *
     * @param context 上下文
     * @param message 消息的资源ID
     */
    public static void showLong(Context context, int message) {
        if (isShowFlag)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示位置的吐司, 短吐司, 消息为"字符串资源的ID"
     *
     * @param context   上下文
     * @param messageId 字符串类型的消息
     * @param gravity   参数类型为Int型,表示在屏幕上所处的位置(如Gravity.centre表示处在屏幕中间)
     * @param xOffset   Toast这个View以Gravity.centre位置为参照物相对X轴的偏移量
     * @param yOffset   Toast这个View以Gravity.centre位置为参照物相对Y轴的偏移量
     */
    public static void showCustomLocationShort(Context context, int messageId,
                                               int gravity, int xOffset, int yOffset) {
        if (isShowFlag) {
            Toast toast = Toast
                    .makeText(context, messageId, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    /**
     * 自定义显示位置的吐司, 长吐司, 消息为字符串资源ID
     *
     * @param context   上下文
     * @param messageId 整形int的消息资源的ID值
     * @param gravity   参数类型为Int型,表示在屏幕上所处的位置(如Gravity.centre表示处在屏幕中间)
     * @param xOffset   Toast这个View以Gravity.centre位置为参照物相对X轴的偏移量
     * @param yOffset   Toast这个View以Gravity.centre位置为参照物相对Y轴的偏移量
     */
    public static void showCustomLocationLong(Context context, int messageId,
                                              int gravity, int xOffset, int yOffset) {
        if (isShowFlag) {
            Toast toast = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    /**
     * 自定义显示位置的吐司, 短吐司, 消息为字符串类型
     *
     * @param context 上下文
     * @param message 字符串类型的消息
     * @param gravity 参数类型为Int型,表示在屏幕上所处的位置(如Gravity.centre表示处在屏幕中间)
     * @param xOffset Toast这个View以Gravity.centre位置为参照物相对X轴的偏移量
     * @param yOffset Toast这个View以Gravity.centre位置为参照物相对Y轴的偏移量
     */
    public static void showCustomLocationShort(Context context,
                                               CharSequence message, int gravity, int xOffset, int yOffset) {
        if (isShowFlag) {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    /**
     * 自定义显示位置的吐司, 长吐司, 消息为字符串类型
     *
     * @param context      上下文
     * @param charSequence 字符串类型的消息
     * @param gravity      参数类型为Int型,表示在屏幕上所处的位置(如Gravity.centre表示处在屏幕中间)
     * @param xOffset      Toast这个View以Gravity.centre位置为参照物相对X轴的偏移量
     * @param yOffset      Toast这个View以Gravity.centre位置为参照物相对Y轴的偏移量
     */
    public static void showCustomLocationLong(Context context,
                                              CharSequence charSequence, int gravity, int xOffset, int yOffset) {
        if (isShowFlag) {
            toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();
        }
    }

    /**
     * 这是一个带图片的吐司,其吐司的显示位置定义在了屏幕正中间-->短吐司
     *
     * @param context 上下文
     * @param message 字符串类型的消息
     * @param resId   图片资源ID
     */
    public static void showCustomToastWithImageShort(Context context,
                                                     CharSequence message, int resId) {
        if (isShowFlag) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            LinearLayout ToastContentView = (LinearLayout) toast.getView();
            ImageView img = new ImageView(context);
            img.setImageResource(resId);
            ToastContentView.addView(img);
            toast.show();
        }
    }

    /**
     * 这是一个带图片的吐司,其吐司的显示位置定义在了屏幕正中间-->长吐司
     * @param context 上下文
     * @param message 字符串类型的消息
     * @param resId   图片资源ID
     */
    public static void showCustomToastWithImageLong(Context context,
                                                    CharSequence message, int resId) {
        if (isShowFlag) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout ToastContentView = (LinearLayout) toast.getView();
            ImageView img = new ImageView(context);
            img.setImageResource(resId);
            ToastContentView.addView(img);
            toast.show();
        }
    }

    /**
     * 这是一个带图片的吐司,其吐司的显示位置定义在了屏幕正中间-->短吐司
     * @param context   上下文
     * @param messageId 字符串资源的ID值
     * @param resId     图片资源ID
     */
    public static void showCustomToastWithImageShort(Context context,
                                                     int messageId, int resId) {
        if (isShowFlag) {
            toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout ToastContentView = (LinearLayout) toast.getView();
            ImageView img = new ImageView(context);
            img.setImageResource(resId);
            ToastContentView.addView(img);
            toast.show();
        }
    }

    /**
     * 这是一个带图片的吐司,其吐司的显示位置定义在了屏幕正中间-->长吐司
     *
     * @param context   上下文
     * @param messageId 字符串资源的ID值
     * @param resId     图片资源ID
     */
    public static void showCustomToastWithImageLong(Context context,
                                                    int messageId, int resId) {
        if (isShowFlag) {
            toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout ToastContentView = (LinearLayout) toast.getView();
            ImageView img = new ImageView(context);
            img.setImageResource(resId);
            ToastContentView.addView(img);
            toast.show();
        }
    }



   

    /**
     * 完全自定义的短吐司：所展示出来的视图是自己在res文件夹中自定义的xml布局文件
     *
     * @param context        所在Activity的上下文
     * @param layoutResource 所要加载的XML布局资源文件的ID值
     * @param message        所要通知的文本信息（CharSequence形式）
     * @param imaegId        所要通知的图片的信息（图片资源ID值）
     */
    public static void showCompletedCustomToastShortWithResId(Context context,
                                                              int layoutResource, CharSequence message, int imaegId) {
        if (isShowFlag) {
            toast = new Toast(context);
            LayoutInflater inflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflate.inflate(layoutResource, null);
            TextView mTextView = (TextView) view
                    .findViewById(R.id.tips_msg);
            ImageView mImageView = (ImageView) view
                    .findViewById(R.id.tips_icon);
            mTextView.setText(message);
            mImageView.setImageResource(imaegId);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setView(view);
            toast.setDuration(ToastUtil.LENGTH_SHORT);
            toast.show();
        }
    }
}