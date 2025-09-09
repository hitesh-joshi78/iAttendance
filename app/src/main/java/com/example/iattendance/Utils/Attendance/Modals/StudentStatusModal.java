package com.example.iattendance.Utils.Attendance.Modals;

public class StudentStatusModal {

    String rollNo;
    String studImage;
    String studStatus;

    public StudentStatusModal(String rollNo, String studImage, String studStatus) {
        this.rollNo = rollNo;
        this.studImage = studImage;
        this.studStatus = studStatus;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStudImage() {
        return studImage;
    }

    public void setStudImage(String studImage) {
        this.studImage = studImage;
    }

    public String getStudStatus() {
        return studStatus;
    }

    public void setStudStatus(String studStatus) {
        this.studStatus = studStatus;
    }
}
