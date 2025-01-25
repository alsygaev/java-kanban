package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private static final Gson gson = new Gson();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureHandlers();
    }

    public static Gson getGson() {
        return new Gson().newBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter()) // Адаптер для Duration
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter()) // Адаптер для LocalDateTime
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter()) // Адаптер для LocalTime
                .create();
    }


    private void configureHandlers() {
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));
    }
    public void start() {
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }


}
