package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managerapp.TaskManager;
import taskapp.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = new Gson();
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
                default -> sendServerError(exchange);
            }

        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            List<Task> tasks = manager.getTasks();
            sendOK(exchange, gson.toJson(tasks));
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Task task = manager.getTaskByID(id);
            if (task != null) {
                sendOK(exchange, gson.toJson(task));
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            Task created = manager.addTask(task);
            sendCreated(exchange, gson.toJson(created));
        } else {
            try {
                Task updated = manager.updateTask(task);
                sendOK(exchange, gson.toJson(updated));
            } catch (IllegalArgumentException e) {
                sendNotFound(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            manager.deleteTasks();
            sendOK(exchange, "{\"status\":\"All tasks deleted\"}");
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteTaskByID(id);
            sendOK(exchange, "{\"status\":\"Task deleted\"}");
        } else {
            sendNotFound(exchange);
        }
    }
}


