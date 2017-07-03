package com.afrid.iscan.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

public class NetUtils {

    private static final int CONNECTION_TIMEOUT = 20000;
    private static final int SOCKET_TIMEOUT = 20000;
    private static boolean connectionTimeout, socketTimeout;
    private static final int ON_SUCCESS = 0;
    private static final int ON_FAILED = 1;
    private OnResultListener mOnResultListener;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mOnResultListener==null) return;
            switch (msg.arg1){
                case ON_SUCCESS:
                    mOnResultListener.onSuccess(msg.obj.toString());
                    break;
                case ON_FAILED:
                    mOnResultListener.onFailed(msg.obj.toString());
                    break;
            }
        }
    };


    private static final String TAG = "Test";

    public void getData(final String url , final String param , final OnResultListener onResultListener){
        this.mOnResultListener = onResultListener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpPost(url, param);
            }
        }).start();
    }

    public interface  OnResultListener{
        void onSuccess(String result);
        void onFailed(String error);
    }

    

    
    public  void  httpPost(String url, String jsonToPost) {
    	String jsonString = "";
    	
    	HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);

        // HttpClient httpClient = new DefaultHttpClient();;
        HttpPost httpPost = new HttpPost(url);
        BufferedReader reader = null;

        // Set some headers to inform server about the type of the content
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");

        try {
            // Set json to StringEntity
            StringEntity se = new StringEntity(jsonToPost, "UTF-8");
            // Set httpPost Entity
            httpPost.setEntity(se);

            // Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
            jsonString = reader.readLine();
            Message msg = new Message();
            msg.arg1 = ON_SUCCESS;
            msg.obj = jsonString;
            mHandler.sendMessage(msg);

        }catch (Exception e) {
            Message msg = new Message();
            msg.arg1 = ON_FAILED;
            msg.obj = e.getMessage();
            mHandler.sendMessage(msg);
            Log.e(TAG, "httpPostTest: ========="+e.getMessage() );
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) httpClient.getConnectionManager().shutdown();
                if (reader != null) reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
