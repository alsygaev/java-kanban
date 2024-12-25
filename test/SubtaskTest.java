import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic", "Description Epic");
        subtask = new Subtask("Subtask", "Description Subtask", epic.getId());
    }


    @Test
    void getDescription() {
        assertEquals("Description Subtask", subtask.getDescription());
    }

    @Test
    void setDescription() {
        subtask.setDescription("New description Subtask");
        assertEquals("New description Subtask", subtask.getDescription());
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
        String str = "Tasks.Subtask{id=0', name='Subtask', description='Description Subtask', status=NEW, epicId=0, duration=null, startTime=null}";
        assertEquals(str, subtask.toString());
    }
}