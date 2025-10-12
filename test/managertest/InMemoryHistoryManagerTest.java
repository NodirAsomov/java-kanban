package managertest;

import managerapp.HistoryManager;
import managerapp.Managers;
import managerapp.TaskManager;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskapp.Status;
import taskapp.Task;

import java.util.*;

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
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
        System.out.println("история дожна игнорировать null задачи");
    }

    @Test
    void add() {
        historyManager.add(new Task(1, "name", "newname", Status.NEW));
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    }


}
