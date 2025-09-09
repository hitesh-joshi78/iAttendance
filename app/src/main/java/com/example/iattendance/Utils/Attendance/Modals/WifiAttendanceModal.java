package com.example.iattendance.Utils.Attendance.Modals;

import java.util.HashMap;
import java.util.Objects;

public class WifiAttendanceModal {
    String collegeCode, semYear, subCode;
    String date, time, rollNo, studentImg;
    HashMap<String, Object> attendance;

    public WifiAttendanceModal(String collegeCode, String semYear, String subCode, String date, String time, String rollNo, HashMap<String, Object> attendance, String studentImg) {
        this.collegeCode = collegeCode;
        this.semYear = semYear;
        this.subCode = subCode;
        this.date = date;
        this.time = time;
        this.rollNo = rollNo;
        this.studentImg = studentImg;
        this.attendance = attendance;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getStudentImg() {
        return studentImg;
    }

    public void setStudentImg(String studentImg) {
        this.studentImg = studentImg;
    }

    public String getSemYear() {
        return semYear;
    }

    public void setSemYear(String semYear) {
        this.semYear = semYear;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public HashMap<String, Object> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String, Object> attendance) {
        this.attendance = attendance;
    }
}
