package managertest;

import managerapp.HistoryManager;
import managerapp.Managers;
import managerapp.TaskManager;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskapp.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private static TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addHistoryShouldIgnoreNullTask() {
        historyManager.add(null);
        ArrayList<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }
}
