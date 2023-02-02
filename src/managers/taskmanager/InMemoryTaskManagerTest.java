package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    InMemoryTaskManager manager = new InMemoryTaskManager();;

    @BeforeEach
    public void setManager() {
        super.setManager(manager);
    }

    @Test
    public void getTasksCaseStandard() {
        super.getTasksCaseStandard();
    }

    @Test
    public void getTasksCaseEmptyList() {
        super.getTasksCaseEmptyList();
    }

    @Test
    public void removeAllTasksCaseStandard() {
        super.removeAllTasksCaseStandard();
    }

    @Test
    public void removeAllTasksCaseEmptyList() {
        super.removeAllTasksCaseEmptyList();
    }

    @Test
    public void findTaskByIdCaseStandard() {
        super.findTaskByIdCaseStandard();
    }

    @Test
    public void findTaskByIdCaseEmptyList() {
        super.findTaskByIdCaseEmptyList();
    }

    @Test
    public void findTaskByIdCaseIncorrectData() {
        super.findTaskByIdCaseIncorrectData();
    }

    @Test
    public void addTaskCaseStandard() {
        super.addTaskCaseStandard();
    }

    @Test
    public void updateTaskCaseStandard() {
        super.updateTaskCaseStandard();
    }

    @Test
    public void updateTaskCaseEmptyList() {
        super.updateTaskCaseEmptyList();
    }

    @Test
    public void updateTaskCaseIncorrectData() {
        super.updateTaskCaseIncorrectData();
    }

    @Test
    public void removeTaskCaseStandard() {
        super.removeTaskCaseStandard();
    }

    @Test
    public void removeTaskCaseEmptyList() {
        super.removeTaskCaseEmptyList();
    }

    @Test
    public void removeTaskCaseIncorrectData() {
        super.removeTaskCaseIncorrectData();
    }

    @Test
    public void getTaskCaseStandard() {
        super.getTaskCaseStandard();
    }

    @Test
    public void getTaskCaseEmptyList() {
        super.getTaskCaseEmptyList();
    }

    @Test
    public void getTaskCaseIncorrectData() {
        super.getTaskCaseIncorrectData();
    }

    @Test
    public void getEpicsCaseStandard() {
        super.getEpicsCaseStandard();
    }

    @Test
    public void getEpicsCaseEmptyList() {
        super.getEpicsCaseEmptyList();
    }

    @Test
    public void removeAllEpicsCaseStandard() {
        super.removeAllEpicsCaseStandard();
    }

    @Test
    public void removeAllEpicsCaseEmptyList() {
        super.removeAllEpicsCaseEmptyList();
    }


    @Test
    public void findEpicByIdCaseStandard() {
        super.findEpicByIdCaseStandard();
    }

    @Test
    public void findEpicByIdCaseEmptyList() {
        super.findEpicByIdCaseEmptyList();
    }

    @Test
    public void findEpicByIdCaseIncorrectData() {
        super.findEpicByIdCaseIncorrectData();
    }

    @Test
    public void addEpicCaseStandard() {
        super.addEpicCaseStandard();
    }

    @Test
    public void addEpicCaseEmptyList() {
        super.addEpicCaseEmptyList();
    }


    @Test
    public void updateEpicCaseStandard() {
        super.updateEpicCaseStandard();
    }

    @Test
    public void updateEpicCaseEmptyList() {
        super.updateEpicCaseEmptyList();
    }

    @Test
    public void updateEpicCaseIncorrectData() {
        super.updateEpicCaseIncorrectData();
    }

    @Test
    public void removeEpicCaseStandard() {
        super.removeEpicCaseStandard();
    }

    @Test
    public void removeEpicCaseEmptyList() {
        super.removeEpicCaseEmptyList();
    }

    @Test
    public void getSubTasksInEpicCaseStandard() {
        super.getSubTasksInEpicCaseStandard();
    }

    @Test
    public void getSubTasksInEpicCaseEmptyList() {
        super.getSubTasksInEpicCaseEmptyList();
    }

    @Test
    public void getEpicCaseStandard() {
        super.getEpicCaseStandard();
    }

    @Test
    public void getEpicCaseEmptyList() {
        super.getEpicCaseEmptyList();
    }


    @Test
    public void countEpicStatusCaseStandard() {
        super.countEpicStatusCaseStandard();
    }

    @Test
    public void countEpicStatusCaseEmptyList() {
        super.countEpicStatusCaseEmptyList();
    }

    @Test
    public void getAllSubTasksCaseStandard() {
        super.getAllSubTasksCaseStandard();
    }

    @Test
    public void getAllSubTasksCaseEmptyList() {
        super.getAllSubTasksCaseEmptyList();
    }


    @Test
    public void removeAllSubTasksCaseStandard() {
        super.removeAllSubTasksCaseStandard();
    }

    @Test
    public void removeAllSubTasksCaseEmptyList() {
        super.removeAllSubTasksCaseEmptyList();
    }


    @Test
    public void findSubTaskByIdCaseStandard() {
        super.findSubTaskByIdCaseStandard();
    }

    @Test
    public void findSubTaskByIdCaseEmptyList() {
        super.findSubTaskByIdCaseEmptyList();
    }

    @Test
    public void findSubTaskByIdCaseIncorrectData() {
        super.findSubTaskByIdCaseIncorrectData();
    }

    @Test
    public void addSubTaskCaseStandard() {
        super.addSubTaskCaseStandard();
    }


    @Test
    public void addSubTaskCaseIncorrectData() {
        super.addSubTaskCaseIncorrectData();
    }

    @Test
    public void updateSubTaskCaseStandard() {
        super.updateSubTaskCaseStandard();
    }

    @Test
    public void updateSubTaskCaseEmptyList() {
        super.updateSubTaskCaseEmptyList();
    }

    @Test
    public void updateSubTaskCaseIncorrectData() {
        super.updateSubTaskCaseIncorrectData();
    }

    @Test
    public void removeSubTaskCaseStandard() {
        super.removeSubTaskCaseStandard();
    }

    @Test
    public void removeSubTaskCaseEmptyList() {
        super.removeSubTaskCaseEmptyList();
    }

    @Test
    public void removeSubTaskCaseIncorrectData() {
        super.removeSubTaskCaseIncorrectData();
    }

    @Test
    public void doesEpicExistForSubTaskCaseStandard() {
        super.doesEpicExistForSubTaskCaseStandard();
    }

    @Test
    public void doesEpicExistForSubTaskCaseEmptyList() {
        super.doesEpicExistForSubTaskCaseEmptyList();
    }

    @Test
    public void doesEpicExistForSubTaskCaseIncorrectData() {
        super.doesEpicExistForSubTaskCaseIncorrectData();
    }

    @Test
    public void getHistoryCaseStandard() {
        super.getHistoryCaseStandard();
    }

    @Test
    public void getHistoryCaseEmptyList() {
        super.getHistoryCaseEmptyList();
    }

    @Test
    public void getHistoryCaseIncorrectData() {
        super.getHistoryCaseIncorrectData();
    }

    @Test
    public void getSubTaskCaseStandard() {
        super.getSubTaskCaseStandard();
    }

    @Test
    public void getSubTaskCaseEmptyList() {
        super.getSubTaskCaseEmptyList();
    }

    @Test
    public void getSubTaskCaseIncorrectData() {
        super.getSubTaskCaseIncorrectData();
    }
}