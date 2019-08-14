package com.qbent.enfinsapp.global;

public enum GlobalSettings {
//    API_BASE_URL("http://166.62.38.28/api/external/");
    //API_BASE_URL("http://localhost:50740/api/external/");
API_BASE_URL("http://192.168.0.65:80/api/external/");



    private String key;

    public String getKey() {
        return key;
    }

    GlobalSettings (String key) {
        this.key = key;
    }
}
