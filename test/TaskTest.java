import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    @BeforeEach
    void setUp() {
        task = new Task("this is Tasks.Task", "task description");
    }

    @Test
    void getName() {
        assertEquals("this is Tasks.Task", task.getName());
    }

    @Test
    void setName() {
        task.setName("this is new task");
        assertEquals("this is new task", task.getName());
    }

    @Test
    void getDescription() {
        assertEquals("task description", task.getDescription());
    }

    @Test
    void setDescription() {
        task.setDescription("this is new task description");
        assertEquals("this is new task description", task.getDescription());
    }

    @Test
    void setStatus() {
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void getStatus() {
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    void testToString() {
        String str = "Tasks.Task{id=0', name='this is task', description='task description', status=NEW}";
        assertEquals(task.toString(), task.toString());
    }
}