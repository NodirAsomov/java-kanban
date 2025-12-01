package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import managerapp.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer server;
    private final TaskManager manager;
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);


        server.createContext("/tasks", new TasksHandler(manager));

    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("HTTP-сервер остановлен");
    }

    public static Gson getGson() {
        return gson;
    }
}

