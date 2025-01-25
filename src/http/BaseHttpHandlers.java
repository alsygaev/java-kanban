package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandlers implements HttpHandler {

    // Отправка JSON-ответа
    protected void sendJson(HttpExchange exchange, String json, int statusCode) throws IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    // Отправка текстового ответа
    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    // Отправка ошибки
    protected void sendError(HttpExchange exchange, String errorMessage, int statusCode) throws IOException {
        sendText(exchange, errorMessage, statusCode);
    }

    // Отправка 404 Not Found
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "Not Found", 404);
    }

    public abstract void handle(HttpExchange exchange) throws IOException;
}
