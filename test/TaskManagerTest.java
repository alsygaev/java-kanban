import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
        assertNotNull(taskManager, "taskManager должен быть инициализирован");
    }

    @Test
    void calculateEpicStatus_AllNew() {
        Epic epic = new Epic("Epic", "Description Epic");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epicId);
        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epicId);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.NEW, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void calculateEpicStatus_AllDone() {
        Epic epic = new Epic("Epic", "DescriptionEpic");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epicId);
        subtask1.setStatus(TaskStatus.DONE);

        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epicId);
        subtask2.setStatus(TaskStatus.DONE);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void calculateEpicStatus_MixedStatuses() {
        Epic epic = new Epic("Epic", "DescriptionEpic");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epicId);
        subtask1.setStatus(TaskStatus.NEW);

        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epicId);
        subtask2.setStatus(TaskStatus.DONE);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void calculateEpicStatus_AllInProgress() {
        Epic epic = new Epic("Epic", "DescriptionEpic");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "DescriptionSubtask1", epicId);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);

        Subtask subtask2 = new Subtask("Subtask2", "DescriptionSubtask2", epicId);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void testTaskTimeOverlap() {
        Task task1 = new Task("Task1", "DescriptionTask1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task1);

        Task task2 = new Task("Task2", "DescriptionTask2");
        task2.setStartTime(task1.getStartTime().plusMinutes(30));
        task2.setDuration(Duration.ofMinutes(60));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createTask(task2);
        });

        assertEquals("The new task has a cross with another task", exception.getMessage());
    }
}
