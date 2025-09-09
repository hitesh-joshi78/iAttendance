package com.example.iattendance.Utils.Attendance;

import com.example.iattendance.Utils.Attendance.Modals.AttendanceHistoryModal;

import java.util.HashMap;

public interface AttendanceHistoryInterface {

    void getAttendanceDHistory(HashMap<String, AttendanceHistoryModal> passedData);

    void isCheckingStatus(boolean status);

}
