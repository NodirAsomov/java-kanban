package http;

import com.google.gson.*;
import managerapp.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private final com.sun.net.httpserver.HttpServer server;
    private final TaskManager manager;
    private static final int PORT = 8080;
    private static final Gson gson = buildGson();

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(PORT), 0);


        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/subtasks", new SubtasksHandler(manager));


        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту: " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("HTTP-сервер остановлен");
    }

    public static Gson getGson() {
        return gson;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                        return LocalDateTime.parse(json.getAsString());
                    }
                })
                .setPrettyPrinting()
                .create();
    }
}

