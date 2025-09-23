package managerapp;

import taskapp.Epic;
import taskapp.SubTask;
import taskapp.Task;

import java.util.*;



public interface TaskManager {
    // добавить таск
    //abstract int getNextID();

     Task addTask(Task task);


     Epic addEpic(Epic epic);



     SubTask addSubtask(SubTask subtask);




     Task updateTask(Task task);


     Epic updateEpic(Epic epic);



     SubTask updateSubtask(SubTask subtask);





     Task getTaskByID(int id);

     Epic getEpicByID(int id);

     SubTask getSubtaskByID(int id);

     List<Task> getTasks();

     List<Epic> getEpics();

     List<SubTask> getSubtasks();

     List<SubTask> getEpicSubtasks(int epicId);

     void deleteTasks();

     void deleteEpics();

     void deleteSubtasks();

     void deleteTaskByID(int id);

     void deleteEpicByID(int id);

     void deleteSubtaskByID(int id);

     List<Task> getHistory();




    //abstract void updateEpicStatus(Epic epic);




}
