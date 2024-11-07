import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task task;
    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Task", "TaskDescription");
    }

    @Test
    void addToHistoryTest() {
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        historyManager.addToHistory(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(task, history.get(0), "История не найдена");
    }
}