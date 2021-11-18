package io.esastack.test.pref.command;

import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.stat.Stat;
import com.oppo.test.platform.util.thread.ExecuteUnit;
import esa.commons.Checks;

public abstract class ClientExecuteUnit implements ExecuteUnit {

    private Monitor monitor;
    private final String url;
    private final String bodyString;

    protected ClientExecuteUnit(String url, String bodyString) {
        Checks.checkNotNull(url, "url");
        Checks.checkNotNull(bodyString, "bodyString");
        this.url = url;
        this.bodyString = bodyString;
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void run() {
        Stat stat = new Stat();
        stat.start();
        stat.setSuccess(doRequest(url, bodyString));
        stat.end();
        monitor.add(stat);
    }

    abstract public boolean doRequest(String url, String bodyString);

}
