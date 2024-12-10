import tasks.Epic;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {

    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("this is epic", "epic description");
    }

    @org.junit.jupiter.api.Test
    void getDescription() {
        assertEquals("epic description", epic.getDescription());
    }

    @org.junit.jupiter.api.Test
    void setDescription() {
        epic.setDescription("This is new description");
        assertEquals("This is new description", epic.getDescription());
    }

    @org.junit.jupiter.api.Test
    void getStatus() {
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Status must be NEW");
    }

    @org.junit.jupiter.api.Test
    void setStatus() {
        epic.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "New status must be IN_PROGRESS");
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        String str = "Tasks.Epic{id=0', name='this is epic', description='epic description', status=NEW}";
        assertEquals(str, epic.toString());
    }
}