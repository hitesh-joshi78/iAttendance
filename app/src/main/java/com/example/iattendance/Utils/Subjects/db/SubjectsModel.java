package com.example.iattendance.Utils.Subjects.db;

public class SubjectsModel {
    String Batch, Division, Semester, Subject_code, Subject, Subject_type, Year, Range, facultyName;
    int Class_completed;

    public SubjectsModel() {
    }


    public SubjectsModel(String batch, String division, String semester, String subject_code, String subject, String subject_type, String year, int class_completed, String batchRange, String profName) {
        Batch = batch;
        Division = division;
        Semester = semester;
        Subject_code = subject_code;
        Subject = subject;
        Subject_type = subject_type;
        Year = year;
        Class_completed = Class_completed;
        Range = batchRange;
        facultyName = profName;
    }

    public String getRange() {
        return Range;
    }

    public void setRange(String range) {
        Range = range;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getBatchRange() {
        return Range;
    }

    public void setBatchRange(String batchRange) {
        this.Range = batchRange;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getSubject_code() {
        return Subject_code;
    }

    public void setSubject_code(String subject_code) {
        Subject_code = subject_code;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getSubject_type() {
        return Subject_type;
    }

    public void setSubject_type(String subject_type) {
        Subject_type = subject_type;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getClass_completed() {
        return Class_completed;
    }

    public void setClass_completed(int Class_completed) {
        this.Class_completed = Class_completed;
    }
}