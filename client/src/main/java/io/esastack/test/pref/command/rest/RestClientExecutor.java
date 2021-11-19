package io.esastack.test.pref.command.rest;

import esa.commons.logging.Logger;
import io.esastack.commons.net.http.MediaTypeUtil;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.restclient.RestClient;
import io.esastack.test.pref.command.ClientExecuteUnit;

import java.util.concurrent.CompletionStage;

public class RestClientExecutor extends ClientExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private final RestClient client;

    public RestClientExecutor(String url, String bodyString, int countCount, int connectionPoolSize) {
        super(url, bodyString, countCount);
        System.setProperty("io.esastack.httpclient.ioThreads", "4");
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
                .contentType(MediaTypeUtil.TEXT_PLAIN)
                .execute()
                .thenApply(restResponseBase -> {

                    if (restResponseBase.status() != 200) {
                        try {
                            logger.error("Error occur!status:{},body:{}", restResponseBase.status(),
                                    restResponseBase.bodyToEntity(String.class));
                        } catch (Exception e) {
                            logger.error("Error occur!", e);
                        }
                        return false;
                    }
                    return true;
                });
    }
}
