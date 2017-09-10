package com.example.asderfers.larva_detection.Controllers;

import com.example.asderfers.larva_detection.DeviceList;

/**
 * Created by akm on 09-Sep-17.
 */
    public interface SyncResponseHandler {
        void onSyncWait();
        void finishSyncWait();
        void onUpdate(DeviceList updatedCourseList);
    }

