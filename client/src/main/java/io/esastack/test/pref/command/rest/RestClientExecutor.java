package io.esastack.test.pref.command.rest;

import esa.commons.Checks;
import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.restclient.RestClient;
import io.esastack.restclient.RestResponseBase;
import io.esastack.test.pref.command.ClientExecuteUnit;

public class RestClientExecutor extends ClientExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private final RestClient client;

    public RestClientExecutor(String url, String bodyString) {
        super(url, bodyString);
        System.setProperty("io.esastack.httpclient.ioThreads", "6");
        this.client = RestClient.create()
                .connectTimeout(1000)
                .readTimeout(3000)
                .connectionPoolSize(2048)
                .connectionPoolWaitingQueueLength(16).build();
    }

    @Override
    public boolean doRequest(String url, String bodyString) {
        Checks.checkNotNull(url, "url");
        Checks.checkNotNull(bodyString, "bodyString");

        try {
            RestResponseBase response = client.post(url)
                    .entity(bodyString)
                    .execute()
                    .toCompletableFuture()
                    .get();
            return response.status() == 200;
        } catch (Throwable e) {
            logger.error("Error occur when doRequest!");
            return false;
        }
    }
}
