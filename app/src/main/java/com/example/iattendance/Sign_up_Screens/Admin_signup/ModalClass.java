package com.example.iattendance.Sign_up_Screens.Admin_signup;

public class ModalClass {
    String adminName, phoneNumber, password, collegeCode, collegeName, collegeAddress;

    // Required public no-argument constructor
    public ModalClass() {
        // Default constructor required for Firebase Firestore deserialization
    }

    public ModalClass(String adminName, String phoneNumber, String password, String collegeCode, String collegeName, String collegeAddress) {
        this.adminName = adminName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.collegeCode = collegeCode;
        this.collegeName = collegeName;
        this.collegeAddress = collegeAddress;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeAddress() {
        return collegeAddress;
    }

    public void setCollegeAddress(String collegeAddress) {
        this.collegeAddress = collegeAddress;
    }
}
