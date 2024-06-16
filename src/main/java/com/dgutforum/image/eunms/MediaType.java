package com.dgutforum.image.eunms;

public enum MediaType {

    IMAGE_JPG("image/jpeg", "jpg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_GIF("image/gif", "gif");

    private String mime;
    private String ext;

    MediaType(String mime, String ext) {
        this.mime = mime;
        this.ext = ext;
    }

    public String getMime() {
        return mime;
    }

    public String getExt() {
        return ext;
    }

    public static String getExtByMime(String mime) {
        for (MediaType type : values()) {
            if (type.getMime().equals(mime)) {
                return type.getExt();
            }
        }
        return null;
    }
}
