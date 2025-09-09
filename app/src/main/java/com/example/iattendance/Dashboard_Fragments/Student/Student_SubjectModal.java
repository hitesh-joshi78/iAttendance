package com.example.iattendance.Dashboard_Fragments.Student;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class Student_SubjectModal {
    String collegeCode, semester, year, subjectType, division;
    String courseName, facultyName, subjectName, batch, batchRange;

    public String getBatchRange() {
        return batchRange;
    }

    public void setBatchRange(String batchRange) {
        this.batchRange = batchRange;
    }

    public Student_SubjectModal(String collegeCode, String semester, String year, String subjectType, String division, String courseName, String facultyName, String subjectName, String batch, String brange) {
        this.collegeCode = collegeCode;
        this.semester = semester;
        this.year = year;
        this.subjectType = subjectType;
        this.division = division;
        this.courseName = courseName;
        this.facultyName = facultyName;
        this.subjectName = subjectName;
        this.batch = batch;
        this.batchRange = brange;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
