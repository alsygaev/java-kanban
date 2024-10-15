import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Epic implements Taskable {
    private String name;
    private String description;
    private TaskStatus status;

    public Epic(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
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
        Epic epic = (Epic) o;
        return Objects.equals(name, epic.name) && Objects.equals(description, epic.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}

/*

public class Epic implements Taskable {

    List<Subtask> subtasks = new ArrayList<Subtask>();
    private int id;
    private String name; //Name of the task
    private String description; // description of the task
    private TaskStatus status; // Status of the task

    private static int idEpicCounter = 1;  // Счетчик для Epic

    public Epic(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;

        this.id = idEpicCounter++;

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
        return status;
    }

    @Override
    public void setStatus(TaskStatus status) {

    }


    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", id=" + id +
                '}';
    }
}
*/
