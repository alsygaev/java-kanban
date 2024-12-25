package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.List;

public interface TaskManager {

    // Create Tasks.Task
    int createTask(Task task);

    // Create Tasks.Subtask
    int createSubtask(Subtask subtask);

    // Create Tasks.Epic
    int createEpic(Epic epic);

    // Get all tasks
    List<Task> getAllTasks();

    // Get task by Id
    Task getTaskById(int id);

    // Get all subtasks by epic
    List<Subtask> getAllSubtasksByEpic(int epicId);

    // Get subtask by Id
    Subtask getSubtaskById(int subtaskId);

    // Get subtask by Id
    List<Subtask> getAllSubtasks();

    // Get all epics
    List<Epic> getAllEpics();

    // Get epic by id
    Epic getEpicById(int id);

    // Update task
    void updateTask(Task task);

    // Update epic
    void updateEpic(Epic epic);

    // Update subtask
    void updateSubtask(Subtask subtask);

    // Delete task by id
    void deleteTaskById(int id);

    // Delete epic by id
    void deleteEpicById(int id);

    // Delete subtask by id
    void deleteSubtaskById(int id);

    // Delete all tasks
    void deleteAllTasks();

    // Delete all epics
    void deleteAllEpics();

    // Delete all subtasks by epic
    void deleteAllSubtasksByEpic(int epicId);

    // Delete all subtasks
    void deleteAllSubtasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
