package com.example.iattendance.Utils.Subjects.Validation;

import android.content.Context;
import android.text.TextUtils;

public class subjectValidation {

    Context context;

    public subjectValidation(Context context) {
        this.context = context;
    }

    public boolean isEmptyField(String value) {
        return !TextUtils.isEmpty(value);
    }



}
