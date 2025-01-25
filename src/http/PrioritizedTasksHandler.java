package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.util.List;

public class PrioritizedTasksHandler extends BaseHttpHandlers {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedTasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod()) && "/prioritized".equals(exchange.getRequestURI().getPath())) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendJson(exchange, gson.toJson(prioritizedTasks), 200);
            } else {
                sendText(exchange, "Method not allowed or invalid path", 405);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Логирование ошибки
            sendError(exchange, "Internal Server Error: " + e.getMessage(), 500);
        }
    }


    private void handleGet(HttpExchange exchange) throws IOException {
        // Получаем список задач в порядке приоритета
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        if (prioritizedTasks.isEmpty()) {
            sendJson(exchange, "[]", 200); // Возвращаем пустой список, если задач нет
        } else {
            sendJson(exchange, gson.toJson(prioritizedTasks), 200);
        }
    }
}
