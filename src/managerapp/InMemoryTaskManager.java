package managerapp;
import taskapp.Epic;
import taskapp.Status;
import taskapp.SubTask;
import taskapp.Task;
import java.util.*;



public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

    private final HistoryManager history = Managers.getDefaultHistory();
    private int nextID = 1;

    private int getNextID() {
        return nextID++;
    }

    public Task addTask(Task task) {
        task.setId(getNextID());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpic(Epic epic) {
        epic.setId(getNextID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask addSubtask(SubTask subtask) {
        Epic epic = epics.get(subtask.getEpicID());

        if (epic == null) {
            throw new IllegalArgumentException(
                    "taskapp.Epic с id " + subtask.getEpicID() + " не существует"
            );
        }

        subtask.setId(getNextID());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (!tasks.containsKey(taskID)) {
            throw new IllegalArgumentException("taskapp.Task с id " + taskID + " не существует");
        }
        tasks.replace(taskID, task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        int epicID = epic.getId();
        if (!epics.containsKey(epicID)) {
            throw new IllegalArgumentException("taskapp.Epic с id " + epicID + " не существует");
        }

        Epic newEpic = epics.get(epicID);

        newEpic.setName(epic.getName());
        newEpic.setDescription(epic.getDescription());

        return newEpic;
    }

    public SubTask updateSubtask(SubTask subtask) {
        int subtaskID = subtask.getId();
        if (!subtasks.containsKey(subtaskID)) {
            throw new IllegalArgumentException("taskapp.SubTask с id " + subtaskID + " не существует");
        }

        SubTask oldSubtask = subtasks.get(subtaskID);

        if (oldSubtask.getEpicID() != subtask.getEpicID()) {
            throw new IllegalArgumentException("Нельзя менять epicID у подзадачи");
        }

        subtasks.put(subtaskID, subtask);

        Epic epic = epics.get(subtask.getEpicID());
        epic.removeSubtask(oldSubtask);
        epic.addSubtask(subtask);

        updateEpicStatus(epic);

        return subtask;
    }

    public Task getTaskByID(int id) {

        Task task = tasks.get(id);
        if (task != null) {
            history.add(task);
        }
        return task;
    }

    public Epic getEpicByID(int id) {

        Epic epic = epics.get(id);
        if (epic != null) {
            history.add(epic);
        }
        return epic;


    }

    public SubTask getSubtaskByID(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            history.add(subtask);
        }
        return subtask;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<SubTask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubtaskList();
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic);
        }
    }

    public void deleteTaskByID(int id) {
        tasks.remove(id);
    }

    public void deleteEpicByID(int id) {
        ArrayList<SubTask> epicSubtasks = epics.get(id).getSubtaskList();
        for (SubTask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtaskByID(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicID = subtask.getEpicID();
            subtasks.remove(id);

            Epic epic = epics.get(epicID);
            if (epic != null) {
                epic.removeSubtask(subtask);
                updateEpicStatus(epic);
            }
        }
    }


    private void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> list = epic.getSubtaskList();

        if (list.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int allIsDoneCount = 0;
        int allIsInNewCount = 0;

        for (SubTask subtask : list) {
            if (subtask.getStatus() == Status.DONE) {
                allIsDoneCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allIsInNewCount++;
            }
        }

        if (allIsDoneCount == list.size()) {
            epic.setStatus(Status.DONE);
        } else if (allIsInNewCount == list.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }
}