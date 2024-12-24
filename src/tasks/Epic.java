package tasks;

import managers.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    // Расчёт продолжительности эпика на основе подзадач
    public Duration getDuration(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    // Расчёт времени начала эпика на основе подзадач
    public LocalDateTime getStartTime(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    // Расчёт времени завершения эпика на основе подзадач
    public LocalDateTime getEndTime(List<Subtask> subtasks) {
        return subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

}
