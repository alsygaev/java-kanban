import managers.FileBackedTaskManager;
import managers.ManagerSaveException;
import org.junit.jupiter.api.Test;
import java.io.File;


import static org.junit.jupiter.api.Assertions.*;

class FileExceptionTest {

    @Test
    void testSaveToNonExistentFile() {
        File file = new File("/invalid/path/tasks.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        assertThrows(ManagerSaveException.class, manager::save, "Expected ManagerSaveException when saving to an invalid path");
    }

    @Test
    void testLoadFromNonExistentFile() {
        File file = new File("non-existent.txt");
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(file), "Expected ManagerSaveException when loading from a non-existent file");
    }
}
