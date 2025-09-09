package com.example.iattendance.Utils.Attendance.Modals;

public class StudAttendanceModal {

    String Attendance, FacultyName, Roll_no, subjectName, subjectType, batch, subjectCode;

    public StudAttendanceModal(String attendance, String facultyName, String roll_no, String subjectName, String subjectType, String batch, String subCode) {
        Attendance = attendance;
        FacultyName = facultyName;
        Roll_no = roll_no;
        this.subjectName = subjectName;
        this.subjectType = subjectType;
        this.batch = batch;
        this.subjectCode = subCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public String getFacultyName() {
        return FacultyName;
    }

    public void setFacultyName(String facultyName) {
        FacultyName = facultyName;
    }

    public String getRoll_no() {
        return Roll_no;
    }

    public void setRoll_no(String roll_no) {
        Roll_no = roll_no;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
