package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandlers {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendText(exchange, "Method not allowed", 405);
            }
        } catch (IllegalArgumentException e) {
            sendError(exchange, "Time slot is conflict: " + e.getMessage(), 406);
        } catch (Exception e) {
            sendError(exchange, "Internal server error: " + e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if ("/epics".equals(path)) {
            // Возвращаем список всех эпиков
            List<Epic> epics = taskManager.getAllEpics();
            sendJson(exchange, gson.toJson(epics), 200);
        } else if (path.matches("/epics/\\d+")) {
            // Возвращаем эпик по ID
            int id = extractIdFromPath(path);
            Epic epic = taskManager.getEpicById(id);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                sendJson(exchange, gson.toJson(epic), 200);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic.getId() == 0) {
                // Создаём новый эпик
                taskManager.createEpic(epic);
                sendText(exchange, "Epic created successfully", 201);
            } else {
                // Обновляем существующий эпик
                taskManager.updateEpic(epic);
                sendText(exchange, "Epic updated successfully", 200);
            }
        } catch (Exception e) {
            sendError(exchange, "Invalid JSON format: " + e.getMessage(), 400);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if ("/epics".equals(path)) {
            // Удаляем все эпики
            taskManager.deleteAllEpics();
            sendText(exchange, "All epics deleted successfully", 200);
        } else if (path.matches("/epics/\\d+")) {
            // Удаляем эпик по ID
            int id = extractIdFromPath(path);
            taskManager.deleteEpicById(id);
            sendText(exchange, "Epic deleted successfully", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}
