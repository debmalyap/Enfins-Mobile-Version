package com.qbent.enfinsapp.model;

public class ApiRequest<T> {
    private T _t;
    private String _restApiUrl;

    public ApiRequest(String restApiUrl) {
        this._restApiUrl = restApiUrl;
    }

    public String get_restApiUrl() {
        return _restApiUrl;
    }

    public void set_restApiUrl(String _restApiUrl) {
        this._restApiUrl = _restApiUrl;
    }

    public T get_t() {
        return _t;
    }

    public void set_t(T _t) {
        this._t = _t;
    }
}
