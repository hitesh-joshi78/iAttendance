package com.example.iattendance.Utils.Faculty.db;

public class FacultyModalClass {
    String facultyId;
    String collegeCode;
    String course;
    String contact;
    String password;
    String facultyName;


    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public FacultyModalClass(String facultyId, String collegeCode, String course, String contact, String password, String facultyName) {
        this.facultyId = facultyId;
        this.collegeCode = collegeCode;
        this.course = course;
        this.contact = contact;
        this.password = password;
        this.facultyName = facultyName;
    }
}
