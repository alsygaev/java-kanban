import Managers.FileBackedTaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("testEmpty", ".csv");
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
        File tempFile = File.createTempFile("testTasks", ".csv");
        tempFile.deleteOnExit();

        // Создаём менеджер и добавляем задачи
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task = new Task("Tasks.Task 1", "Description 1");
        Epic epic = new Epic("Tasks.Epic 1", "Description Tasks.Epic 1");
        Subtask subtask = new Subtask("Tasks.Subtask 1", "Description Tasks.Subtask 1", 2);

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
        File tempFile = File.createTempFile("testMultipleTasks", ".csv");
        tempFile.deleteOnExit();

        // Создаём менеджер и добавляем несколько задач
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Tasks.Task 1", "Description 1");
        Task task2 = new Task("Tasks.Task 2", "Description 2");
        Epic epic1 = new Epic("Tasks.Epic 1", "Description Tasks.Epic 1");
        Epic epic2 = new Epic("Tasks.Epic 2", "Description Tasks.Epic 2");
        Subtask subtask1 = new Subtask("Tasks.Subtask 1", "Description Tasks.Subtask 1", 3);

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
