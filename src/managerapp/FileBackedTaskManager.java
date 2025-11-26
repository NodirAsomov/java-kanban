package managerapp;

import taskapp.Epic;
import taskapp.Status;
import taskapp.SubTask;
import taskapp.Task;


import java.io.*;

import static taskapp.Type.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask addSubtask(SubTask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }


    public SubTask updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }


    public void deleteTasks() {
        super.deleteTasks();
        save();
    }


    public void deleteEpics() {
        super.deleteEpics();
        save();
    }


    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    public void deleteTask(int taskId) {
        super.deleteTaskByID(taskId);
        save();
    }


    public void deleteEpicById(Integer epicId) {
        super.deleteEpicByID(epicId);
        save();
    }


    public void deleteSubtask(Integer subTaskId) {
        super.deleteSubtaskByID(subTaskId);
        save();
    }


    public void deleteSubtask(SubTask subtask) {
        super.deleteSubtasks();
        save();
    }


    public void updateEpicStatus(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.write("\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
                writer.write("\n");
            }
            for (SubTask subtask : getSubtasks()) {
                writer.write(toString(subtask));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач в файл", e);
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%d",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task instanceof SubTask ? ((SubTask) task).getEpicID() : 0
        );
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {


            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                Task task = fromString(line);

                switch (task.getType()) {
                    case TASK:
                        manager.putTaskFromFile(task);
                        break;

                    case EPIC:
                        manager.putEpicFromFile((Epic) task);
                        break;

                    case SUBTASK:
                        manager.putSubtaskFromFile((SubTask) task);
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Неизвестный тип задачи: " + task.getType()
                        );
                }
            }
            manager.restoreEpicSubtasks();
            manager.updateNextId();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки задачи из файла", e);
        }
        return manager;
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Некорректная строка задачи: " + value);
        }

        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length > 5 ? Integer.parseInt(parts[5]) : 0;

        switch (type) {
            case "TASK":
                return new Task(id, name, description, status);
            case "EPIC":
                return new Epic(id, name, description);
            case "SUBTASK":
                return new SubTask(id, name, description, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

}
