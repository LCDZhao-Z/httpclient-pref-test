package io.esastack.test.pref.command.http;

import io.esastack.httpclient.core.HttpClient;
import io.esastack.httpclient.core.HttpResponse;
import io.esastack.test.pref.command.ClientExecuteUnit;

import java.util.concurrent.CompletionStage;

public class HttpClientExecutor extends ClientExecuteUnit {

    private final HttpClient client;

    public HttpClientExecutor(String url, String bodyString, int countCount,int connectionPoolSize) {
        super(url, bodyString, countCount);
        System.setProperty("io.esastack.httpclient.ioThreads", "6");
        this.client = HttpClient.create()
                .connectTimeout(1000)
                .readTimeout(3000)
                .connectionPoolSize(connectionPoolSize)
                .connectionPoolWaitingQueueLength(256)
                .build();
    }

    @Override
    public CompletionStage<HttpResponse> doRequest(String url, byte[] body) {
        return client.post(url)
                .body(body)
                .execute();
    }
}
