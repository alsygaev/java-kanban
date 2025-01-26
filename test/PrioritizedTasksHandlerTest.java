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
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PrioritizedTasksHandlerTest {
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
    void testGetEmptyPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body()); // Проверяем, что возвращается пустой список
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        // Создаем задачи с разным временем начала
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.now().plusHours(1));
        task1.setDuration(Duration.ofMinutes(30));
        taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.now().plusHours(2));
        task2.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task2);

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, prioritizedTasks.length);
        assertEquals("Task 1", prioritizedTasks[0].getName());
        assertEquals("Task 2", prioritizedTasks[1].getName());
    }

    @Test
    void testGetPrioritizedTasksAfterDeletion() throws IOException, InterruptedException {
        // Создаем задачи
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.now().plusHours(1));
        task1.setDuration(Duration.ofMinutes(30));
        int task1Id = taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(LocalDateTime.now().plusHours(2));
        task2.setDuration(Duration.ofMinutes(60));
        int task2Id = taskManager.createTask(task2);

        // Удаляем одну задачу
        taskManager.deleteTaskById(task1Id);

        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, prioritizedTasks.length);
        assertEquals("Task 2", prioritizedTasks[0].getName());
    }

    @Test
    void testInvalidMethod() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
        assertEquals("Method not allowed or invalid path", response.body());
    }
}
