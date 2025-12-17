package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managerapp.TaskManager;
import taskapp.SubTask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson = new Gson();

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
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
            List<SubTask> subtasks = manager.getSubtasks();
            sendOK(exchange, gson.toJson(subtasks));
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            SubTask subtask = manager.getSubtaskByID(id);
            if (subtask != null) {
                sendOK(exchange, gson.toJson(subtask));
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        SubTask subtask = gson.fromJson(body, SubTask.class);

        if (subtask.getId() == 0) {
            SubTask created = manager.addSubtask(subtask);
            sendCreated(exchange, gson.toJson(created));
        } else {
            try {
                SubTask updated = manager.updateSubtask(subtask);
                sendOK(exchange, gson.toJson(updated));
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            manager.deleteSubtasks();
            sendOK(exchange, "{\"status\":\"All subtasks deleted\"}");
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteSubtaskByID(id);
            sendOK(exchange, "{\"status\":\"Subtask deleted\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}

