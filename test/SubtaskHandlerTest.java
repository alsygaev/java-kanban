import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest {
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
    void testCreateAndGetSubtask() throws IOException, InterruptedException {
        // Создаём Epic
        Epic epic = new Epic("Test Epic", "Epic Description");
        int epicId = taskManager.createEpic(epic);

        // Создание Subtask, привязанного к Epic
        Subtask subtask = new Subtask("Test Subtask", "Description", epicId);
        String json = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // Проверяем, что Subtask добавлена
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(1, subtasks.length);
        assertEquals("Test Subtask", subtasks[0].getName());
    }


    @Test
    void testGetSubtaskById() throws IOException, InterruptedException {
        // Создаём Epic
        Epic epic = new Epic("Test Epic", "Epic Description");
        int epicId = taskManager.createEpic(epic);

        // Создаём Subtask, привязанный к Epic
        Subtask subtask = new Subtask("Test Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        // Проверяем получение Subtask по ID
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask fetchedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getName(), fetchedSubtask.getName());
        assertEquals(subtask.getDescription(), fetchedSubtask.getDescription());
    }

    @Test
    void testDeleteAllSubtasks() throws IOException, InterruptedException {
        // Создаём Epic
        Epic epic = new Epic("Test Epic", "Epic Description");
        int epicId = taskManager.createEpic(epic);

        // Создаём несколько Subtasks
        taskManager.createSubtask(new Subtask("Subtask 1", "Description 1", epicId));
        taskManager.createSubtask(new Subtask("Subtask 2", "Description 2", epicId));

        // Удаляем все Subtasks
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что все Subtasks удалены
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(0, subtasks.length);
    }

    @Test
    void testDeleteSubtaskById() throws IOException, InterruptedException {
        // Создаём Epic
        Epic epic = new Epic("Test Epic", "Epic Description");
        int epicId = taskManager.createEpic(epic);

        // Создаём Subtask
        Subtask subtask = new Subtask("Subtask to delete", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        // Удаляем Subtask по ID
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что Subtask удалена
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testInvalidJsonOnCreateSubtask() throws IOException, InterruptedException {
        String invalidJson = "{ invalid }";

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
