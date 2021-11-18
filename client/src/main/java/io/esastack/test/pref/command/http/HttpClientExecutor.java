package io.esastack.test.pref.command.http;

import esa.commons.Checks;
import esa.commons.logging.Logger;
import io.esastack.httpclient.core.HttpClient;
import io.esastack.httpclient.core.HttpResponse;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.ClientExecuteUnit;

import java.nio.charset.StandardCharsets;

public class HttpClientExecutor extends ClientExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private final HttpClient client;

    public HttpClientExecutor(String url, String bodyString) {
        super(url, bodyString);
        System.setProperty("io.esastack.httpclient.ioThreads", "6");
        this.client = HttpClient.create()
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
            HttpResponse response = client.post(url)
                    .body(bodyString.getBytes(StandardCharsets.UTF_8))
                    .execute()
                    .get();
            return response.status() == 200;
        } catch (Throwable e) {
            logger.error("Error occur when doRequest!");
            return false;
        }
    }
}
