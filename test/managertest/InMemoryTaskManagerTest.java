package managertest;

import managerapp.Managers;

import managerapp.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import taskapp.Status;
import taskapp.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


public class InMemoryTaskManagerTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask() {
        final Task task = taskManager.addTask(new Task("homework", "writing", Status.NEW));
        final Task savedTask = taskManager.getTaskByID(task.getId());
        assertNotNull(savedTask, "Ошибка! Задача не найдена.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Ошибка! Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Ошибка! Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Ошибка! Задачи не совпадают.");


    }
}
