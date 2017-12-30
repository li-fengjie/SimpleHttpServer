package com.example.lifen.simplehttpserver;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Unchangedman on 2017/12/29.
 */

public class ResourceInAssetsHandler implements IRsourceUriHandler {
    private static final String TAG = "ResourceInAssetsHandler";
    private String acceptPrefix = "/static/";
    private Context context;

    public ResourceInAssetsHandler(Context context){
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        /*
        判断uri 是否以 “/static/" 开始
         */
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void postHandle(String uri, HttpContext httpContext) throws IOException{
        Log.d(TAG, "postHandle() called with: uri = [" + uri + "], httpContext = [" + httpContext + "]");
        /*
        截取assets 文件夹下静态网页相对路径
         */
        int startIndex = acceptPrefix.length();
        String assetsPath = uri.substring(startIndex);
        Log.i(TAG, "assetsPath: " + assetsPath);

        /*
        打开静态网页 返回一个输入流 转化为 byte[]
         */
        InputStream inputStream = context.getAssets().open(assetsPath);//打开文件 返回一个输入流
        byte[] raw = StreamToolkit.readRawFromStream(inputStream);
        Log.i(TAG, "assetsPath "+ "raw =" + raw.length);
        inputStream.close();

        OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();//获取当前Socket的输出流
        PrintStream printWriter = new PrintStream(outputStream);//输出符合http协议的信息给浏览器
        printWriter.println("HTTP/1.1 200 OK");
        printWriter.println("Content-Length:" + raw.length);

        if(assetsPath.endsWith(".html")){
            printWriter.println("Content-Type:text/html");
        }else if(assetsPath.endsWith(".js")){
            printWriter.println("Content-Type:text/js");
        }else if(assetsPath.endsWith(".css")){
            printWriter.println("Content-Type:text/css");
        }else if(assetsPath.endsWith(".jpg")){
            printWriter.println("Content-Type:text/jpg");
        }else if(assetsPath.endsWith(".png")){
            printWriter.println("Content-Type:text/png");
        }
        printWriter.println();

        printWriter.write(raw);
        Log.i(TAG, "postHandle: over");
    }
}
