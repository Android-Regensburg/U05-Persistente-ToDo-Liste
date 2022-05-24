package de.ur.mi.android.demos.todo.tasks;

import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Erzeugt einen neuen TaskManager
     * Der Listener ist bei uns die MainActivity, die das TaskManagerListener-Interface
     * implementiert.
     *
     * @param listener Observer/Listener, der über Änderungen an der Aufgabenliste informiert werden soll
     */
    public TaskManager(TaskManagerListener listener) {
        this.listener = listener;
        this.tasks = new ArrayList<>();
    }

    /**
     * Fügt eine neue Aufgabe mit der übergebenen Beschreibung zur Liste hinzu und informiert danach den
     * angeschlossenen Listener
     *
     * @param description Beschreibung der neuen Aufgabe
     */
    public void addTask(String description) {
        Task taskToAdd = new Task(description);
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
            if (task.getID().equals(id)) {
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
            listener.onTaskListUpdated();
        }
    }

    /**
     * Gibt eine Kopie der aktuellen Aufgaben zurück, die von diesem TaskManger verwaltet werden
     *
     * @return Sortierte ArrayList mit Kopien der aktuellen Aufgabe
     */
    public ArrayList<Task> getDeepCopyOfCurrentTasks() {
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
    
    /*
    * Diese Methode kann verwendet werden, um von außerhalb des TaskManagers eine Aktualisierung durch den
    * registrierten Listener anzufordern. Beachten Sie, dass dies erst möglich ist, wenn TaskManager
    * und über den Adapter angeschlossenes UI vollständig initialisiert wurden.
    */
    public void requestUpdate() {
        listener.onTaskListUpdated();
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
