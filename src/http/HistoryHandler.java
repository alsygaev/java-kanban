package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandlers {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equals(method) && "/history".equals(path)) {
                handleGet(exchange);
            } else {
                sendText(exchange, "Method not allowed or invalid path", 405);
            }
        } catch (Exception e) {
            sendError(exchange, "Internal server error: " + e.getMessage(), 500);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        // Получение истории задач
        List<Task> history = taskManager.getHistory();

        if (history.isEmpty()) {
            sendJson(exchange, "[]", 200); // Возвращаем пустой список, если история пуста
        } else {
            sendJson(exchange, gson.toJson(history), 200);
        }
    }
}
