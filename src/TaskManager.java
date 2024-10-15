import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskManager {

    private Map<Epic, List<Subtask>> epics = new HashMap<>();
    private Map<Subtask, List<Task>> subtasks = new HashMap<>();

    // Создание Epic
    public void createEpic(String name, String description) {
        Epic epic = new Epic(name, description, TaskStatus.NEW);
        epics.put(epic, new ArrayList<>());
        System.out.println("Epic " + epic.hashCode() + " created");
    }

    // Создание Subtask
    public void createSubtask(Epic epic, String name, String description) {
        if (epics.containsKey(epic)) {
            Subtask subtask = new Subtask(name, description, TaskStatus.NEW, epic);
            epics.get(epic).add(subtask);
            subtasks.put(subtask, new ArrayList<>());
            System.out.println("Subtask " + subtask.hashCode() + " created for Epic " + epic.hashCode());
        } else {
            System.out.println("Epic not found");
        }
    }

    // Создание Task
    public void createTask(Subtask subtask, String name, String description) {
        if (subtasks.containsKey(subtask)) {
            Task task = new Task(name, description, TaskStatus.NEW);
            subtasks.get(subtask).add(task);
            System.out.println("Task " + task.hashCode() + " created for Subtask " + subtask.hashCode());
        } else {
            System.out.println("Subtask not found");
        }
    }

    // Метод обновления статуса Task
    public void updateTaskStatus(Task task, TaskStatus status) {
        for (List<Task> taskList : subtasks.values()) {
            if (taskList.contains(task)) {
                task.setStatus(status);
                System.out.println("Task " + task.hashCode() + " updated to " + status);

                // Обновляем статус Subtask и Epic
                updateSubtaskStatus(getSubtaskContainingTask(task));
                return;
            }
        }
        System.out.println("Task not found");
    }

    // Метод обновления статуса Subtask
    private void updateSubtaskStatus(Subtask subtask) {
        List<Task> taskList = subtasks.get(subtask);
        boolean allTasksDone = taskList.stream().allMatch(task -> task.getStatus() == TaskStatus.DONE);
        boolean anyTaskInProgress = taskList.stream().anyMatch(task -> task.getStatus() == TaskStatus.IN_PROGRESS);

        if (allTasksDone) {
            subtask.setStatus(TaskStatus.DONE);
        } else if (anyTaskInProgress) {
            subtask.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            subtask.setStatus(TaskStatus.NEW);
        }

        // Обновляем статус Epic
        updateEpicStatus(subtask.getEpic());
    }

    // Метод обновления статуса Epic
    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtaskList = epics.get(epic);
        boolean allSubtasksDone = subtaskList.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);
        boolean anySubtasksInProgress = subtaskList.stream().anyMatch(subtask -> subtask.getStatus() == TaskStatus.IN_PROGRESS);

        if (allSubtasksDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (anySubtasksInProgress) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    // Метод поиска Subtask, содержащего Task
    private Subtask getSubtaskContainingTask(Task task) {
        return subtasks.entrySet().stream()
                .filter(entry -> entry.getValue().contains(task))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    // Получение всех Epic, Subtask и Task
    public void getAllEpics() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.keySet()) {
                System.out.println("EpicId: " + epic.hashCode() + "| Epic: " + epic.getName() + " Status: " + epic.getStatus());
                List<Subtask> subtaskList = epics.get(epic);
                for (Subtask subtask : subtaskList) {
                    System.out.println("  Subtask: " + subtask.getName() + " Status: " + subtask.getStatus());
                    List<Task> taskList = subtasks.get(subtask);
                    for (Task task : taskList) {
                        System.out.println("    Task: " + task.getName() + " Status: " + task.getStatus());
                    }
                }
            }
        } else {
            System.out.println("No Epic found");
        }
    }

    // Поиск Epic по хэш-коду
    public Epic findEpicByHashCode(Object hashCode) {
        if (hashCode instanceof Integer) {
            int code = (Integer) hashCode; // Приведение Object к int
            return epics.keySet().stream()
                    .filter(epic -> epic.hashCode() == code)
                    .findFirst()
                    .orElse(null);
        }
        return null; // Возвращаем null, если hashCode не является Integer
    }

    // Поиск Subtask по HashCode
    public Subtask findSubtaskByHashCode(Object hashCode) {
        if (hashCode instanceof Integer) {
            int code = (Integer) hashCode;
            return epics.values().stream()
                    .flatMap(List::stream) // Преобразуем List<Subtask> в Stream<Subtask>
                    .filter(subtask -> subtask.hashCode() == code)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // Поиск Task по HashCode
    public Task findTaskByHashCode(Object hashCode) {
        if (hashCode instanceof Integer) {
            int code = (Integer) hashCode;
            return subtasks.values().stream()
                    .flatMap(List::stream) // Преобразуем List<Task> в Stream<Task>
                    .filter(task -> task.hashCode() == code)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    //Удаление Epic
    public void deleteEpic(Epic epic) {
        epics.remove(epic);
        subtasks.entrySet().removeIf(entry -> entry.getKey().getEpic() == epic);
        subtasks.values().removeIf(taskList -> taskList.contains(epic));
    }

    //Удаление Task
    public void deleteTask(Task task) {
        for (Map.Entry<Subtask, List<Task>> entry : subtasks.entrySet()) {
            List<Task> taskList = entry.getValue();
            if (taskList.contains(task)) {
                taskList.remove(task);
                System.out.println("Task " + task.getName() + " удален.");
                return;
            }
        }
        System.out.println("Task не найден.");
    }
}
