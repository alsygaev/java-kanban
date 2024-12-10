package tasks;

public class Epic extends Task {
    public TaskType taskType = TaskType.EPIC;

    public Epic() {
        this.status = TaskStatus.NEW;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public String toString() {
        return "Tasks.Epic{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}
