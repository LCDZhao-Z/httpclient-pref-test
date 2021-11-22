package io.esastack.test.pref.command;

import com.oppo.test.platform.util.ServerUtil;
import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.thread.ExecuteParam;
import com.oppo.test.platform.util.thread.MultiThread;
import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.apache.ApacheClientExecutor;
import io.esastack.test.pref.command.async.AsyncHttpClientExecutor;
import io.esastack.test.pref.command.http.HttpClientExecutor;
import io.esastack.test.pref.command.ok.OKHttpClientExecutor;
import io.esastack.test.pref.command.rest.RestClientExecutor;
import io.esastack.test.pref.util.Constants;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class TestPlatformExecutor implements Executor {
    private static final Logger logger = LoggerUtils.logger();

    @Override
    public void execute(String type,
                        String url,
                        String ThreadNum,
                        String runTime,
                        String bodyString,
                        int countCount,
                        int connectionPoolSize) {
        final Options options = ServerUtil.buildCommandlineOptions(new Options());
        final CommandLine commandLine = ServerUtil.getCommandLine(new String[]{ThreadNum, runTime}, options);

        ExecuteParam executeParam = new ExecuteParam();

        if (!executeParam.initParam(commandLine)) {
            System.exit(-1);
        }
        logger.info("ExecuteParam threads:{}", executeParam.getThreads());

        // initial monitor as a timer
        Monitor monitor = new Monitor();
        monitor.begin();

        ClientExecuteUnit executeUnit;
        if (Constants.Type.REST.equals(type)) {
            executeUnit = new RestClientExecutor(url, bodyString, countCount, connectionPoolSize);
        } else if (Constants.Type.HTTP.equals(type)) {
            executeUnit = new HttpClientExecutor(url, bodyString, countCount, connectionPoolSize);
        } else if (Constants.Type.APACHE.equals(type)) {
            executeUnit = new ApacheClientExecutor(url, bodyString, countCount, connectionPoolSize);
        } else if (Constants.Type.ASYNC_HTTP.equals(type)) {
            executeUnit = new AsyncHttpClientExecutor(url, bodyString, countCount, connectionPoolSize);
        } else if (Constants.Type.OK.equals(type)) {
            executeUnit = new OKHttpClientExecutor(url, bodyString, countCount, connectionPoolSize);
        } else {
            throw new UnsupportedOperationException("Unknown type : " + type);
        }

        // 关联执行器与计时器
        executeUnit.setMonitor(monitor);

        //开始执行
        MultiThread.execute(executeParam, executeUnit);

        // 结束
        MultiThread.stop(executeUnit);
    }
}
