package com.qbent.enfinsapp.model;

public class UploadPassbook {
    public byte[] IMAGE;

    public UploadPassbook(byte[] IMAGE) {
        this.IMAGE = IMAGE;
    }

    public byte[] getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(byte[] IMAGE) {
        this.IMAGE = IMAGE;
    }
}
