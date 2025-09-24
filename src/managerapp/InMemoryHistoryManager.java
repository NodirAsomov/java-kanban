package managerapp;

import taskapp.Task;
import java.util.*;

public class InMemoryHistoryManager implements  HistoryManager{

    private static final int MAX_HISTORY_STORAGE = 10;
    private final ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
            if (historyList.size() == MAX_HISTORY_STORAGE) {
                historyList.remove(0);
            }
            historyList.add(task);
        }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyList);
    }

}
