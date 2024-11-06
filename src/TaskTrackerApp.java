import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTrackerApp {
    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем Task
        Task task1 = new Task("this is Task", "Buy milk");
        Task task2 = new Task("this is Task", "Buy bread");
        Task task3 = new Task("this is Task", "Buy vegetables");

        // Добавляем таски в Tasks
        System.out.println("\n");
        System.out.println("// Добавляем задачу в Tasks");

        int task1Id = taskManager.createTask(task1);
        System.out.println("Task with ID: " + task1.getId() + " / name: " + task1.getName() + " / description: " + task1.getDescription() +
                " / " + task1.getStatus() + " was created.");

        int task2Id = taskManager.createTask(task2);
        System.out.println("Task with ID: " + task2.getId() + " / name: " + task2.getName() + " / description: " + task2.getDescription() +
                " / " + task2.getStatus() + " was created.");

        int task3Id = taskManager.createTask(task3);
        System.out.println("Task with ID: " + task3.getId() + " / name: " + task3.getName() + " / description: " + task3.getDescription() +
                " / " + task3.getStatus() + " was created.");

        //Просмотр всех Тасков из Tasks
        System.out.println("\n");
        System.out.println("//Просмотр всех Тасков из Tasks");

        taskManager.getAllTasks().stream().forEach(System.out::println);

        //Создание Epic и Subtask
        Epic epic = new Epic("This is Epic", "Курс Java");

        int epicId = taskManager.createEpic(epic);
        System.out.println("\n");
        System.out.println("Created new epic: " + epicId + " /name: " + epic.getName() + " / description: " + epic.getDescription() +
                " / " + epic.getStatus() + " was created.");

        Subtask subtask1 = new Subtask("Check subtask with Epic", "Subtask_1", epicId );
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Check subtask with Epic", "Subtask_2", epicId );
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Check subtask with Epic", "Subtask_3", TaskStatus.IN_PROGRESS, epicId );
        taskManager.createSubtask(subtask3);

        //Просмотр всех Subtask из Epic
        System.out.println("\n");
        System.out.println("//Просмотр всех Subtask из Epics");

        System.out.println("Epics:");
        for (Task e : taskManager.getAllEpics()) {
            System.out.println(e);
            for (Task t : taskManager.getAllSubtasksByEpic(e.getId())) {
                System.out.println("--> " + t);
            }
        }

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
        System.out.println(taskManager.getEpicById(id));

        //Обновление таска
        System.out.println("\n");
        System.out.println("//Обновление статуса таска NEW -> IN_PROGRESS -> DONE");

        int taskId = taskManager.getAllTasks().stream().findAny().get().getId();
        Task task = taskManager.getTaskById(taskId);
        task.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        System.out.println("CHANGE STATUS: NEW -> IN_PROGRESS");
        System.out.println("Tasks:");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);
        System.out.println("CHANGE STATUS: IN_PROGRESS -> DONE");
        System.out.println("Tasks:");
        for (Task t : taskManager.getAllTasks()) {
            System.out.println(t);
        }

        //Обновление статуса сабтаска
        System.out.println("\n");
        System.out.println("//Обновление статуса subtask и epic IN_PROGRESS -> NEW");

        int epicIdForUpdate = taskManager.getAllEpics().stream().findAny().get().getId();

        Subtask subtaskForUpdate1 = taskManager.getSubtaskById(5);
        subtaskForUpdate1.setStatus(TaskStatus.NEW);
        Subtask subtaskForUpdate2 = taskManager.getSubtaskById(7);
        subtaskForUpdate2.setStatus(TaskStatus.NEW);

        taskManager.updateSubtask(subtaskForUpdate1);
        taskManager.updateSubtask(subtaskForUpdate2);

        System.out.println("Subtasks:");
        for (Task t : taskManager.getAllSubtasksByEpic(epicIdForUpdate)) {
            System.out.println(t);
        }

        System.out.println("Epic with ID: " + epicIdForUpdate + " has NEW STATUS: " +
                taskManager.getEpicById(epicIdForUpdate).getStatus());


        //Обновление наименования/описания сабтаска
        System.out.println("\n");
        System.out.println("//Обновление наименования/описания сабтаска");

        int epicIdForUpdateNameSubtask = taskManager.getAllEpics().stream().findAny().get().getId();
        int subtaskIdForUpdateName = taskManager.getAllSubtasksByEpic(epicIdForUpdateNameSubtask).stream().findAny().get().getId();

        Subtask subtaskForUpdateName = taskManager.getSubtaskById(7);
        subtaskForUpdateName.setName("Измененное наименование сабтаска");
        subtaskForUpdateName.setDescription("Измененное описание сабтаска");

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

        int epicIdForDone = 4;

        List<Subtask> subtaskIdForDone = taskManager.getAllSubtasksByEpic(epicIdForDone);

        for (Subtask s : subtaskIdForDone) {
            s.setStatus(TaskStatus.DONE);
            taskManager.updateSubtask(s);
        }

        System.out.println("Epic with ID: " + epicIdForUpdate + " has NEW STATUS: " +
                taskManager.getEpicById(epicIdForUpdate).getStatus());

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
        taskManager.deleteAllSubtasksByEpic(epicIdForDelete);
        System.out.println("All subtasks have DELETED by Epic with ID: " + epicIdForDelete);

        if (taskManager.getAllEpics().isEmpty()) {
            System.out.println("No epic found");
        }

        //Удаление всех Epic
        System.out.println("\n");
        System.out.println("//Удаление всех Epic");

        taskManager.deleteAllEpics();
        System.out.println("All epics are DELETED");

        //Get tasks history
        System.out.println("\n");
        System.out.println("//Получение истории");
        taskManager.getHistory().stream().forEach(t -> System.out.println(t.getName() + " / " +
                t.getDescription() + " / " + t.getStatus()));

        System.out.println("\n");
        System.out.println("//Получение данных by printAllTasks");

        printAllTasks(taskManager);


    }

    private static void printAllTasks(TaskManager manager) {

        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtasksByEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

