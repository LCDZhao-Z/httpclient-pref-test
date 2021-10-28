package com.oppo.httpclient;

import com.oppo.test.platform.util.ServerUtil;
import com.oppo.test.platform.util.stat.Monitor;
import com.oppo.test.platform.util.thread.ExecuteParam;
import com.oppo.test.platform.util.thread.MultiThread;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class HttpClientExecutor {

    public void run(String[] args, String uri) {
        final Options options = ServerUtil.buildCommandlineOptions(new Options());
        final CommandLine commandLine = ServerUtil.getCommandLine(args, options);

        ExecuteParam executeParam = new ExecuteParam();

        if (!executeParam.initParam(commandLine)) {
            System.exit(-1);
        }

        // initial monitor as a timer
        Monitor monitor = new Monitor();
        monitor.begin();

        long length = commandLine.hasOption('s') ? Integer.parseInt(commandLine.getOptionValue('s')) : 1024 * 1024;

        // 统一执行单元
        final ExecuteUnitImpl impl = new ExecuteUnitImpl(length, uri);

        // 关联执行器与计时器
        impl.setMonitor(monitor);

        //开始执行
        MultiThread.execute(executeParam, impl);

        // 结束
        MultiThread.stop(impl);
    }
}
