package de.ur.mi.android.demos.todo.room;
import android.content.Context;
import androidx.room.Room;
import java.util.ArrayList;
import de.ur.mi.android.demos.todo.tasks.Task;

// Helper Klasse, die den Zugriff auf die Datenbank bündelt
public class TaskDatabaseHelper {

    private static final String DATABASE_NAME = "tasks-db";
    private final Context context;
    private TaskDatabase db;

    public TaskDatabaseHelper(Context context) {
        this.context = context;
        initDatabase();
    }

    private void initDatabase() {
        // Erstellen der Datenbank; benötigt werden Kontext, Klasse der Datenbank, die man erstellen will und der Name der Datenbank
        db = Room.databaseBuilder(context, TaskDatabase.class, DATABASE_NAME)
                // ermöglicht das Ausführen von Datenbankabfragen im MainThread (wird nicht empfohlen!)
                .allowMainThreadQueries()
                .build();
    }

    // einzelnen Task zur Datenbank hinzufügen
    public void addTask(Task task) {
        db.taskDao().insertTask(task);
    }

    // bestehenden Task in der Datenbank updaten
    public void updateTask(Task task) {
        db.taskDao().updateTask(task);
    }

    // alle in der Datenbank existierenden Tasks holen
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<Task>(db.taskDao().getAllTasks());
    }
}
