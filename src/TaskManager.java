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

        System.out.println("Task with ID " + task.getId() + " / name: " + task.getName() + " / description: " + task.getDescription() +
                " / " + task.getStatus() + " was created.");

        return id;
    }

    //Create Subtask
    public int createSubtask(int epicId, Subtask subtask) {
        int id = generatorId++;
        if (epics.containsKey(epicId)) {
            subtask.setId(id);
            subtasks.put(id, subtask);

            System.out.println("Subtask with ID " + subtask.getId() + " / name: " + subtask.getName() + " / description: " + subtask.getDescription() +
                    " in epic: " + epics.get(epicId) +
                    " / " + subtask.getStatus() + " was created.");
            return id;
        } else {
            System.out.println("Subtask is not created. Epic with id: " + epicId + " does not exist.");
            return -1;
        }
    }

    //Create Epic
    public int createEpic(Epic epic) {
        int id = generatorId++;
        epic.setId(id);
        epics.put(id, epic);
        System.out.println("Epic with ID " + epic.getId() + " / name: " + epic.getName() + " /description: " + epic.getDescription() +
                " / " + epic.getStatus() + " was created.");

        return id;
    }

    //Get all tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        tasks.values().forEach(task -> taskList.add(task));
        return taskList;
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
        //subtasks.values().forEach(System.out::println);
    }

    //Get subtask by Id
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        //Epic epic = epics.get(subtask.getEpicId());
        //updateEpic(epic.setStatus(TaskStatus.IN_PROGRESS));
        return subtask;

    }

    //Get all epics
    public List<Epic> getAllEpics() {
        List<Epic> epicList = new ArrayList<>();
        epics.values().forEach(epic -> epicList.add(epic));
        return epicList;
    }

    //Get epic by id
    public Epic getAllEpicById(int id) {
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

        // Get epicId which has subtasks
        int epicId = subtask.getEpicId();

        // Get subtasks list by epicId
        List<Subtask> subtaskList = getAllSubtasksByEpic(epicId);

        // Current status
        TaskStatus epicStatus = TaskStatus.NEW;

        boolean hasInProgress = false;

        for (Subtask s : subtaskList) {
            if (s.getStatus() == TaskStatus.IN_PROGRESS) {
                hasInProgress = true;
                break;
            }
            if (s.getStatus() == TaskStatus.DONE) {
                epicStatus = TaskStatus.DONE;
            } else {
                epicStatus = TaskStatus.IN_PROGRESS;
            }
        }

        // Update epic
        if (hasInProgress) {
            epicStatus = TaskStatus.IN_PROGRESS;
        } else if (epicStatus == TaskStatus.NEW && subtaskList.isEmpty()) {
            epicStatus = TaskStatus.NEW;
        }

        Epic epic = getAllEpicById(epicId);
        if (epic != null) {
            epic.setStatus(epicStatus);
            epics.put(epicId, epic);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        subtasks.remove(getAllSubtasksByEpic(id));
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    void deleteAllEpics() {
        epics.clear();
    }






}
