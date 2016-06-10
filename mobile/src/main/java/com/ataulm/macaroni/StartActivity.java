package com.ataulm.macaroni;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.view.View;

public class StartActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_DIR = 456;
    private static final int REQUEST_CODE_PICK_DOC = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void findAudio(View view) {
        startActivityForResult(pickDocIntent(), REQUEST_CODE_PICK_DOC);
    }

    private static Intent pickDocIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        return intent;
    }

    public void findAudioDir(View view) {
        startActivityForResult(pickDirIntent(), REQUEST_CODE_PICK_DIR);
    }

    private static Intent pickDirIntent() {
        return new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            onActivityResult(requestCode, data.getData());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResult(int requestCode, Uri data) {
        if (requestCode == REQUEST_CODE_PICK_DIR) {
            // TODO: https://github.com/googlesamples/android-DirectorySelection
            log("dir: " + data);
        } else if (requestCode == REQUEST_CODE_PICK_DOC) {
            dumpImageMetaData(data);
        }
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
            log("Display Name: " + displayName);

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
            log("Size: " + size);
        }
    }

    private void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

}
