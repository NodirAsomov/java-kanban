
import managerapp.TaskManager;
import http.HttpTaskServer;
import managerapp.Managers;


public class Main {
    public static void main(String[] args) {
        try {
            TaskManager manager = Managers.getDefault();
            HttpTaskServer server = new HttpTaskServer(manager);
            server.start();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
}
