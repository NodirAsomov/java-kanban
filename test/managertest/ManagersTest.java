package managertest;


import managerapp.InMemoryHistoryManager;
import managerapp.InMemoryTaskManager;
import managerapp.Managers;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }
}
