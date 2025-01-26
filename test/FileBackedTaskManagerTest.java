import managers.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("tasks1", ".txt");
        tempFile.deleteOnExit(); // Удаляем файл после завершения тестов

        // Создаём менеджер с пустым состоянием
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        // Сохраняем состояние в файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что состояние менеджера пустое
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testSaveAndLoadWithTasks() throws IOException {
        File tempFile = File.createTempFile("tasks2", ".txt");
        tempFile.deleteOnExit();

        // Создаём менеджер и добавляем задачи
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task = new Task("Task 1", "Description Task 1");
        Epic epic = new Epic("Epic 1", "Description Epic 1");
        Subtask subtask = new Subtask("Subtask 1", "Description Subtask 1", 2);

        int taskId = manager.createTask(task);
        int epicId = manager.createEpic(epic);
        int subtaskId = manager.createSubtask(subtask);

        // Сохраняем состояние в файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем задачи
        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(taskId, loadedManager.getAllTasks().get(0).getId());
        assertEquals(task.getName(), loadedManager.getAllTasks().get(0).getName());

        // Проверяем эпики
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(epicId, loadedManager.getAllEpics().get(0).getId());
        assertEquals(epic.getName(), loadedManager.getAllEpics().get(0).getName());

        // Проверяем подзадачи
        assertEquals(1, loadedManager.getAllSubtasks().size());
        assertEquals(subtaskId, loadedManager.getAllSubtasks().get(0).getId());
        assertEquals(subtask.getName(), loadedManager.getAllSubtasks().get(0).getName());
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws IOException {
        File tempFile = File.createTempFile("tasks3", ".txt");
        tempFile.deleteOnExit();

        // Создаём менеджер и добавляем несколько задач
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Task 1", "Description Task 1");
        Task task2 = new Task("Task 2", "Description Task 2");
        Epic epic1 = new Epic("Epic 1", "Description Epic 1");
        Epic epic2 = new Epic("Epic 2", "Description Epic 2");
        Subtask subtask1 = new Subtask("Subtask 1", "Description Subtask 1", 3);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);

        // Сохраняем состояние в файл
        manager.save();

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем задачи
        assertEquals(2, loadedManager.getAllTasks().size());
        assertEquals(task1.getName(), loadedManager.getAllTasks().get(0).getName());
        assertEquals(task2.getName(), loadedManager.getAllTasks().get(1).getName());

        // Проверяем эпики
        assertEquals(2, loadedManager.getAllEpics().size());
        assertEquals(epic1.getName(), loadedManager.getAllEpics().get(0).getName());
        assertEquals(epic2.getName(), loadedManager.getAllEpics().get(1).getName());

        // Проверяем подзадачи
        assertEquals(1, loadedManager.getAllSubtasks().size());
        assertEquals(subtask1.getName(), loadedManager.getAllSubtasks().get(0).getName());
    }
}
