package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendOK(HttpExchange h, String text) throws IOException {
        sendText(h, text, 200);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        sendText(h, text, 201);
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Resource not found\"}", 404);
    }

    protected void sendNotAcceptable(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Task overlaps with existing tasks\"}", 406);
    }

    protected void sendServerError(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Internal server error\"}", 500);
    }
}
