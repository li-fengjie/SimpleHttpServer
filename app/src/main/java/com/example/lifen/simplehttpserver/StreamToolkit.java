package com.example.lifen.simplehttpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Unchangedman on 2017/12/27.
 * 工具类
 */

public class StreamToolkit {
    public static final String readLine(InputStream nis) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c1 = 0;
        int c2 = 0;
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')){
            c1 = c2;
            c2 = nis.read();
            sb.append((char)c2);
        }
        if(sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    public static byte[] readRawFromStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int read;
        while ((read = inputStream.read(buffer)) > 0){
            outputStream.write(buffer,0,read);
        }
        return outputStream.toByteArray();
    }

}
