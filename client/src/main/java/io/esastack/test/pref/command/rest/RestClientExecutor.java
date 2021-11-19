package io.esastack.test.pref.command.rest;

import io.esastack.restclient.RestClient;
import io.esastack.test.pref.command.ClientExecuteUnit;

import java.util.concurrent.CompletionStage;

public class RestClientExecutor extends ClientExecuteUnit {

    private final RestClient client;

    public RestClientExecutor(String url, String bodyString, int countCount, int connectionPoolSize) {
        super(url, bodyString, countCount);
        System.setProperty("io.esastack.httpclient.ioThreads", "6");
        this.client = RestClient.create()
                .connectTimeout(1000)
                .readTimeout(3000)
                .connectionPoolSize(connectionPoolSize)
                .connectionPoolWaitingQueueLength(256).build();
    }

    @Override
    public CompletionStage<Boolean> doRequest(String url, byte[] body) {
        return client.post(url)
                .entity(body)
                .execute()
                .thenApply(restResponseBase -> restResponseBase.status() == 200);
    }
}
