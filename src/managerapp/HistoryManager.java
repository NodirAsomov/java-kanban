package managerapp;

import taskapp.Epic;
import taskapp.SubTask;
import taskapp.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory();
}
