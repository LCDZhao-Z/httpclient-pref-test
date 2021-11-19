package io.esastack.test.pref.command.http;

import esa.commons.logging.Logger;
import io.esastack.httpclient.core.HttpClient;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.ClientExecuteUnit;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

public class HttpClientExecutor extends ClientExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private final HttpClient client;

    public HttpClientExecutor(String url, String bodyString, int countCount, int connectionPoolSize) {
        super(url, bodyString, countCount);
        System.setProperty("io.esastack.httpclient.ioThreads", "4");
        this.client = HttpClient.create()
                .connectTimeout(1000)
                .readTimeout(3000)
                .connectionPoolSize(connectionPoolSize)
                .connectionPoolWaitingQueueLength(256)
                .build();
    }

    @Override
    public CompletionStage<Boolean> doRequest(String url, byte[] body) {
        return client.post(url)
                .body(body)
                .execute()
                .thenApply(httpResponse -> {
                    if (httpResponse.status() != 200) {
                        logger.error("Error occur!status:{},body:{}", httpResponse.status(),
                                httpResponse.body().string(StandardCharsets.UTF_8));
                        return false;
                    }
                    return true;
                });
    }
}
