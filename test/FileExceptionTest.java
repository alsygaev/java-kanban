import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileExceptionTest {

    @Test
    void testSaveToNonExistentFile() {
        File file = new File("/invalid/path/tasks.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        assertThrows(IOException.class, manager::save);
    }

    @Test
    void testLoadFromNonExistentFile() {
        File file = new File("non-existent.txt");
        assertDoesNotThrow(() -> FileBackedTaskManager.loadFromFile(file));
    }
}
