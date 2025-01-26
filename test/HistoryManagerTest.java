import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task1", "DescriptionTask1");
        task1.setId(1);

        task2 = new Task("Task2", "DescriptionTask2");
        task2.setId(2);

        task3 = new Task("Task3", "DescriptionTask3");
        task3.setId(3);
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "History must be empty.");
    }

    @Test
    void testAddToHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void testRemoveFromHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.removeFromHistory(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task2));
    }

    @Test
    void testDuplicateTaskInHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Duplicate tasks must not appear in history.");
    }
}
