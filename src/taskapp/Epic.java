package taskapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<SubTask> subtaskList = new ArrayList<>();

    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.type = Type.EPIC;
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);
        this.type = Type.EPIC;
    }


    public void addSubtask(SubTask subtask) {
        subtaskList.add(subtask);
        updateTimeData();
    }

    public void removeSubtask(SubTask subtask) {
        subtaskList.remove(subtask);
        updateTimeData();
    }

    public void clearSubtasks() {
        subtaskList.clear();
        updateTimeData();
    }

    public List<SubTask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }


    public void updateTimeData() {
        if (subtaskList.isEmpty()) {
            duration = 0;
            startTime = null;
            endTime = null;
            return;
        }

        duration = subtaskList.stream().mapToInt(SubTask::getDuration).sum();

        startTime = subtaskList.stream()
                .map(SubTask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        endTime = subtaskList.stream()
                .map(SubTask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtaskCount=" + subtaskList.size() +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}


