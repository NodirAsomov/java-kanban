package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserHandler extends BaseHttpHandler implements HttpHandler {

    private final Map<Integer, User> users = new HashMap<>();
    private final Gson gson = new Gson();
    private int nextId = 1;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();

            switch (method) {
                case "GET":
                    handleGet(exchange, query);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, query);
                    break;
                default:
                    sendServerError(exchange);
            }
        } catch (Exception e) {
            sendServerError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query == null) {

            sendOK(exchange, gson.toJson(users.values()));
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            User user = users.get(id);
            if (user != null) {
                sendOK(exchange, gson.toJson(user));
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes());
        User user = gson.fromJson(body, User.class);

        if (user.getId() == 0) {

            user.setId(nextId++);
            users.put(user.getId(), user);
            sendCreated(exchange, gson.toJson(user));
        } else {

            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                sendOK(exchange, gson.toJson(user));
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {

            users.clear();
            sendOK(exchange, "{\"status\":\"All users deleted\"}");
        } else if (query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            if (users.remove(id) != null) {
                sendOK(exchange, "{\"status\":\"User deleted\"}");
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }


    public static class User {
        private int id;
        private String name;
        private String email;

        public User() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

