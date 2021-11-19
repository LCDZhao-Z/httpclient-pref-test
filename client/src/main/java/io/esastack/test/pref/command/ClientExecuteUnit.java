package io.esastack.test.pref.command;

import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.stat.Stat;
import com.oppo.test.platform.util.thread.ExecuteUnit;
import esa.commons.Checks;
import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ClientExecuteUnit implements ExecuteUnit {

    private static final Logger logger = LoggerUtils.logger();
    private volatile Monitor monitor;
    private final String url;
    private final byte[] body;
    private final AtomicInteger count = new AtomicInteger();
    private final int countPeriod;

    protected ClientExecuteUnit(String url, String bodyString, int countPeriod) {
        Checks.checkNotNull(url, "url");
        Checks.checkNotNull(bodyString, "bodyString");
        this.countPeriod = countPeriod;
        this.url = url;
        this.body = bodyString.getBytes(StandardCharsets.UTF_8);
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void run() {
        final Stat stat = new Stat();
        stat.start();
        if (count.addAndGet(1) < countPeriod) {
            doRequest(url, body).whenComplete((isResponseSuccess, ex) -> {
                try {
                    if (ex != null) {
                        logger.error("Error occur!response:{}", isResponseSuccess, ex);
                        stat.setSuccess(false);
                    } else {
                        stat.setSuccess(isResponseSuccess);
                    }
                } finally {
                    stat.end();
                    monitor.add(stat);
                }
            });
        } else {
            count.set(0);
            CompletionStage<Boolean> responseFuture = doRequest(url, body);
            try {
                stat.setSuccess(responseFuture.toCompletableFuture().get());
            } catch (Exception e) {
                stat.setSuccess(false);
                logger.error("Error occur!", e);
            }
            stat.end();
            monitor.add(stat);
        }
    }

    abstract public CompletionStage<Boolean> doRequest(String url, byte[] body);

}
