package managerapp;

import taskapp.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subtasks = new HashMap<>();

    private final HistoryManager history = Managers.getDefaultHistory();

    private int nextID = 1;

    // TreeSet для приоритизации по startTime
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );


    protected int getNextID() {
        return nextID++;
    }

    public void updateNextId() {
        int maxId = 0;
        for (Task t : tasks.values()) maxId = Math.max(maxId, t.getId());
        for (Epic e : epics.values()) maxId = Math.max(maxId, e.getId());
        for (SubTask s : subtasks.values()) maxId = Math.max(maxId, s.getId());
        nextID = maxId + 1;
    }

    protected void updatePrioritized(Task task) {
        prioritizedTasks.remove(task);
        if (!(task instanceof Epic)) {
            prioritizedTasks.add(task);
        }
    }


    @Override
    public Task addTask(Task task) {
        task.setId(getNextID());
        tasks.put(task.getId(), task);
        updatePrioritized(task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId()))
            throw new IllegalArgumentException("Task с id " + task.getId() + " не существует");
        tasks.put(task.getId(), task);
        updatePrioritized(task);
        return task;
    }

    @Override
    public Task getTaskByID(int id) {
        Task t = tasks.get(id);
        if (t != null) history.add(t);
        return t;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
    }

    @Override
    public void deleteTaskByID(int id) {
        Task t = tasks.remove(id);
        if (t != null) prioritizedTasks.remove(t);
        history.remove(id);
    }


    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getNextID());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId()))
            throw new IllegalArgumentException("Epic с id " + epic.getId() + " не существует");

        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        oldEpic.updateTimeData();
        return oldEpic;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic e = epics.get(id);
        if (e != null) history.add(e);
        return e;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(t -> t instanceof SubTask);
    }

    @Override
    public void deleteEpicByID(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (SubTask s : epic.getSubtaskList()) {
                subtasks.remove(s.getId());
                prioritizedTasks.remove(s);
            }
        }
    }


    @Override
    public SubTask addSubtask(SubTask subtask) {
        Epic epic = epics.get(subtask.getEpicID());
        if (epic == null)
            throw new IllegalArgumentException("Epic с id " + subtask.getEpicID() + " не существует");

        subtask.setId(getNextID());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask);
        epic.updateTimeData();
        updatePrioritized(subtask);
        return subtask;
    }

    @Override
    public SubTask updateSubtask(SubTask subtask) {
        SubTask old = subtasks.get(subtask.getId());
        if (old == null)
            throw new IllegalArgumentException("SubTask с id " + subtask.getId() + " не существует");
        if (old.getEpicID() != subtask.getEpicID())
            throw new IllegalArgumentException("Нельзя менять epicID у подзадачи");

        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicID());
        epic.removeSubtask(old);
        epic.addSubtask(subtask);
        epic.updateTimeData();

        updatePrioritized(subtask);
        return subtask;
    }

    @Override
    public SubTask getSubtaskByID(int id) {
        SubTask s = subtasks.get(id);
        if (s != null) history.add(s);
        return s;
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();
        return epic.getSubtaskList();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();
        epics.values().forEach(Epic::updateTimeData);
    }

    @Override
    public void deleteSubtaskByID(int id) {
        SubTask s = subtasks.remove(id);
        if (s != null) {
            prioritizedTasks.remove(s);
            Epic epic = epics.get(s.getEpicID());
            if (epic != null) {
                epic.removeSubtask(s);
                epic.updateTimeData();
            }
        }
    }


    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public void clearHistory() {
        history.getHistory().clear();
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) deleteTaskByID(id);
        else if (subtasks.containsKey(id)) deleteSubtaskByID(id);
        else if (epics.containsKey(id)) deleteEpicByID(id);
    }


    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    public void putTaskFromFile(Task task) {
        tasks.put(task.getId(), task);
        updatePrioritized(task);
    }

    public void putEpicFromFile(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void putSubtaskFromFile(SubTask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updatePrioritized(subtask);
    }

    public void restoreEpicSubtasks() {
        for (SubTask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.getEpicID());
            if (epic != null) {
                epic.addSubtask(subtask);
                epic.updateTimeData();
            }
        }
    }
}

