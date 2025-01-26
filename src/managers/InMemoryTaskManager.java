package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private final Map<LocalDateTime, Boolean> timeGrid = new HashMap<>();

    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected int generatorId = 1;

    // Create Task
    @Override
    public int createTask(Task task) {
        if (!isTimeSlotAvailable(task)) {
            throw new IllegalArgumentException("The new task is crossing with another task");
        }
        int id = generatorId++;
        task.setId(id);
        tasks.put(id, task);
        prioritizedTasks.add(task);
        reserveTimeSlot(task);
        return id;
    }

    // Create Subtask
    @Override
    public int createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Epic with ID " + subtask.getEpicId() + " does not exist");
        }
        if (!isTimeSlotAvailable(subtask)) {
            throw new IllegalArgumentException("The new subtask is crossing with another task");
        }
        int id = generatorId++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        prioritizedTasks.add(subtask);
        reserveTimeSlot(subtask);
        updateEpicStatus(subtask.getEpicId());
        return id;
    }

    // Create Epic
    @Override
    public int createEpic(Epic epic) {
        int id = generatorId++;
        epic.setId(id);
        epics.put(id, epic);
        prioritizedTasks.add(epic);
        return id;
    }

    // Update Task
    @Override
    public void updateTask(Task task) {
        Task existingTask = tasks.get(task.getId());
        if (existingTask != null) {
            releaseTimeSlot(existingTask);
        }
        if (!isTimeSlotAvailable(task)) {
            if (existingTask != null) {
                reserveTimeSlot(existingTask);
            }
            throw new IllegalArgumentException("The updated task is crossing with another task");
        }
        prioritizedTasks.remove(existingTask);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        reserveTimeSlot(task);
    }

    // Update Subtask
    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask existingSubtask = subtasks.get(subtask.getId());
        if (existingSubtask != null) {
            releaseTimeSlot(existingSubtask);
        }
        if (!isTimeSlotAvailable(subtask)) {
            if (existingSubtask != null) {
                reserveTimeSlot(existingSubtask);
            }
            throw new IllegalArgumentException("The updated subtask is crossing with another task");
        }
        prioritizedTasks.remove(existingSubtask);
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        reserveTimeSlot(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    // Update Epic
    @Override
    public void updateEpic(Epic epic) {
        prioritizedTasks.remove(epics.get(epic.getId()));
        epics.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
    }

    // Get all tasks
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Get task by Id
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return task;
    }

    // Get all subtasks by epic
    @Override
    public List<Subtask> getAllSubtasksByEpic(int epicId) {
        List<Subtask> subtaskList = new ArrayList<>();
        subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .forEach(subtaskList::add);
        return subtaskList;
    }

    // Get all subtasks
    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Get subtask by Id
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addToHistory(subtask);
        return subtask;
    }

    // Get all epics
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Get epic by Id
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    // Delete Task by Id
    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        removeTaskFromManager(task);
    }

    // Delete Subtask by Id
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            removeTaskFromManager(subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    // Delete Epic by Id
    @Override
    public void deleteEpicById(int id) {
        getAllSubtasksByEpic(id).forEach(subtask -> deleteSubtaskById(subtask.getId()));
        Epic epic = epics.remove(id);
        removeTaskFromManager(epic);
    }

    // Delete all tasks
    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(this::removeTaskFromManager);
        tasks.clear();
    }

    // Delete all epics
    @Override
    public void deleteAllEpics() {
        epics.values().forEach(this::removeTaskFromManager);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasksByEpic(int epicId) {
        getAllSubtasksByEpic(epicId).forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        getAllSubtasksByEpic(epicId).forEach(subtask -> deleteSubtaskById(subtask.getId()));
        updateEpicStatus(epicId);

    }

    // Delete all subtasks
    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(this::removeTaskFromManager);
        subtasks.clear();
        epics.values().forEach(epic -> epic.setStatus(TaskStatus.NEW));
    }

    // Get task history
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Get prioritized tasks
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Проверка пересечений
    private boolean isTimeSlotAvailable(Task task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();

        if (start == null || end == null) return true;

        for (LocalDateTime time = start; !time.isAfter(end); time = time.plusMinutes(15)) {
            if (Boolean.TRUE.equals(timeGrid.get(time))) {
                return false;
            }
        }
        return true;
    }

    private void reserveTimeSlot(Task task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();

        if (start != null && end != null) {
            for (LocalDateTime time = start; !time.isAfter(end); time = time.plusMinutes(15)) {
                timeGrid.put(time, true);
            }
        }
    }

    private void releaseTimeSlot(Task task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();

        if (start != null && end != null) {
            for (LocalDateTime time = start; !time.isAfter(end); time = time.plusMinutes(15)) {
                timeGrid.remove(time);
            }
        }
    }

    private void removeTaskFromManager(Task task) {
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.removeFromHistory(task.getId());
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> subtaskList = getAllSubtasksByEpic(epicId);

        boolean hasInProgress = false;
        boolean hasNew = false;
        boolean allDone = true;

        for (Subtask subtask : subtaskList) {
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                hasInProgress = true;
                break;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                hasNew = true;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
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
