package managerapp;

import taskapp.Epic;

import taskapp.Status;
import taskapp.SubTask;
import taskapp.Task;

import java.util.ArrayList;



public interface TaskManager {
    // добавить таск
    abstract int getNextID();

    abstract Task addTask(Task task);


    abstract Epic addEpic(Epic epic);



    abstract SubTask addSubtask(SubTask subtask);




    abstract Task updateTask(Task task);


    abstract Epic updateEpic(Epic epic);



    abstract SubTask updateSubtask(SubTask subtask);





    abstract Task getTaskByID(int id);

    abstract Epic getEpicByID(int id);

    abstract SubTask getSubtaskByID(int id);

    abstract ArrayList<Task> getTasks();

    abstract ArrayList<Epic> getEpics();

    abstract ArrayList<SubTask> getSubtasks();

    abstract ArrayList<SubTask> getEpicSubtasks(int epicId);

    abstract void deleteTasks();

    abstract void deleteEpics();

    abstract void deleteSubtasks();

    abstract void deleteTaskByID(int id);

    abstract void deleteEpicByID(int id);

    abstract void deleteSubtaskByID(int id);




    abstract void updateEpicStatus(Epic epic);




}
