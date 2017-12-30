package com.example.lifen.simplehttpserver;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;


/**
 * Created by Unchangedman on 2017/12/30.
 */

class UploadImageHandler implements IRsourceUriHandler {
    private static final String TAG = "UploadImageHandler";
    String acceptPrefix = "/image/";
    Activity activity;

    public UploadImageHandler(Activity activity){
        this.activity = activity;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void postHandle(String uri, HttpContext httpContext) throws IOException {
        long totalLength = Long.parseLong(httpContext.getRequestHeaderValue("Content-Length").trim());
        /*
        将 接收到的 图片存储到本机 SD卡目录下
         */
        File file = new File(Environment.getExternalStorageDirectory(),
                "tmpFile.jpg");
        Log.i(TAG, "postHandle: " +"totalLength=" + totalLength + " file    getPath=" + file.getPath() );
        if(file.exists()){
            file.delete();
        }
        byte[] buffer = new byte[10240];
        int read;
        long nLeftLength = totalLength;
        FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());//文件输出流
        InputStream inputStream = httpContext.getUnderlySocket().getInputStream();//从当前 Socket 中得到文件输入流
        while (nLeftLength > 0 && (read = inputStream.read(buffer)) > 0){//写到文件输出流中，即存储到SD 卡路径下
            fileOutputStream.write(buffer,0,read);
            nLeftLength -= read;
        }
        Log.i(TAG, "postHandle: close");
        fileOutputStream.close();

        /*
        从当前 Socket 中得到 文件输出流，将 符合http协议的信息 返回给浏览器
         */
        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.print("HTTP/1.1 200 OK");
        printWriter.println();
        /*
        记录图片存储的位置
         */
        onImageLoaded(file.getPath());
    }

    public void onImageLoaded(String path){
        Log.d(TAG, "onImageLoaded() called with: path = [" + path + "]");
    }
}
