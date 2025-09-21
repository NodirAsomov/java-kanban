package tasktest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import taskapp.Epic;
import taskapp.Status;
import taskapp.Task;

public class EpicTest {



    @Test
    public void tasksIsShouldBeEqual() {
        Task task1 = new Task(1, "1 таск","Описание 1 таска", Status.NEW);
        Task task2 = new Task(1, "2 таск","Описание 2 таска", Status.IN_PROGRESS);
        assertEquals(task1, task2,"Ошибка! Экземпляры класса Task должны быть равны друг другу, если равен их id");
    }

    }
