package io.esastack.test.pref.command;

public interface Executor {

    void execute(String type,
                 String url,
                 String ThreadNum,
                 String runTime,
                 String bodyString,
                 int countCount,
                 int connectionPoolSize);
}
