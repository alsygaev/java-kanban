import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager, HistoryManager {

    private InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private int generatorId = 1;

    //Create Task
    @Override
    public int createTask(Task task) {
        int id = generatorId++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    //Create Subtask
    @Override
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
    @Override
    public int createEpic(Epic epic) {
        int id = generatorId++;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    //Get all tasks
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    //Get task by Id
    @Override
    public Task getTaskById(int id) {

        historyManager.addToHistory(tasks.get(id));

        return tasks.get(id);
    }

    //Get all subtasks
    @Override
    public List<Subtask> getAllSubtasksByEpic(int epicId) {
        List<Subtask> subtaskList = new ArrayList<>();
        subtasks.values().stream()
                .filter(s -> s.getEpicId() == epicId)
                .forEach(task -> subtaskList.add(task));
        return subtaskList;
    }

    //Get all epics
    @Override
    public List<Subtask> getAllSubtasks() {

        return new ArrayList<>(subtasks.values());

    }

    //Get subtask by Id
    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

        historyManager.addToHistory(subtask);

        return subtask;

    }

    //Get all epics
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //Get epic by id
    @Override
    public Epic getEpicById(int id) {

        historyManager.addToHistory(epics.get(id));

        return epics.get(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        // Update subtask
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId());

    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteEpicById(int id) {
        // Удаляем подзадачи эпика
        getAllSubtasksByEpic(id).forEach(subtask -> historyManager.removeFromHistory(subtask.getEpicId()));
        getAllSubtasksByEpic(id).forEach(subtask -> deleteSubtaskById(subtask.getId()));

        // Удаляем эпик
        epics.remove(id);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            subtasks.remove(id);
            historyManager.removeFromHistory(id);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void deleteAllTasks() {
        List<Task> allTasks = getAllTasks();
        allTasks.forEach(task -> historyManager.removeFromHistory(task.getId()));
        tasks.clear();

    }

    @Override
    public void deleteAllEpics() {
        List<Epic> allEpics = getAllEpics();
        allEpics.forEach(epic -> historyManager.removeFromHistory(epic.getId()));
        epics.clear();

    }

    @Override
    public void deleteAllSubtasksByEpic(int epicId) {
        getAllSubtasksByEpic(epicId).forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        getAllSubtasksByEpic(epicId).forEach(subtask -> deleteSubtaskById(subtask.getId()));
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteAllSubtasks() {
        List<Subtask> allSubtasks = getAllSubtasks();
        allSubtasks.forEach(task -> historyManager.removeFromHistory(task.getId()));
        subtasks.clear();
        updateAllEpicStatusToNew();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Метод для обновления статуса эпика
    private void updateEpicStatus(int epicId) {
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
                break;
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

    // Метод для обновления статуса эпика
    private void updateAllEpicStatusToNew() {
        epics.values().stream().forEach(task -> task.setStatus(TaskStatus.NEW));
    }


    @Override
    public void addToHistory(Task task) {
        historyManager.addToHistory(task);
    }

    @Override
    public void removeFromHistory(int id) {

    }
}
