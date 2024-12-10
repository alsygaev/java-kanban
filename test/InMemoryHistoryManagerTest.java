import Managers.InMemoryHistoryManager;
import Managers.InMemoryTaskManager;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();

        task1 = new Task("Tasks.Task 1", "Tasks.Task 1 Description");
        taskManager.createTask(task1);

        task2 = new Task("Tasks.Task 2", "Tasks.Task 2 Description");
        taskManager.createTask(task2);

        task3 = new Task("Tasks.Task 3", "Tasks.Task 3 Description");
        taskManager.createTask(task3);

    }

    @Test
    void addToHistoryTest() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История  должна содержать 3 задачи");
        assertEquals(task1, history.get(0), "Первая задача - Tasks.Task 1");
        assertEquals(task2, history.get(1), "Первая задача - Tasks.Task 2");
        assertEquals(task3, history.get(2), "Первая задача - Tasks.Task 3");
    }

    @Test
    void addDublicateTaskToHistoryTest() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task1);

        final List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История  не должна содержать дубликаты");
        assertEquals(task2, history.get(0), "Первая задача - Tasks.Task 2 после попытки добавить дубликат");
        assertEquals(task1, history.get(1), "Первая задача - Tasks.Task 1");
    }

    @Test
    void removeTaskToHistoryTest() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.removeFromHistory(task3.getId());

        final List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История  должна содержать 2 задачи");
        assertFalse(history.contains(task3), "История не должна содержать Tasks.Task 3");
    }

}