package io.esastack.test.pref.command;

import com.oppo.test.platform.util.ServerUtil;
import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.thread.ExecuteParam;
import com.oppo.test.platform.util.thread.MultiThread;
import io.esastack.test.pref.command.http.HttpClientExecutor;
import io.esastack.test.pref.command.rest.RestClientExecutor;
import io.esastack.test.pref.util.Constants;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class TestPlatformExecutor implements Executor {

    @Override
    public void execute(String type, String url, String ThreadNum, String runTime, String bodyString) {
        final Options options = ServerUtil.buildCommandlineOptions(new Options());
        final CommandLine commandLine = ServerUtil.getCommandLine(new String[]{ThreadNum, runTime}, options);

        ExecuteParam executeParam = new ExecuteParam();

        if (!executeParam.initParam(commandLine)) {
            System.exit(-1);
        }

        // initial monitor as a timer
        Monitor monitor = new Monitor();
        monitor.begin();

        ClientExecuteUnit executeUnit;
        if (Constants.Type.REST.equals(type)) {
            executeUnit = new RestClientExecutor(url, bodyString);
        } else if (Constants.Type.HTTP.equals(type)) {
            executeUnit = new HttpClientExecutor(url, bodyString);
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
