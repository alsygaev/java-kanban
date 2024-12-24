import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void createTaskTest() {
        Task task = new Task("Tasks.Task", "TaskDescription");
        int taskId = taskManager.createTask(task);

        assertNotNull(taskManager.getTaskById(taskId));
        assertEquals("TaskDescription", taskManager.getTaskById(taskId).getDescription());
    }

    @Test
    void createAndRetrieveEpicWithSubtasksTest() {
        Epic epic = new Epic("Tasks.Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Tasks.Subtask 1", "SubtaskDescription 1", epicId);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Tasks.Subtask 2", "SubtaskDescription 2", epicId);
        taskManager.createSubtask(subtask2);

        assertEquals(2, taskManager.getAllSubtasksByEpic(epicId).size());
    }

    @Test
    void updateTaskStatusTest() {
        Task task = new Task("Tasks.Task", "Tasks.TaskStatus");
        int taskId = taskManager.createTask(task);

        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getTaskById(taskId).getStatus());

        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        assertEquals(TaskStatus.DONE, taskManager.getTaskById(taskId).getStatus());
    }

    @Test
    void updateSubtaskAndEpicStatusTest() {
        Epic epic = new Epic("Tasks.Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Tasks.Subtask 1", "SubtaskDescription 1", epicId);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Tasks.Subtask 2", "SubtaskDescription 2", epicId);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void deleteTaskByIdTest() {
        Task task = new Task("Tasks.Task", "TaskDescription");
        int taskId = taskManager.createTask(task);

        taskManager.deleteTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId));
    }

    @Test
    void deleteAllTasksTest() {
        taskManager.createTask(new Task("Tasks.Task 1", "TaskDescription 1"));
        taskManager.createTask(new Task("Tasks.Task 2", "TaskDescription 2"));

        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void deleteSubtaskByIdTest() {
        Epic epic = new Epic("Tasks.Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Tasks.Subtask", "SubtaskDescription", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtaskId);
        assertTrue(taskManager.getAllSubtasksByEpic(epicId).isEmpty());
    }

    @Test
    void deleteAllSubtasksByEpicTest() {
        Epic epic = new Epic("Tasks.Epic Test", "Java Course");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Tasks.Subtask 1", "SubtaskDescription 1", epicId);
        Subtask subtask2 = new Subtask("Tasks.Subtask 2", "SubtaskDescription 2", epicId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteAllSubtasksByEpic(epicId);
        assertTrue(taskManager.getAllSubtasksByEpic(epicId).isEmpty());
    }

    @Test
    void deleteAllEpicsTest() {
        Epic epic1 = new Epic("Tasks.Epic 1", "EpicDescription 1");
        Epic epic2 = new Epic("Tasks.Epic 2", "EpicDescription 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("History Tasks.Task 1", "HistoryTaskDescription 1");
        Task task2 = new Task("History Tasks.Task 2", "HistoryTaskDescription 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals("History Tasks.Task 1", history.get(0).getName());
        assertEquals("History Tasks.Task 2", history.get(1).getName());
    }

    @Test
    void getHistoryAfterCreatingTasksTest() {
        Task task1 = new Task("Tasks.Task 1", "TaskDescription 1");
        task1.setId(1);
        Task task2 = new Task("Tasks.Task 2", "TaskDescription 2");
        task2.setId(2);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Запросим задачи, чтобы они добавились в историю
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи.");
        assertEquals(task1, history.get(0), "Первая задача должна быть Tasks.Task 1.");
        assertEquals(task2, history.get(1), "Вторая задача должна быть Tasks.Task 2.");
    }

    @Test
    void deleteTaskShouldRemoveFromHistoryTest() {
        Task task1 = new Task("Tasks.Task 1", "TaskDescription 1");
        task1.setId(1);
        Task task2 = new Task("Tasks.Task 2", "TaskDescription 2");
        task2.setId(2);

        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);

        taskManager.deleteTaskById(task1Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать 1 задачу после удаления Tasks.Task 1.");
        assertEquals(task2, history.get(0), "Единственная задача в истории должна быть Tasks.Task 2.");
    }

    @Test
    void deleteSubtaskShouldUpdateEpicTest() {
        Epic epic = new Epic("Tasks.Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Tasks.Subtask", "SubtaskDescription", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtaskId);

        List<Subtask> subtasks = taskManager.getAllSubtasksByEpic(epicId);
        assertTrue(subtasks.isEmpty(), "Все подзадачи эпика должны быть удалены.");

        Epic updatedEpic = taskManager.getEpicById(epicId);
        assertNotNull(updatedEpic, "Эпик не должен быть удалён.");
        assertTrue(taskManager.getAllSubtasksByEpic(epicId).isEmpty(), "В эпике не должно оставаться подзадач.");
    }

    @Test
    void taskTimeSlotValidationTest() {
        Task task1 = new Task("Task 1", "Description 1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setStartTime(task1.getStartTime().plusMinutes(30));
        task2.setDuration(Duration.ofMinutes(60));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createTask(task2);
        });

        assertEquals("The new task is crossing with another task", exception.getMessage());
    }
}