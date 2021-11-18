package io.esastack;


import io.esastack.test.pref.command.Executor;
import io.esastack.test.pref.command.TestPlatformExecutor;

public class ClientApplication {

    public static void main(String[] args) {
        if (args.length < 5) {
            throw new IllegalStateException("The size of args should not be less than 5!");
        }
        Executor executor = new TestPlatformExecutor();
        executor.execute(args[1], args[2], args[3], args[4], args[5]);
    }

}
