import java.util.*;


public class TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

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
                    "Epic с id " + subtask.getEpicID() + " не существует"
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
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        int epicID = epic.getId();
        if (!epics.containsKey(epicID)) {
            return null;
        }

        Epic newEpic = epics.get(epicID);

        newEpic.setName(epic.getName());
        newEpic.setDescription(epic.getDescription());

        updateEpicStatus(newEpic);
        return newEpic;
    }

    public SubTask updateSubtask(SubTask subtask) {
        int subtaskID = subtask.getId();
        if (!subtasks.containsKey(subtaskID)) {
            return null;
        }

        SubTask newSubtask = subtasks.get(subtaskID);

        if (newSubtask.getEpicID() != subtask.getEpicID()) {
            return null;
        }

        newSubtask.setName(subtask.getName());
        newSubtask.setDescription(subtask.getDescription());
        newSubtask.setStatus(subtask.getStatus());

        Epic epic = epics.get(newSubtask.getEpicID());
        updateEpicStatus(epic);

        return newSubtask;
    }


    public Task getTaskByID(int id) {
        return tasks.get(id);
    }

    public Epic getEpicByID(int id) {
        return epics.get(id);
    }

    public SubTask getSubtaskByID(int id) {
        return subtasks.get(id);
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
}