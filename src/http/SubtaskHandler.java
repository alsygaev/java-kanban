package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandlers {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager) {
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
        } catch (Exception e) {
            e.printStackTrace(); // Логируем ошибку
            sendError(exchange, "Internal Server Error: " + e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if ("/subtasks".equals(path)) {
            // Возвращаем список всех subtask
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendJson(exchange, gson.toJson(subtasks), 200);
        } else if (path.matches("/subtasks/\\d+")) {
            // Возвращаем subtask по ID
            int id = extractIdFromPath(path);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask == null) {
                sendNotFound(exchange);
            } else {
                sendJson(exchange, gson.toJson(subtask), 200);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (subtask.getId() == 0) {
                // Создаём новую Subtask
                taskManager.createSubtask(subtask);
                sendText(exchange, "Subtask created successfully", 201);
            } else {
                // Обновляем существующую Subtask
                taskManager.updateSubtask(subtask);
                sendText(exchange, "Subtask updated successfully", 200);
            }
        } catch (IllegalArgumentException e) {
            sendError(exchange, e.getMessage(), 400);
        } catch (Exception e) {
            sendError(exchange, "Invalid JSON format: " + e.getMessage(), 400);
        }
    }


    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if ("/subtasks".equals(path)) {
            // Удаляем все subtask
            taskManager.deleteAllSubtasks();
            sendText(exchange, "All subtasks deleted successfully", 200);
        } else if (path.matches("/subtasks/\\d+")) {
            // Удаляем subtask по ID
            int id = extractIdFromPath(path);
            taskManager.deleteSubtaskById(id);
            sendText(exchange, "Subtask deleted successfully", 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
