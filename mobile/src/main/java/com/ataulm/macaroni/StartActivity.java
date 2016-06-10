package com.ataulm.macaroni;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class StartActivity extends BaseActivity {

    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void findAudio(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            onActivityResult(resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResult(int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            onActivityResult(data);
        } else {
            toast("resultCode ok? " + (resultCode == RESULT_OK) + " data ok? " + (data != null));
        }
    }

    private void onActivityResult(Intent data) {
        Uri uri = data.getData();
        toast(uri.toString());
        dumpImageMetaData(uri);
    }

    public void dumpImageMetaData(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
        try {
            dumpDataToLogsFrom(cursor);
        } finally {
            close(cursor);
        }
    }

    private void dumpDataToLogsFrom(Cursor cursor) {
        // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (cursor != null && cursor.moveToFirst()) {
            // Note it's called "Display Name".  This is
            // provider-specific, and might not necessarily be the file name.
            String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            Log.i("macaroni", "Display Name: " + displayName);

            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            // If the size is unknown, the value stored is null.  But since an
            // int can't be null in Java, the behavior is implementation-specific,
            // which is just a fancy term for "unpredictable".  So as
            // a rule, check if it's null before assigning to an int.  This will
            // happen often:  The storage API allows for remote files, whose
            // size might not be locally known.
            String size = null;
            if (!cursor.isNull(sizeIndex)) {
                // Technically the column stores an int, but cursor.getString()
                // will do the conversion automatically.
                size = cursor.getString(sizeIndex);
            } else {
                size = "Unknown";
            }
            Log.i("macaroni", "Size: " + size);
        }
    }

    private void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

}
