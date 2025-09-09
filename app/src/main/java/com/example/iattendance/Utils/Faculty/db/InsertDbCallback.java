package com.example.iattendance.Utils.Faculty.db;

import java.util.Map;

public interface InsertDbCallback {
    void onInsertComplete(boolean success);

    void onDataRetrieval(Map<String, String> data);
}
