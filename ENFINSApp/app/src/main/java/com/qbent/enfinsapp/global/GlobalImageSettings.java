package com.qbent.enfinsapp.global;

public enum GlobalImageSettings {
//    API_BASE_URL("http://166.62.38.28/resources/KYCDocument/");
    //API_BASE_URL("http://localhost:50740/api/external/");
API_BASE_URL("http://192.168.0.65:80/resources/KYCDocument/");




    private String key;

    public String getKey() {
        return key;
    }

    GlobalImageSettings (String key) {
        this.key = key;
    }
}
