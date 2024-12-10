package Managers;

public class Managers {

    private Managers() {
    }

    // Универсальный метод для создания различных реализаций Managers.TaskManager
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}