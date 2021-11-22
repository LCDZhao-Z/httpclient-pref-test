package io.esastack.test.pref.command.apache;

import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.ClientExecuteUnit;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApacheClientExecutor extends ClientExecuteUnit {
    private final CloseableHttpAsyncClient client;
    private static final Logger logger = LoggerUtils.logger();

    public ApacheClientExecutor(String url, String bodyString, int countCount, int connectionPoolSize) {
        super(url, bodyString, countCount);
        PoolingAsyncClientConnectionManager connectionManager = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(connectionPoolSize)
                .setMaxConnPerRoute(connectionPoolSize)
                .setConnectionTimeToLive(TimeValue.ofMinutes(1L))
                .build();
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(4)
                .build();
        client = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setIOReactorConfig(ioReactorConfig)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(Timeout.ofSeconds(1))
                        .setResponseTimeout(Timeout.ofSeconds(3))
                        .build())
                .build();
        client.start();
    }

    @Override
    public CompletionStage<Boolean> doRequest(String url, byte[] body) {
        SimpleHttpRequest request = SimpleRequestBuilder.post()
                .setUri(url)
                .setBody(body, ContentType.TEXT_HTML)
                .build();
        final CompletableFuture<Boolean> future = new CompletableFuture<>();
        client.execute(request, new FutureCallback<SimpleHttpResponse>() {

            @Override
            public void completed(final SimpleHttpResponse response) {
                future.complete(response.getCode() == 200);
            }

            @Override
            public void failed(final Exception ex) {
                logger.error("failed!", ex);
                future.complete(false);
            }

            @Override
            public void cancelled() {
                logger.error("cancelled!");
                future.complete(false);
            }
        });

        return future;
    }
}
