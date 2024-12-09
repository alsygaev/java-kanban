public class Managers {

    private Managers() {
    }

    // Универсальный метод для создания различных реализаций TaskManager
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}