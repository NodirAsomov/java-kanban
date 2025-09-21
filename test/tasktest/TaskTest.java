package tasktest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import taskapp.Epic;
import taskapp.Status;

public class TaskTest {

    @Test
    public void epicIsShouldBeEqual() {
        Epic epic1 = new Epic(1,"reading","writing");
        Epic epic2 = new Epic( "home", "work");
        assertEquals(epic1, epic2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }


}
