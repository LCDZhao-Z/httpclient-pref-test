package io.esastack;


import esa.commons.logging.Logger;
import io.esastack.httpclient.core.util.LoggerUtils;
import io.esastack.test.pref.command.Executor;
import io.esastack.test.pref.command.TestPlatformExecutor;

import java.util.Arrays;

public class ClientApplication {
    private static final Logger logger = LoggerUtils.logger();

    public static void main(String[] args) {
        if (args.length < 7) {
            throw new IllegalStateException("The size of args should not be less than 7!");
        }
        logger.info("args : {}", Arrays.toString(args));
        Executor executor = new TestPlatformExecutor();
        executor.execute(args[0], args[1], args[2], args[3], args[4], Integer.parseInt(args[5]), Integer.parseInt(args[6]));
    }

}
