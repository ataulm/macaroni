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

    List<DocumentInfo> getDocumentsInDirectory(Uri directoryTreeUri) {
        Uri childDocumentsUri = createUriForChildDocuments(directoryTreeUri);
        Cursor childDocumentsCursor = queryForDocuments(childDocumentsUri);
        try {
            return marshallCursorToList(directoryTreeUri, childDocumentsCursor);
        } finally {
            close(childDocumentsCursor);
        }
    }

    private Uri createUriForChildDocuments(Uri treeUri) {
        String treeDocumentId = DocumentsContract.getTreeDocumentId(treeUri);
        return DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, treeDocumentId);
    }

    private Cursor queryForDocuments(Uri childDocumentsUri) {
        return contentResolver.query(childDocumentsUri, PROJECTION_DOCUMENT_INFO, null, null, null);
    }

    private List<DocumentInfo> marshallCursorToList(Uri treeUri, Cursor cursor) {
        if (empty(cursor)) {
            return Collections.emptyList();
        }

        cursor.moveToFirst();
        List<DocumentInfo> items = new ArrayList<>();
        do {
            DocumentInfo entry = new DocumentInfo(
                    getString(Column.DOCUMENT_ID, cursor),
                    getDocumentUri(treeUri, cursor),
                    getString(Column.DISPLAY_NAME, cursor),
                    getString(Column.MIME_TYPE, cursor)
            );

            items.add(entry);
        } while (cursor.moveToNext());

        return items;
    }

    private boolean empty(Cursor childDocumentsCursor) {
        return childDocumentsCursor == null || childDocumentsCursor.getCount() == 0;
    }

    private String getString(Column column, Cursor cursor) {
        return cursor.getString(column.positionProjection());
    }

    private Uri getDocumentUri(Uri treeUri, Cursor cursor) {
        String documentId = getString(Column.DOCUMENT_ID, cursor);
        return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId);
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
