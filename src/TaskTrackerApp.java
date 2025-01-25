import http.HttpTaskServer;
import managers.TaskManager;
import managers.Managers;

import java.io.IOException;

public class TaskTrackerApp {
    public static void main(String[] args) {
        try {
            // Создаём менеджер задач
            TaskManager taskManager = Managers.getDefault();

            // Запускаем HTTP-сервер
            HttpTaskServer server = new HttpTaskServer(taskManager);
            server.start();

            // Информируем, что сервер запущен
            System.out.println("HTTP Task Server is running on port 8080.");

        } catch (IOException e) {
            System.err.println("Failed to start HTTP Task Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
