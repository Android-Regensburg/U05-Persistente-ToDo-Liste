package de.ur.mi.android.demos.todo.tasks;
import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import de.ur.mi.android.demos.todo.room.TaskDatabaseHelper;

/**
 * Instanzen dieser Klasse verwalten eine Liste von Aufgaben. Über öffentliche Methoden können neue
 * Aufgaben hinzugefügt werden und der Status existierender Aufgaben verändert werden. Diese Änderungen
 * werden über eine einfache Listener-Schnittstelle an einen im Konstruktor gesetzten Observer übergeben.
 */
public class TaskManager {

    // Observer/Listener, der über Änderungen an der Aufgabenliste informiert werden soll
    private final TaskManagerListener listener;
    // Liste der Aufgaben, die dieser Manager verwaltet
    private final ArrayList<Task> tasks;
    private final TaskDatabaseHelper db;

    /**
     * Erzeugt einen neuen TaskManager
     *
     * @param listener Observer/Listener, der über Änderungen an der Aufgabenliste informiert werden soll
     */
    public TaskManager(Context context, TaskManagerListener listener) {
        this.listener = listener;
        this.db = new TaskDatabaseHelper(context);
        this.tasks = db.getAllTasks();
    }

    public void requestUpdate() {
        listener.onTaskListUpdated();
    }

    /**
     * Fügt eine neue Aufgabe mit der übergebenen Beschreibung zur Liste hinzu und informiert danach den
     * angeschlossenen Listener
     *
     * @param description Beschreibung der neuen Aufgabe
     */
    public void addTask(String description) {
        Task taskToAdd = new Task(description);
        db.addTask(taskToAdd);
        tasks.add(taskToAdd);
        listener.onTaskListUpdated();
    }

    /**
     * Ändert den Zustand (offen bzw. erledigt) der Aufgabe an der übergebenen Position
     *
     * @param position Listenposition der Aufgaben, deren Zustand geändert werden soll
     */
    public void toggleTaskStateAtPosition(int position) {
        Task task = tasks.get(position);
        toggleTaskState(task);
    }

    /**
     * Ändert den Zustand (offen bzw. erledigt) der Aufgabe mit der übergebenen ID
     *
     * @param id ID der Aufgaben, deren Zustand geändert werden soll
     */
    public void toggleTaskStateForId(String id) {
        for (Task task : tasks) {
            if (task.id.toString().equals(id)) {
                toggleTaskState(task);
            }
        }
    }

    /**
     * Ändert den Zustand der übergebenen Aufgabe. Ist die Aufgabe aktuell als offen markiert,
     * so ändert sich ihr Zustand durch das Aufrufen dieser Methoden zu "erledigt" bzw. "geschlossen".
     *
     * @param taskToToggle Aufgabe, deren Zustand geändert werden soll
     */
    private void toggleTaskState(Task taskToToggle) {
        if (taskToToggle != null) {
            if (taskToToggle.isClosed()) {
                taskToToggle.markAsOpen();
            } else {
                taskToToggle.markAsClosed();
            }
            db.updateTask(taskToToggle);
            listener.onTaskListUpdated();
        }
    }

    /**
     * Gibt eine Kopier der aktuellen Aufgaben zurück, die von diesem TaskManger verwaltet werden
     *
     * @return Sortierte ArrayList mit Kopien der aktuellen Aufgabe
     */
    public ArrayList<Task> getCurrentTasks() {
        // Erstellen einer neuen, leeren Liste für die kopierten Aufgaben
        ArrayList<Task> currentTasks = new ArrayList<>();
        // Iteration über die Liste der aktuellen Aufgaben
        for (Task task : tasks) {
            // Hinzufügen von Kopien der Aufgaben zur neuen Liste
            currentTasks.add(task.copy());
        }
        // Sortieren der Einträge der neuen Liste. Dabei wir automatisch die Implementierung des
        // Comparable-Interface der Task-Klasse verwendet: Aufgaben werden absteigend nach Erstellungsdatum
        // sortiert und offene Aufgaben erscheinen vor geschlossenen
        Collections.sort(currentTasks);
        return currentTasks;
    }

    /**
     * Interface für Observer, die über Änderungen am Zustand eines TaskMangers informiert werden sollen.
     * Die Interface-Methode wird aufgerufen, wenn die Aufgabenliste durch Hinzufügen neuer oder Umschalten
     * bestehender Aufgaben geändert wurde.
     */
    public interface TaskManagerListener {

        /**
         * Wird aufgerufen, wenn sich die Aufgabenliste des Managers geändert hat
         */
        void onTaskListUpdated();

    }
}
