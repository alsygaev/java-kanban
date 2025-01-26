import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest {
    private HttpTaskServer taskServer;
    private TaskManager taskManager;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        client = HttpClient.newHttpClient();
        gson = HttpTaskServer.getGson();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void testGetEmptyHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body()); // Проверяем, что возвращается пустой список
    }

    @Test
    void testGetHistoryWithTasks() throws IOException, InterruptedException {
        // Создаем задачи
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Запрашиваем задачи, чтобы они добавились в историю
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, history.length);
        assertEquals("Task 1", history[0].getName());
        assertEquals("Task 2", history[1].getName());
    }

    @Test
    void testHistoryAfterTaskDeletion() throws IOException, InterruptedException {
        // Создаем задачи
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Запрашиваем задачи, чтобы они добавились в историю
        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);

        // Удаляем первую задачу
        taskManager.deleteTaskById(task1Id);

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, history.length);
        assertEquals("Task 2", history[0].getName());
    }

    @Test
    void testInvalidMethod() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Method not allowed or invalid path", response.body());
    }
}
