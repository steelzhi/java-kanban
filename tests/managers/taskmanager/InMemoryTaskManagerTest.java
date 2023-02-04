package managers.taskmanager;

import managers.taskmanager.memorymanager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setManager() {
        manager = new InMemoryTaskManager();
    }
}