package com.ataulm.macaroni;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.DocumentsContract.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DocumentInfoProvider {

    private static final String[] PROJECTION_DOCUMENT_INFO = Column.projection();

    private final ContentResolver contentResolver;
    DocumentInfoProvider(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    List<DocumentInfo> getDocumentsInDirectory(Uri directoryUri) {
        Uri childDocumentsUri = createUriForChildDocuments(directoryUri);
        Cursor childDocumentsCursor = queryForDocuments(childDocumentsUri);
        try {
            return marshallCursorToList(childDocumentsCursor);
        } finally {
            close(childDocumentsCursor);
        }
    }

    private Uri createUriForChildDocuments(Uri uri) {
        String treeDocumentId = DocumentsContract.getTreeDocumentId(uri);
        return DocumentsContract.buildChildDocumentsUriUsingTree(uri, treeDocumentId);
    }

    private Cursor queryForDocuments(Uri childDocumentsUri) {
        return contentResolver.query(childDocumentsUri, PROJECTION_DOCUMENT_INFO, null, null, null);
    }

    private List<DocumentInfo> marshallCursorToList(Cursor cursor) {
        if (empty(cursor)) {
            return Collections.emptyList();
        }

        cursor.moveToFirst();
        List<DocumentInfo> items = new ArrayList<>();
        do {
            DocumentInfo entry = new DocumentInfo(
                    getString(Column.DOCUMENT_ID, cursor),
                    getString(Column.DISPLAY_NAME, cursor),
                    getString(Column.MIME_TYPE, cursor)
            );

            items.add(entry);
        } while (cursor.moveToNext());

        return items;
    }

    private String getString(Column column, Cursor cursor) {
        return cursor.getString(column.positionProjection());
    }

    private boolean empty(Cursor childDocumentsCursor) {
        return childDocumentsCursor == null || childDocumentsCursor.getCount() == 0;
    }

    private void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    private enum Column {

        DOCUMENT_ID(Document.COLUMN_DOCUMENT_ID),
        DISPLAY_NAME(Document.COLUMN_DISPLAY_NAME),
        MIME_TYPE(Document.COLUMN_MIME_TYPE);

        private final String name;

        Column(String name) {
            this.name = name;
        }

        public static String[] projection() {
            String[] projection = new String[values().length];
            for (int i = 0; i < values().length; i++) {
                projection[i] = values()[i].name;
            }
            return projection;
        }

        public int positionProjection() {
            return ordinal();
        }

    }

}
