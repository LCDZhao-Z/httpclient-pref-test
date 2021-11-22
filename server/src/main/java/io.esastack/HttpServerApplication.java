package io.esastack;

import esa.httpserver.HttpServer;
import esa.httpserver.ServerOptionsConfigure;
import io.netty.handler.logging.LogLevel;

import java.io.IOException;

public class HttpServerApplication {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        if (args.length > 0) {
            String portString = args[0];
            port = Integer.parseInt(portString);
        }
        int threadNum = 8;
        if (args.length > 1) {
            String threadString = args[1];
            threadNum = Integer.parseInt(threadString);
        }

        final HttpServer server = HttpServer.create(ServerOptionsConfigure.newOpts()
                .logging(LogLevel.TRACE)
                .ioThreads(threadNum)
                .configured())
                .handle(req ->
                        req.onData(data ->
                                req.response().end(data.retain())));
        server.listen(port);
        System.in.read();
    }

}
