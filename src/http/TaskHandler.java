package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandlers {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager) {
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
            sendError(exchange, "Internal server error: " + e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if ("/tasks".equals(path)) {
            List<Task> tasks = taskManager.getAllTasks();
            sendJson(exchange, gson.toJson(tasks), 200);
        } else if (path.startsWith("/tasks/")) {
            try {
                int taskId = extractIdFromPath(path);
                Task task = taskManager.getTaskById(taskId);
                if (task == null) {
                    sendNotFound(exchange);
                } else {
                    sendJson(exchange, gson.toJson(task), 200);
                }
            } catch (NumberFormatException e) {
                sendError(exchange, "Invalid task ID", 400);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);

            if (task.getId() == 0) {
                // Если ID задачи отсутствует, создаём новую задачу
                taskManager.createTask(task);
                sendText(exchange, "Task created successfully", 201);
            } else {
                // Если ID есть, обновляем существующую задачу
                taskManager.updateTask(task);
                sendText(exchange, "Task updated successfully", 200);
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            sendError(exchange, "Invalid JSON format", 400);
        }
    }


    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if ("/tasks".equals(path)) {
            taskManager.deleteAllTasks();
            sendText(exchange, "All tasks deleted successfully", 200);
        } else if (path.startsWith("/tasks/")) {
            try {
                int taskId = extractIdFromPath(path);
                if (taskManager.getTaskById(taskId) == null) {
                    sendNotFound(exchange);
                } else {
                    taskManager.deleteTaskById(taskId);
                    sendText(exchange, "Task deleted successfully", 200);
                }
            } catch (NumberFormatException e) {
                sendError(exchange, "Invalid task ID", 400);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
