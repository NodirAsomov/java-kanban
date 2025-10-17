package managertest;

import managerapp.HistoryManager;
import managerapp.Managers;
import managerapp.TaskManager;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskapp.Status;
import taskapp.Task;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        System.out.println("История должна игнорировать null задачи");
    }

    @Test
    void add() {
        Task task = new Task(1, "name", "newname", Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история должна содержать одну задачу.");
    }

    @Test
    void removeTaskFromHistory() {
        Task task = new Task(2, "name2", "newname2", Status.IN_PROGRESS);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());


        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "Задача должна быть удалена из истории.");
    }

    @Test
    void addMultipleTasksAndCheckOrder() {
        historyManager.add(new Task(3, "name3", "newname3", Status.DONE));
        historyManager.add(new Task(4, "name4", "newname4", Status.NEW));
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи.");

    }


}
