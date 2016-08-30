package com.hbhongfei.hfcable.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.hbhongfei.hfcable.activity.InputMyInfoActivity;

import java.util.Calendar;

/**
 * 选择日期
 * Created by 苑雪元 on 2015/10/8.
 */
public class DatepickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private InputMyInfoActivity activity;
    private Class c;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity= (InputMyInfoActivity) getActivity();
        super.onCreate(savedInstanceState);
    }
    //用于创建日期对话框
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c= Calendar.getInstance();
       int year= c.get(Calendar.YEAR);
       int month= c.get(Calendar.MONTH);
       int date= c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(getActivity(),this,year,month,date);
        return dialog;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        activity.setDate(year,monthOfYear,dayOfMonth);

    }
}
