package com.ataulm.macaroni;

import android.net.Uri;

class DocumentInfo {

    private final String id;
    private final Uri uri;
    private final String name;
    private final String mimeType;

    public DocumentInfo(String id, Uri uri, String name, String mimeType) {
        this.id = id;
        this.uri = uri;
        this.name = name;
        this.mimeType = mimeType;
    }

    public String getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentInfo that = (DocumentInfo) o;

        if (!id.equals(that.id)) {
            return false;
        }
        if (!uri.equals(that.uri)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        return mimeType.equals(that.mimeType);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + uri.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + mimeType.hashCode();
        return result;
    }
}
