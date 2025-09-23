package managerapp;




import taskapp.Task;
import java.util.ArrayList;


public class InMemoryHistoryManager implements  HistoryManager{
    private static final int MAX_HISTORY_STORAGE = 10;
    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        } else {
        if (historyList.size() == MAX_HISTORY_STORAGE) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }}

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList);
    }
}
