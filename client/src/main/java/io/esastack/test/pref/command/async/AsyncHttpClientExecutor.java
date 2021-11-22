package io.esastack.test.pref.command.async;

import io.esastack.test.pref.command.ClientExecuteUnit;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.util.HttpConstants;

import java.util.concurrent.CompletionStage;

public class AsyncHttpClientExecutor extends ClientExecuteUnit {

    AsyncHttpClient client;

    public AsyncHttpClientExecutor(String url, String bodyString, int countPeriod, int connectionPoolSize) {
        super(url, bodyString, countPeriod);
        DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
                .setConnectTimeout(1000)
                .setReadTimeout(3000)
                .setMaxConnectionsPerHost(connectionPoolSize)
                .setMaxConnections(connectionPoolSize)
                .setIoThreadsCount(4);
        client = Dsl.asyncHttpClient(clientBuilder);
    }

    @Override
    public CompletionStage<Boolean> doRequest(String url, byte[] body) {
        Request request = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl(url)
                .setBody(body)
                .build();

        return client.executeRequest(request)
                .toCompletableFuture()
                .thenApply(response -> response.getStatusCode() == 200);
    }
}
