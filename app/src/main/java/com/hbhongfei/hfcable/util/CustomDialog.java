package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;


/**
 * Created by 苑雪元 on 2016/06/16.
 * 显示自定义对话框
 */
public class CustomDialog {

    private static AlertDialog dialog;

    public static void showOkOrCancleDialog(Context context, String message,final IAlertDialogListener listener) {
        View dialogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Dialog_FS);
        dialogView = LayoutInflater.from(context).inflate(R.layout.ok_cancle_dialog, null);

        TextView dialog_message = (TextView) dialogView.findViewById(R.id.dialog_message);
        TextView dialog_cancel = (TextView) dialogView.findViewById(R.id.dialog_cancel);
        TextView dialog_comfire = (TextView) dialogView.findViewById(R.id.dialog_comfire);

        dialog_message.setText(message);

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog_comfire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                /*事件回调*/
                if (listener != null) {
                    listener.onSureClick();
                }
            }
        });


        /*为dialog设置View*/
        builder.setView(dialogView);
        dialog = builder.create();

        /*显示对话框*/
        dialog.show();
    }


    public static void selectHead(Context context,final IAlertDialogListener listener) {
        View dialogView = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Dialog_FS);
        dialogView = LayoutInflater.from(context).inflate(R.layout.album_select_dialog, null);

        TextView dialog_selectFromAlbum = (TextView) dialogView.findViewById(R.id.dialog_selectFromAlbum);
        TextView dialog_photo = (TextView) dialogView.findViewById(R.id.dialog_photo);
        TextView dialog_album_cancel = (TextView) dialogView.findViewById(R.id.dialog_album_cancel);
        //取消
        dialog_album_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //从相册中选择
        dialog_selectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                /*事件回调*/
                if (listener != null) {
                    listener.selectFromAlbumClick();
                }
            }
        });

        //拍照
        dialog_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }

                /*事件回调*/
                if (listener != null) {
                    listener.photoClick();
                }
            }
        });


        /*为dialog设置View*/
        builder.setView(dialogView);
        dialog = builder.create();

        /*显示对话框*/
        dialog.show();
    }

//
//    public static void showSelectDialog(Context context, String message, String message2, String message3,
//                                        final IAlertDialogListener listener1, final IAlertDialogListener listener2,
//                                        final IAlertDialogListener listener3) {
//        View dialogView = null;
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_selecct, null);
//
//        TextView takePhotoTxt = (TextView) dialogView.findViewById(R.id.takePhotoTxt);
//        TextView selectInAlbumTxt = (TextView) dialogView.findViewById(R.id.selectInAlbumTxt);
//        TextView lookBigTxt = (TextView) dialogView.findViewById(R.id.lookBigTxt);
//
//        takePhotoTxt.setText(message);
//        selectInAlbumTxt.setText(message2);
//        lookBigTxt.setText(message3);
//
//        takePhotoTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//
//                /*事件回调*/
//                if (listener1 != null) {
//                    listener1.onClick();
//                }
//            }
//        });
//        selectInAlbumTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//                /*事件回调*/
//                if (listener2 != null) {
//                    listener2.onClick();
//                }
//            }
//        });
//
//        lookBigTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//                /*事件回调*/
//                if (listener3 != null) {
//                    listener3.onClick();
//                }
//            }
//        });
//        /*为dialog设置View*/
//        builder.setView(dialogView);
//        dialog = builder.create();
//        /*显示对话框*/
//        dialog.show();
//    }
//
//
//    /*发表说说选择对话框*/
//    public static void showDialog(Context context, String message, String message2,
//                                  final IAlertDialogListener listener1, final IAlertDialogListener listener2) {
//        View dialogView = null;
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_selecct_talk, null);
//
//        TextView takePhotoTxt = (TextView) dialogView.findViewById(R.id.takePhotoTxt);
//        TextView selectInAlbumTxt = (TextView) dialogView.findViewById(R.id.selectInAlbumTxt);
//
//        takePhotoTxt.setText(message);
//        selectInAlbumTxt.setText(message2);
//
//        takePhotoTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//
//                /*事件回调*/
//                if (listener1 != null) {
//                    listener1.onClick();
//                }
//            }
//        });
//        selectInAlbumTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//                /*事件回调*/
//                if (listener2 != null) {
//                    listener2.onClick();
//                }
//            }
//        });
//
//        /*为dialog设置View*/
//        builder.setView(dialogView);
//        dialog = builder.create();
//        /*显示对话框*/
//        dialog.show();
//    }
//
//
//    public static void showAddressSelectDialog(Context context,
//                                               final IAlertDialogListener listener) {
//        View dialogView = null;
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        dialogView = LayoutInflater.from(context).inflate(R.layout.custom_addressselect_dialog, null);
//
//        TextView dialog_comfirm = (TextView) dialogView.findViewById(R.id.dialog_comfirm);
//
//        dialog_comfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//
//                /*事件回调*/
//                if (listener != null) {
//                    listener.onClick();
//                }
//            }
//        });
//
//
//        /*为dialog设置View*/
//        builder.setView(dialogView);
//        dialog = builder.create();
//
//        /*显示对话框*/
//        dialog.show();
//    }
}
