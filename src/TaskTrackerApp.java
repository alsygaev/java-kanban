import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTrackerApp {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        // Создаем задачу
        System.out.println("\n");
        System.out.println("// Создаем задачу");

        Task task1 = new Task("this is Task", "Buy milk");
        Task task2 = new Task("this is Task", "Buy bread");
        Task task3 = new Task("this is Task", "Buy vegetables");

        // Добавляем задачу в TaskManager
        System.out.println("\n");
        System.out.println("// Добавляем задачу в TaskManager");

        int task1Id = taskManager.createTask(task1);
        System.out.println("Created new task: " + task1Id);
        int task2Id = taskManager.createTask(task2);
        System.out.println("Created new task: " + task2Id);
        int task3Id = taskManager.createTask(task3);
        System.out.println("Created new task: " + task3Id);

        Subtask subtask = new Subtask("Check subtask without Epic", "Subtask_1", 2 );
        taskManager.createSubtask(2, subtask);

        Epic epic = new Epic("This is Epic", "Курс Java");

        int epicId = taskManager.createEpic(epic);
        System.out.println("Created new epic: " + epicId);

        Subtask subtask1 = new Subtask("Check subtask with Epic", "Subtask_1", 5 );
        taskManager.createSubtask(epicId, subtask1);
        Subtask subtask2 = new Subtask("Check subtask with Epic", "Subtask_2", 5 );
        taskManager.createSubtask(epicId, subtask2);
        Subtask subtask3 = new Subtask("Check subtask with Epic", "Subtask_3", 5 );
        taskManager.createSubtask(epicId, subtask3);

        //Просмотр всех Тасков из Tasks
        System.out.println("\n");
        System.out.println("//Просмотр всех Тасков из Tasks");

        taskManager.getAllTasks();

        //Просмотр всех Эпиков из Epics
        System.out.println("\n");
        System.out.println("//Просмотр всех Эпиков из Epics");

        System.out.println("Эпики:");
        for (Task e : taskManager.getAllEpics()) {
            System.out.println(e);
            for (Task t : taskManager.getAllSubtasksByEpic(e.getId())) {
                System.out.println("--> " + t);
            }
        }

        //Найти эпик по ID
        System.out.println("\n");
        System.out.println("//Найти эпик по ID");

        int id = taskManager.getAllEpics().stream().findAny().get().getId();
        System.out.println(taskManager.getAllEpicById(id));

        //Обновление таска
        System.out.println("\n");
        System.out.println("//Обновление статуса таска IN_PROGRESS -> DONE");

        int taskId = taskManager.getAllTasks().stream().findAny().get().getId();
        Task task = taskManager.getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        System.out.println("CHANGE STATUS: IN_PROGRESS->DONE");
        System.out.println("Задачи:");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }

        //Обновление статуса сабтаска
        System.out.println("\n");
        System.out.println("//Обновление статуса сабтаска и эпика IN_PROGRESS");

        int epicIdForUpdate = taskManager.getAllEpics().stream().findAny().get().getId();
        System.out.println(epicIdForUpdate);
        int subtaskId = taskManager.getAllSubtasksByEpic(epicIdForUpdate).stream().findAny().get().getId();
        System.out.println(subtaskId);
        Subtask subtaskForUpdate = taskManager.getSubtaskById(subtaskId);
        subtaskForUpdate.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtaskForUpdate);

        System.out.println("Epic with ID: " + epicIdForUpdate + " has NEW STATUS: " +
                taskManager.getAllEpicById(epicIdForUpdate).getStatus());

        System.out.println("CHANGE STATUS: IN_PROGRESS->DONE");

        System.out.println("Задачи:");
        for (Task t : taskManager.getAllSubtasksByEpic(epicIdForUpdate)) {
            System.out.println(t);
        }

        //Обновление наименования/описания сабтаска
        System.out.println("\n");
        System.out.println("//Обновление наименования/описания сабтаска");

        int epicIdForUpdateNameSubtask = taskManager.getAllEpics().stream().findAny().get().getId();
        int subtaskIdForUpdateName = taskManager.getAllSubtasksByEpic(epicIdForUpdateNameSubtask).stream().findAny().get().getId();

        Subtask subtaskForUpdateName = taskManager.getSubtaskById(subtaskId);
        subtaskForUpdate.setName("Измененное наименование сабтаска");
        subtaskForUpdate.setDescription("Измененное описание сабтаска");

        taskManager.updateSubtask(subtaskForUpdateName);

        System.out.println("Subtask with id: "+ subtaskForUpdateName.getId()
                + " has NEW NAME: " + subtaskForUpdateName.getName());
        System.out.println("Subtask with id: "+ subtaskForUpdateName.getId()
                + " has NEW DESCRIPTION: " + subtaskForUpdateName.getDescription());

        System.out.println("Задачи:");
        for (Task t : taskManager.getAllSubtasksByEpic(epicIdForUpdate)) {
            System.out.println(t);
        }

        //Завершение эпика
        System.out.println("\n");
        System.out.println("//Завершение эпика");

        int epicIdForDone = 5;

        List<Subtask> subtaskIdForDone = taskManager.getAllSubtasksByEpic(epicIdForDone);

        for (Subtask s : subtaskIdForDone) {
            s.setStatus(TaskStatus.DONE);
            taskManager.updateSubtask(s);
        }

        System.out.println("Epic with ID: " + epicIdForUpdate + " has NEW STATUS: " +
                taskManager.getAllEpicById(epicIdForUpdate).getStatus());

        System.out.println("CHANGE STATUS: IN_PROGRESS->DONE");

        System.out.println("Задачи:");
        for (Subtask t : taskManager.getAllSubtasksByEpic(epicIdForUpdate)) {
            System.out.println(t);
        }

        //Удаление таска
        System.out.println("\n");
        System.out.println("//Удаление таска");

        int taskIdForDelete = taskManager.getAllTasks().stream().findAny().get().getId();

        taskManager.deleteTaskById(taskIdForDelete);
        System.out.println("Task with id: " + taskIdForDelete + " has DELETED");

        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }

        //Удаление всех тасков
        System.out.println("\n");
        System.out.println("//Удаление тасков");

        taskManager.deleteAllTasks();
        System.out.println("All tasks has DELETED");

        if (taskManager.getAllTasks().isEmpty()) {
            System.out.println("No tasks found");
        }

        //Удаление сабтаска
        System.out.println("\n");
        System.out.println("//Удаление сабтаска");

        int epicIdForDeleteSubtask = taskManager.getAllEpics().stream().findAny().get().getId();
        int subtaskIdForDelete= taskManager.getAllSubtasksByEpic(epicIdForDeleteSubtask).stream().findAny().get().getId();

        taskManager.deleteSubtaskById(subtaskIdForDelete);
        System.out.println("Subtask with id: " + taskIdForDelete + " has DELETED");

        for (Subtask t : taskManager.getAllSubtasksByEpic(epicIdForDeleteSubtask)) {
            System.out.println(t);
        }

        //Удаление всех сабтасков по эпику
        System.out.println("\n");
        System.out.println("//Удаление всех тасков по эпику");

        int epicIdForDelete = taskManager.getAllEpics().stream().findAny().get().getId();
        taskManager.deleteEpicById(epicIdForDelete);
        System.out.println("Epic with ID: " + epicIdForDelete + " and all subtasks have DELETED");

        if (taskManager.getAllEpics().isEmpty()) {
            System.out.println("No epic found");
        }
    }
}

