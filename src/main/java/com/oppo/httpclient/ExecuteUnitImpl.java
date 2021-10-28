package com.oppo.httpclient;

import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.stat.Stat;
import com.oppo.test.platform.util.thread.ExecuteUnit;
import esa.httpclient.core.HttpClient;
import esa.httpclient.core.HttpRequest;
import esa.httpclient.core.HttpResponse;

import java.util.concurrent.TimeUnit;

public class ExecuteUnitImpl implements ExecuteUnit {

    private final String uri;

    private String msg;
    private volatile Monitor monitor;
    private byte[] b;

    private static final HttpClient ESA_HTTP_CLIENT;

    static {
        System.setProperty("esa.httpclient.ioThreads", "4");
        ESA_HTTP_CLIENT=HttpClient.create()
                .connectTimeout(1000)
                .readTimeout(3000)
                .connectionPoolSize(2048)
                .connectionPoolWaitingQueueLength(16).build();
    }

    public ExecuteUnitImpl(long msgLength, String uri) {
        final StringBuilder strings = new StringBuilder();
        for(long i = 0L; i < msgLength; i++){
            strings.append('a');
        }
        msg = strings.toString();
        b = msg.getBytes();
        this.uri = uri;
    }

    @Override
    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public Monitor getMonitor() {
        return monitor;
    }

    @Override
    public void run() {
        Stat stat = new Stat();
        stat.start();

        try {
            sendHttpRequest(stat);

        } catch (Throwable t){
            t.printStackTrace();
        }

        stat.end();
        monitor.add(stat);
    }

    public  void sendHttpRequest(Stat stat){
        HttpResponse response;
        try {
            response = ESA_HTTP_CLIENT.async(HttpRequest.get(uri)
                    .build())
                    .get(10L, TimeUnit.SECONDS);

            stat.setSuccess(response.status() == 200);
        }catch (Throwable th) {
            stat.setSuccess(false);
            th.printStackTrace();
        }
    }

}
