package tasktest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import taskapp.Epic;
import taskapp.Status;
import taskapp.SubTask;




public class SubTaskTest {


    @Test
    public void subtaskIsShouldBeEqual() {
        Epic epic1 = new Epic("Эпик 1", "first epic view");
        SubTask subtask1 = new SubTask(1, "subtask","view", Status.NEW,epic1.getId());
        SubTask subtask2 = new SubTask(1, "subtask","view", Status.DONE,epic1.getId());
        assertEquals(subtask1, subtask2,"Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id");
    }

}
