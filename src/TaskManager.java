import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private int generatorId = 1;

    //Create Task
    public int createTask(Task task) {
        int id = generatorId++;
        task.setId(id);
        tasks.put(id, task);

        return id;
    }

    //Create Subtask
    public int createSubtask(Subtask subtask) {
        int id = generatorId++;
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(id);
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getEpicId());
            return id;
        } else {
            return -1;
        }
    }

    //Create Epic
    public int createEpic(Epic epic) {
        int id = generatorId++;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    //Get all tasks
    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    //Get task by Id
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    //Get all subtasks
    public List<Subtask> getAllSubtasksByEpic(int epicId) {
        List<Subtask> subtaskList = new ArrayList<>();
        subtasks.values().stream()
                .filter(s -> s.getEpicId() == epicId)
                .forEach(task -> subtaskList.add(task));
        return subtaskList;
    }

    //Get subtask by Id
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        return subtask;

    }

    //Get all epics
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //Get epic by id
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        // Update subtask
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());

    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        subtasks.remove(getAllSubtasksByEpic(id));
    }

    //добавить обновление статуса эпика
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            subtasks.remove(id);
            updateEpicStatus(epicId);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        tasks.clear();
    }

    public void deleteAllSubtasksByEpic(int epicId) {
        getAllSubtasksByEpic(epicId).forEach(subtask -> deleteSubtaskById(subtask.getId()));
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // Метод для обновления статуса эпика
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        List<Subtask> subtaskList = getAllSubtasksByEpic(epicId);

        boolean hasInProgress = false;
        boolean hasNew = false;
        boolean allDone = true;

        for (Subtask s : subtaskList) {
            if (s.getStatus() == TaskStatus.IN_PROGRESS) {
                hasInProgress = true;
            }
            if (s.getStatus() == TaskStatus.NEW) {
                hasNew = true;
            }
            if (s.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
        }
        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (hasInProgress) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (hasNew) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

}
