package com.example.iattendance.Utils.Subjects.db;

import com.example.iattendance.Dashboard_Fragments.Student.Student_SubjectModal;

import java.util.ArrayList;

public interface subjectInterface {

    void getSemesterList(ArrayList<String> semesterList);

    void getStudentSemesterList(ArrayList<Student_SubjectModal> semesterList);

    void getFacultyCodes(ArrayList<String> facultyCodeList);

    void isConfirmed(boolean val);

    


}
