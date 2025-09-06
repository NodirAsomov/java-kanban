public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task goShopping = new Task("Покупка в магазине", "купить томаты для соуса");
        Task goShoppingTask = taskManager.addTask(goShopping);
        System.out.println(goShoppingTask);

        Task goShoppingToUpdate = new Task(goShopping.getId(), "надо сегодня сделать покупку", "купить макароны",
                Status.IN_PROGRESS);
        Task goShoppingToUpdateTask = taskManager.updateTask(goShoppingToUpdate);
        System.out.println(goShoppingToUpdateTask);


        Epic bigTask = new Epic("купить учебники ", "Нужно сделать до 1 мая ");
        taskManager.addEpic(bigTask);
        System.out.println(bigTask);
        SubTask subtask1 = new SubTask("написать реферат", "Нужно сдать завтра ",
                bigTask.getId());
        SubTask subtask2 = new SubTask("прочитать все темы ", "завтра опрос",
                bigTask.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        System.out.println(bigTask);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        System.out.println(bigTask) ;
    }
}