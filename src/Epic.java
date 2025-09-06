import java.util.ArrayList;

public  class Epic extends  Task{

    private ArrayList<SubTask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
    }
    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);
    }

    public void addSubtask(SubTask subtask) {
        subtaskList.add(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public ArrayList<SubTask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    public void removeSubtask(SubTask subtask) {
        subtaskList.remove(subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtaskList.size = " + subtaskList.size() +
                ", status = " + getStatus() +
                '}';
    }
}