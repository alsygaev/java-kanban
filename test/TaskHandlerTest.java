import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest {
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
    void testCreateAndGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description");
        task.setStatus(TaskStatus.NEW);
        String json = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // Проверяем, что задача добавлена
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length);
        assertEquals("Test Task", tasks[0].getName());
    }

    @Test
    void testGetNonExistentTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/999");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Task to update", "Initial description");
        int taskId = taskManager.createTask(task);

        // Обновляем задачу
        task.setId(taskId);
        task.setName("Updated Task");
        task.setDescription("Updated description");
        String json = gson.toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем обновление
        url = URI.create("http://localhost:8080/tasks/" + taskId);
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task updatedTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Updated Task", updatedTask.getName());
        assertEquals("Updated description", updatedTask.getDescription());
    }

    @Test
    void testDeleteAllTasks() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Task 1", "Description 1"));
        taskManager.createTask(new Task("Task 2", "Description 2"));

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что все задачи удалены
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(0, tasks.length);
    }

    @Test
    void testDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task to delete", "Description");
        int taskId = taskManager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что задача удалена
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testInvalidJsonOnCreateTask() throws IOException, InterruptedException {
        String invalidJson = "{ invalid }";

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
