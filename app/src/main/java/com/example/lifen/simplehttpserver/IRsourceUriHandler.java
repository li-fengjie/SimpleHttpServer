package com.example.lifen.simplehttpserver;

import java.io.IOException;

/**
 * Created by Unchangedman on 2017/12/29.
 */

public interface IRsourceUriHandler {
    boolean accept(String uri);
    void postHandle(String uri, HttpContext httpContext) throws IOException;
}
