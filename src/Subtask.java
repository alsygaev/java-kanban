import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class Subtask implements Taskable {
    private String name;
    private String description;
    private TaskStatus status;
    private Epic epic;

    public Subtask(String name, String description, TaskStatus status, Epic epic) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(name, subtask.name) && Objects.equals(description, subtask.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}

/*

public class Subtask implements Taskable {
    private int id;
    private String name; //Name of the task
    private String description; // description of the task
    private TaskStatus status; // Status of the task

    private static int idSubtaskCounter = 1;  // Счетчик для Subtask
    private int epicId;

    List<Task> tasks = new ArrayList<Task>();

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        //super(name, description, status);
        this.name = name;
        this.description = description;
        this.status = status;

        this.id = idSubtaskCounter++;
        this.epicId = epicId;

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public TaskStatus getStatus() {
        return null;
    }

    @Override
    public void setStatus(TaskStatus status) {

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
*/
