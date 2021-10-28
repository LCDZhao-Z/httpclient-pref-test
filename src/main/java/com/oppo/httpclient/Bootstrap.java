package com.oppo.httpclient;

public class Bootstrap {

    private static final String uri = "http://10.176.99.184:9997/12345";

    private static final String[] globalArgs = {
            "-t1","-d1m"
    };

    public static void main(String[] args) {
        final HttpClientExecutor executor = new HttpClientExecutor();
        executor.run(globalArgs, uri);
    }
}
