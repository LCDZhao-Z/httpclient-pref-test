package io.esastack.test.pref.command.ok;

import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.ClientExecuteUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OKHttpClientExecutor extends ClientExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private final OkHttpClient client;


    public OKHttpClientExecutor(String url, String bodyString, int countPeriod, int connectionPoolSize) {
        super(url, bodyString, countPeriod);
        client = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(Executors.newFixedThreadPool(4)))
                .connectTimeout(1, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(connectionPoolSize, 1, TimeUnit.MINUTES))
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        client.dispatcher().setMaxRequests(connectionPoolSize);

        client.dispatcher().setMaxRequestsPerHost(connectionPoolSize);
    }

    @Override
    public CompletionStage<Boolean> doRequest(String url, byte[] body) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(body))
                .build();

        Call call = client.newCall(request);

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response) {
                future.complete(response.code() == 200);
            }

            public void onFailure(Call call, IOException e) {
                logger.error("failed!", e);
                future.complete(false);
            }
        });
        return future;
    }
}
