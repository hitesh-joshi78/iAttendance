package com.example.iattendance.Utils.Attendance.Modals;

public class AttendanceHistoryModal {
    String rollNo, status, studAttTime, studAttDate;

    public AttendanceHistoryModal(String rollNo, String status, String studAttTime, String studAttDate) {
        this.rollNo = rollNo;
        this.status = status;
        this.studAttTime = studAttTime;
        this.studAttDate = studAttDate;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudAttTime() {
        return studAttTime;
    }

    public void setStudAttTime(String studAttTime) {
        this.studAttTime = studAttTime;
    }

    public String getStudAttDate() {
        return studAttDate;
    }

    public void setStudAttDate(String studAttDate) {
        this.studAttDate = studAttDate;
    }
}
