import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Task task = new Task("Task", "TaskDescription");
        int taskId = taskManager.createTask(task);

        assertNotNull(taskManager.getTaskById(taskId));
        assertEquals("TaskDescription", taskManager.getTaskById(taskId).getDescription());
    }

    @Test
    void createAndRetrieveEpicWithSubtasksTest() {
        Epic epic = new Epic("Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "SubtaskDescription 1", epicId);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "SubtaskDescription 2", epicId);
        taskManager.createSubtask(subtask2);

        assertEquals(2, taskManager.getAllSubtasksByEpic(epicId).size());
    }

    @Test
    void updateTaskStatusTest() {
        Task task = new Task("Task", "TaskStatus");
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
        Epic epic = new Epic("Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "SubtaskDescription 1", epicId);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "SubtaskDescription 2", epicId);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.DONE, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void deleteTaskByIdTest() {
        Task task = new Task("Task", "TaskDescription");
        int taskId = taskManager.createTask(task);

        taskManager.deleteTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId));
    }

    @Test
    void deleteAllTasksTest() {
        taskManager.createTask(new Task("Task 1", "TaskDescription 1"));
        taskManager.createTask(new Task("Task 2", "TaskDescription 2"));

        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void deleteSubtaskByIdTest() {
        Epic epic = new Epic("Epic", "EpicDescription");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "SubtaskDescription", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtaskId);
        assertTrue(taskManager.getAllSubtasksByEpic(epicId).isEmpty());
    }

    @Test
    void deleteAllSubtasksByEpicTest() {
        Epic epic = new Epic("Epic Test", "Java Course");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "SubtaskDescription 1", epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "SubtaskDescription 2", epicId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteAllSubtasksByEpic(epicId);
        assertTrue(taskManager.getAllSubtasksByEpic(epicId).isEmpty());
    }

    @Test
    void deleteAllEpicsTest() {
        Epic epic1 = new Epic("Epic 1", "EpicDescription 1");
        Epic epic2 = new Epic("Epic 2", "EpicDescription 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("History Task 1", "HistoryTaskDescription 1");
        Task task2 = new Task("History Task 2", "HistoryTaskDescription 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id);

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals("History Task 1", history.get(0).getName());
        assertEquals("History Task 2", history.get(1).getName());
    }
}