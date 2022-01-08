package com.pmkisanyojana.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class FireJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {

        switch (tag) {
            case Prevalent.JOB_TAG_DELETE_STATUS:
                return new CommonMethod();
            default:
                return null;
        }
    }
}


