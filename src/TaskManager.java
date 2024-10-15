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

        System.out.println("Subtask " + subtask.hashCode() + " updated to " + subtask.getStatus());

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

        System.out.println("Epic " + epic.hashCode() + " updated to " + epic.getStatus());
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
        for (Epic epic : epics.keySet()) {
            System.out.println("Epic: " + epic.getName() + " Status: " + epic.getStatus());
            List<Subtask> subtaskList = epics.get(epic);
            for (Subtask subtask : subtaskList) {
                System.out.println("  Subtask: " + subtask.getName() + " Status: " + subtask.getStatus());
                List<Task> taskList = subtasks.get(subtask);
                for (Task task : taskList) {
                    System.out.println("    Task: " + task.getName() + " Status: " + task.getStatus());
                }
            }
        }
    }

    // Поиск Epic по имени
    public Epic findEpicByName(String name) {
        return epics.keySet().stream()
                .filter(epic -> epic.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    // Поиск Subtask по имени
    public Subtask findSubtaskByName(String name) {
        return epics.values().stream()
                .flatMap(List::stream) // Преобразуем List<Subtask> в Stream<Subtask>
                .filter(subtask -> subtask.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    // Поиск Task по имени
    public Task findTaskByName(String name) {
        return subtasks.values().stream()
                .flatMap(List::stream) // Преобразуем List<Task> в Stream<Task>
                .filter(task -> task.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}

/*

public class TaskManager {

    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Task> tasks = new HashMap<>();


    //Создание Epic
    public void createEpic(String name, String description) {
        Epic epic = new Epic(name, description, TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        System.out.println("Epic " + epic.getId() + " created");
    }

    //Создание Subtask
    public void createSubtask(int epicId, String name, String description) {
        if (epics.containsKey(epicId)) {
            Subtask subtask = new Subtask(name, description, TaskStatus.NEW, epicId);
            subtasks.put(subtask.getId(), subtask);
            epics.get(epicId).getSubtasks().add(subtask);
            System.out.println("Subtask " + subtask.getId() + " created." +
                    " Epic with ID: " + epicId + " with Name: " + name + " and Description: " + description +
                    " Status: " + TaskStatus.NEW);
        } else {
            System.out.println("Epic with id " + epicId + " not found");
        }
    }

    //Создание Task
    public void createTask(int subtaskId, String name, String description) {
        if (subtasks.containsKey(subtaskId)) {
            Task task = new Task(name, description, TaskStatus.NEW);
            tasks.put(task.getId(), task);
            subtasks.get(subtaskId).getTasks().add(task);
            System.out.println("Task " + task.getId() + " created. " + " Name: " + name + " and Description: " + description +
                    " Status: " + TaskStatus.NEW + "SubtaskId: "+ subtaskId);
        } else {
            System.out.println("Subtask with id " + subtaskId + " not found");
        }
    }

    //Метод обновления статуса Task

    public void updateTaskStatus(int epicId, int subtaskId, int taskId, TaskStatus status) {
        // Обновляем статус Task
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).setStatus(status);
            System.out.println("Task " + taskId + " updated to " + status);

            // Обновляем статус Subtask
            updateSubtaskStatus(epicId, subtaskId);
        } else {
            System.out.println("Task with id " + taskId + " not found");
        }
    }

    private void updateSubtaskStatus(int epicId, int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            boolean allTasksDone = subtask.getTasks().stream().allMatch(task -> task.getStatus() == TaskStatus.DONE);
            boolean anyTaskInProgress = subtask.getTasks().stream().anyMatch(task -> task.getStatus() == TaskStatus.IN_PROGRESS);

            if (allTasksDone) {
                subtask.setStatus(TaskStatus.DONE);
            } else if (anyTaskInProgress) {
                subtask.setStatus(TaskStatus.IN_PROGRESS);
            } else {
                subtask.setStatus(TaskStatus.NEW);
            }

            System.out.println("Subtask " + subtaskId + " updated to " + subtask.getStatus());

            // Обновляем статус Epic
            updateEpicStatus(epicId);
        } else {
            System.out.println("Subtask with id " + subtaskId + " not found");
        }
    }


    //Метод обновления статуса Epic
    private void updateEpicStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            boolean allSubtasksDone = epic.getSubtasks().stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);
            boolean anySubtasksInProgress = epic.getSubtasks().stream().allMatch(task -> task.getStatus() == TaskStatus.IN_PROGRESS);

            if (allSubtasksDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (anySubtasksInProgress) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else {
                epic.setStatus(TaskStatus.NEW);
            }

            System.out.println("Epic " + epicId + " updated to " + epic.getStatus());
        } else {
            System.out.println("Task with id " + epicId + " not found");
        }
    }

    //Метод получить все Epicи по epicId
    public void getAllEpics(int epicId) {
        if (epics.containsKey(epicId)) {
            //tasks.get(epicId).setStatus(status);
            Epic epic = epics.get(epicId);
            System.out.println("Epic: " + epic.getId() + " , name: " + epic.getName() + " , description: " +
                    epic.getDescription() + " , status: " + epic.getStatus());

            List<Subtask> subtasks = epic.getSubtasks();

            if (subtasks.isEmpty()) {
                System.out.println("Subtasks for this epic are empty");
            } else {
                for (Subtask subtask : subtasks) {
                    System.out.println("Subtask: " + subtask.getId() + " , name: " + subtask.getName() + " , description: " +
                            subtask.getDescription() + " , status: " + subtask.getStatus() + " , epicId: " + subtask.getEpicId());

                    List<Task> tasks = subtask.getTasks();
                    if (tasks.isEmpty()) {
                        System.out.println("Tasks for this subtask are empty");
                    } else {
                        for (Task task : tasks) {
                            System.out.println("Task: " + task.getId() + " , name: " + task.getName() + " , description: " +
                                    task.getDescription() + " , status: " + task.getStatus());
                        }
                    }
                }
            }
        } else {
            System.out.println("Epic with id " + epicId + " not found");
        }
    }


}
*/
