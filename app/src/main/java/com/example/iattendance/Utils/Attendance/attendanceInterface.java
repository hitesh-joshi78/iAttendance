package com.example.iattendance.Utils.Attendance;

import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.Modals.WifiAttendanceModal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface attendanceInterface {

    void getStudentAttendance(ArrayList<StudAttendanceModal> list);

    void isCheckingAttendance(boolean status);
    void getWifiData(Map<String, Object> attendanceData) throws IOException;


}
