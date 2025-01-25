import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest {
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
    void testCreateAndGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        String json = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // Проверяем, что эпик добавлен
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(1, epics.length);
        assertEquals("Test Epic", epics[0].getName());
    }

    @Test
    void testGetNonExistentEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/999");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to update", "Initial description");
        int epicId = taskManager.createEpic(epic);

        // Обновляем эпик
        epic.setId(epicId);
        epic.setName("Updated Epic");
        epic.setDescription("Updated description");
        String json = gson.toJson(epic);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем обновление
        url = URI.create("http://localhost:8080/epics/" + epicId);
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic updatedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals("Updated Epic", updatedEpic.getName());
        assertEquals("Updated description", updatedEpic.getDescription());
    }

    @Test
    void testDeleteAllEpics() throws IOException, InterruptedException {
        taskManager.createEpic(new Epic("Epic 1", "Description 1"));
        taskManager.createEpic(new Epic("Epic 2", "Description 2"));

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что все эпики удалены
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic[] epics = gson.fromJson(response.body(), Epic[].class);
        assertEquals(0, epics.length);
    }

    @Test
    void testDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to delete", "Description");
        int epicId = taskManager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Проверяем, что эпик удалён
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void testInvalidJsonOnCreateEpic() throws IOException, InterruptedException {
        String invalidJson = "{ invalid }";

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
