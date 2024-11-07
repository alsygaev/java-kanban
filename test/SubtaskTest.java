import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        epic = new Epic("this is epic", "epic description");
        subtask = new Subtask("this is subtask", "subtask description", epic.getId());
    }


    @Test
    void getDescription() {
        assertEquals("subtask description", subtask.getDescription());
    }

    @Test
    void setDescription() {
        subtask.setDescription("new description");
        assertEquals("new description", subtask.getDescription());
    }

    @Test
    void getStatus() {
        assertEquals(TaskStatus.NEW, subtask.getStatus());
    }

    @Test
    void setStatus() {
        subtask.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
    }

    @Test
    void testToString() {
        String str = "Subtask{id=0', name='this is subtask', description='subtask description', status=NEW, epicId=0}";
        assertEquals(str, subtask.toString());
    }
}