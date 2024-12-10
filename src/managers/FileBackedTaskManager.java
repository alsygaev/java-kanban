package managers;

import tasks.*;
import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }


    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasksByEpic(int epicId) {
        super.deleteAllSubtasksByEpic(epicId);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }


    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
            }

            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask));
            }

            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
            }

        } catch (ManagerSaveException | IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл: ", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        int maxId;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            maxId = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("id,type,name,status,description,epic")) {
                    Task task = fromString(line);
                    maxId = Math.max(maxId, task.getId());

                    switch (task.getType()) {
                        case TaskType.TASK:
                            manager.tasks.put(task.getId(), task);
                            break;
                        case TaskType.EPIC:
                            manager.epics.put(task.getId(), (Epic) task);
                            break;
                        case TaskType.SUBTASK:
                            manager.subtasks.put(task.getId(), (Subtask) task);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла: ", e);
        }
        manager.generatorId = maxId + 1;
        return manager; // Возвращаем восстановленный менеджер
    }

    public String toString(Task task) {
        String id;
        String type;
        String name;
        String status;
        String description;
        String epic = "";
        id = String.valueOf(task.getId());

        if (task.getType().equals(TaskType.EPIC)) {
            type = String.valueOf(TaskType.EPIC);
        } else if (task.getType().equals(TaskType.SUBTASK)) {
            type = String.valueOf(TaskType.SUBTASK);
        } else {
            type = String.valueOf(TaskType.TASK);
        }
        name = task.getName();

        status = task.getStatus().toString();

        description = task.getDescription();

        if (task.getType().equals(TaskType.SUBTASK)) {
            epic = String.valueOf(((Subtask) task).getEpicId());
        }

        return id + "," + type + "," + name + "," + status + "," + description + "," + epic;
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",");
        Task task;
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        if (TaskType.EPIC.equals(type)) {
            task = new Epic(name, description);
        } else if (TaskType.SUBTASK.equals(type)) {
            int epicId = Integer.parseInt(fields[5]);
            task = new Subtask(name, description, epicId);
        } else {
            task = new Task(name, description);
        }

        task.setId(id);
        task.setStatus(status);

        return task;
    }

}
