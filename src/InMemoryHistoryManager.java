import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (tasksHistory.size() == 10) {
            tasksHistory.remove(0);
        } else {
            tasksHistory.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(tasksHistory);
    }
}
