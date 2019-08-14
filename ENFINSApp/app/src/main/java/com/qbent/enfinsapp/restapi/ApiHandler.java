package com.qbent.enfinsapp.restapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.qbent.enfinsapp.LoginActivity;
import com.qbent.enfinsapp.global.AuthHelper;
import com.qbent.enfinsapp.global.GlobalSettings;
import com.qbent.enfinsapp.model.ApiRequest;
import com.qbent.enfinsapp.model.Login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ApiHandler {

    public static class GetAsync extends AsyncTask<String, Void, String> {
        private Context _context;
        private ApiCallback _callback;
        private String _urlPart;
        private AuthHelper _authHelper;

        public GetAsync(Context context) {
            this._context = context;
            this._callback = (ApiCallback) context;
            this._authHelper = AuthHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute() {
            try {
                _callback.onApiRequestStart();
            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                _urlPart = strings[0];
                String requestUrl = GlobalSettings.API_BASE_URL.getKey() + _urlPart;

                String newurl=requestUrl.replaceAll(" ","%20");
                URL restUrl = new URL(newurl);
                HttpURLConnection restConnection = (HttpURLConnection) restUrl.openConnection();
                restConnection.setRequestMethod("GET");
                restConnection.setRequestProperty("Accept", "application/json");
                restConnection.setConnectTimeout(1000 * 60 * 60);
                if (_authHelper.isLoggedIn()) {
                    restConnection.setRequestProperty("Authorization", "Bearer " + _authHelper.getIdToken());
                }

                String value = _authHelper.getIdToken();

                restConnection.connect();

                if(restConnection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Intent intent = new Intent(this._context, LoginActivity.class);
                    _context.startActivity(intent);
                }

                if (restConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed: " + restConnection.getResponseCode());
                }

                String output;
                BufferedReader brReader = new BufferedReader(
                        new InputStreamReader(restConnection.getInputStream())
                );
                while ((output = brReader.readLine()) != null) {
                    response = output;
                }
                restConnection. disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s != null) {
                    //if (_urlPart.equals("collection-points")) {
                        _callback.onApiRequestComplete(_urlPart, s);
                    //}
                } else {
                    _callback.onApiRequestComplete("Invalid", "Data");
                }
            } catch (Exception e) {

            }
        }
    }

    public static class PostAsync extends AsyncTask<ApiRequest, Void, String> {
        private Context _context;
        private ApiCallback _callback;
        private String _urlPart;
        private AuthHelper _authHelper;
        private ProgressDialog dialog;


//        private JSONArray jsonArray = new JSONArray();
//        private JSONObject jsonObject = new JSONObject();


        public PostAsync(Context context) {
            this._context = context;
            this._callback = (ApiCallback) context;
            this._authHelper = AuthHelper.getInstance(context);
        }



        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(_context);
            dialog.setMessage("Please wait");
            dialog.show();

            try {
                _callback.onApiRequestStart();
            } catch (Exception e) {

            }
        }

        @Override
        protected String doInBackground(ApiRequest... apiRequests) {
            _urlPart = apiRequests[0].get_restApiUrl();
            String response = "";
            String testUpload = "a";
            if(_urlPart.equals("getLoanBondNoByCP") || _urlPart.equals("search-loanBondNo-overdue") || _urlPart.equals("getLoanBondNoBySearchText") || _urlPart.equals("getLoanBondNoByCP") || _urlPart.equals("search-loanBondNo"))
            {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            try {
//                _urlPart = apiRequests[0].get_restApiUrl();
                if(_urlPart.equals("uploadPassbookFile2")||_urlPart.equals("uploadPassbookFile3"))
                {
                    testUpload = _urlPart;
                    _urlPart = "uploadPassbookFile";
                }
                String apiUrl = GlobalSettings.API_BASE_URL.getKey() + _urlPart;



                URL restUrl = new URL(apiUrl);
                HttpURLConnection restConnection = (HttpURLConnection) restUrl.openConnection();
                restConnection.setDoOutput(true);
                restConnection.setRequestMethod("POST");

//                restConnection.setF
                restConnection.setConnectTimeout(1000 * 60 * 60);

                //Login login = (Login) apiRequests[0].get_t();
                //String path = apiRequests[0].get_t().
                String test =  apiRequests[0].get_t().getClass().getSimpleName();

//                if(test.equals("JSONArray"))
//                {
//                    jsonArray = (JSONArray) apiRequests[0].get_t();
//                }
//                else
//                {
                JSONObject jsonObject = (JSONObject) apiRequests[0].get_t();


                if(testUpload.equals("uploadPassbookFile2"))
                {
                    String position = jsonObject.getString("position");
                    response = position;
                    _urlPart = testUpload;
                }
                if(testUpload.equals("uploadPassbookFile3"))
                {
                    _urlPart = testUpload;
                }
//                }

                restConnection.setRequestProperty("Content-Type", "application/json");

//                jsonObject.accumulate("userName", login.getUserName());
//                jsonObject.accumulate("password", login.getPassword());

                if (_authHelper.isLoggedIn()) {
                    restConnection.setRequestProperty("Authorization", "Bearer " + _authHelper.getIdToken());
                }

                restConnection.connect();
                OutputStream os = restConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                if(test.equals("JSONArray"))
//                {
//                    writer.write(jsonArray.toString());
//                }
//                else
//                {
                writer.write(jsonObject.toString());
//                }

                writer.flush();
                writer.close();
                os.flush();
                os.close();

                if (restConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed: " + restConnection.getResponseCode());
                }

                String output;
                BufferedReader brReader = new BufferedReader(
                        new InputStreamReader(restConnection.getInputStream())
                );
                while ((output = brReader.readLine()) != null) {
                    response = response + output;
                }
                restConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                if (s != null) {
                    //if (_urlPart.equals("authenticate")) {
                        _callback.onApiRequestComplete(_urlPart, s);
                    //}
                } else {
                    _callback.onApiRequestComplete("Invalid", "Data");
                }
            } catch (Exception e) {

            }
        }
    }

    public static class UpdateApp extends AsyncTask<String,Void,Void>{
        private Context _context;
        private ProgressDialog dialog;

        public UpdateApp(Context context) {
            this._context = context;
            dialog = new ProgressDialog(_context);
            dialog.setMessage("Please wait");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {

            int count;
            try {
                URL url = new URL(arg0[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/DCCL.apk");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/DCCL.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                _context.startActivity(intent);

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }


            } catch (Exception e) {
                String test = "a";
            }
            return null;
        }
    }
}
