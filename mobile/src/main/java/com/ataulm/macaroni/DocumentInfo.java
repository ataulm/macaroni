package com.ataulm.macaroni;

class DocumentInfo {

    private final String name;
    private final String mimeType;

    public DocumentInfo(String name, String mimeType) {
        this.name = name;
        this.mimeType = mimeType;
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

        if (!name.equals(that.name)) {
            return false;
        }
        return mimeType.equals(that.mimeType);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + mimeType.hashCode();
        return result;
    }
}
