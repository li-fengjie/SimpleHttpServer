package com.example.lifen.simplehttpserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Unchangedman on 2017/12/27.
 */

public class SimpleHttpServer {
    private static final String TAG = "SimpleHttpServer";
    private final WebConfiguration webConfig;
    private final ExecutorService threadPool;//线程池
    private final HashSet<IRsourceUriHandler> resourceUriHandlers;
    private boolean isEnable;
    private ServerSocket socket;

    public SimpleHttpServer(WebConfiguration webConfig){
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
        resourceUriHandlers = new HashSet<>();
    }

    public void registerResourceHandler(IRsourceUriHandler iRsourceUriHandler){
        resourceUriHandlers.add(iRsourceUriHandler);
    }

    /**
     * 启动Server(异步)
     */
    public void startAsync(){
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }

    /**
     * 停止Server（异步）
     */
    public void stopAsync() {
        if(!isEnable){
            return;
        }
        isEnable = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
    }

    private void doProcSync() {
        Log.d(TAG, "doProcSync() called");
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddr);
            while(isEnable){
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.e(TAG, "doProcSync: e ", e);
        }
    }

    /**
     * 一个HTTP请求报文由请求行（request line）、请求头部（header）、空行和请求数据4个部分组成
     * @param remotePeer
     */
    private void onAcceptRemotePeer(Socket remotePeer) {
        Log.d(TAG, "onAcceptRemotePeer() called with: remotePeer.getRemoteSocketAddress() = [" + remotePeer.getRemoteSocketAddress() + "]");
        HttpContext  httpContext = new HttpContext();
        try {
//            remotePeer.getOutputStream().write("congratulations, connected success".getBytes());
            httpContext.setUnderlySocket(remotePeer);
            InputStream nis = remotePeer.getInputStream();
            String headerLine = null;
            String readLine = StreamToolkit.readLine(nis);//http请求行（request line）1
            Log.i(TAG, "http 1 请求行  readLine =" + readLine);
            //测试请求行结果 readLine =POST /get/?key=dddd&sss=123456 HTTP/1.1
            httpContext.setType(readLine.split(" ")[0]);
            String resourceUri = headerLine = readLine.split(" ")[1];
            Log.i(TAG, "地址 =" + headerLine);
            while ((headerLine = StreamToolkit.readLine(nis)) != null) {//http请求头部（header）2

                if (headerLine.equals("\r\n")) {//http请求空行3
                    Log.i(TAG, "http 3 请求头部 /r/n ");
                    break;
                }
                Log.i(TAG, "http 2 请求头部 headerLine = " + headerLine);
                String[] pair = headerLine.split(": ");
                if (pair.length > 1) {
                    httpContext.addRequestHeader(pair[0], pair[1]);
                }
            }

            for (IRsourceUriHandler handler : resourceUriHandlers) {
                if (!handler.accept(resourceUri)) {
                    continue;
                }
                handler.postHandle(resourceUri, httpContext);//http请求数据4
            }
        } catch (IOException e) {
            Log.e("spy",e.toString());
        }finally {//只要流或socket不关闭,浏览器就收不到信息
            try {
                remotePeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
