package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managerapp.TaskManager;
import taskapp.Epic;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET" -> handleGet(exchange, query);
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange, query);
                default -> sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            List<Epic> epics = manager.getEpics();
            sendOK(exchange, gson.toJson(epics));
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Epic epic = manager.getEpicByID(id);
            if (epic != null) {
                sendOK(exchange, gson.toJson(epic));
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0) {
            Epic created = manager.addEpic(epic);
            sendCreated(exchange, gson.toJson(created));
        } else {
            try {
                Epic updated = manager.updateEpic(epic);
                sendOK(exchange, gson.toJson(updated));
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            manager.deleteEpics();
            sendOK(exchange, "{\"status\":\"All epics deleted\"}");
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteEpicByID(id);
            sendOK(exchange, "{\"status\":\"Epic deleted\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}
