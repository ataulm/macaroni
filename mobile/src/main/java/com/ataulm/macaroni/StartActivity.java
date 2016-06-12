package com.ataulm.macaroni;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_DIR = 456;

    private DirectoryAdapter directoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        directoryAdapter = new DirectoryAdapter(new DirectoryItemViewHolder.Listener() {
            @Override
            public void onClick(DocumentInfo item) {
                toast("clicked " + item.getName());
            }
        });

        RecyclerView directoryView = ButterKnife.findById(this, R.id.directory_recycler_view);
        directoryView.setLayoutManager(new LinearLayoutManager(this));
        directoryView.setAdapter(directoryAdapter);
    }

    public void findAudioDir(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_CODE_PICK_DIR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            onActivityResult(requestCode, data.getData());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onActivityResult(int requestCode, Uri data) {
        if (requestCode == REQUEST_CODE_PICK_DIR) {
            updateDirectoryEntries(data);
        }
    }

    private void updateDirectoryEntries(Uri uri) {
        DocumentInfoProvider documentInfoProvider = new DocumentInfoProvider(getContentResolver());
        List<DocumentInfo> documentsInDirectory = documentInfoProvider.getDocumentsInDirectory(uri);
        directoryAdapter.updateItems(documentsInDirectory);
    }

}
