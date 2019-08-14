package com.qbent.enfinsapp.restapi;

import java.io.IOException;

public interface ApiCallback {
    public void onApiRequestStart() throws IOException;
    public void onApiRequestComplete(String key, String result) throws IOException;
}
