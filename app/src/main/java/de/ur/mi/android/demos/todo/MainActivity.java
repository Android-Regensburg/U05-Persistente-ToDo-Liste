package de.ur.mi.android.demos.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import de.ur.mi.android.demos.todo.tasks.Task;
import de.ur.mi.android.demos.todo.tasks.TaskManager;
import de.ur.mi.android.demos.todo.ui.TaskListRecyclerAdapter;

/**
 * ToDo-Liste
 *
 * Diese App erlaubt den Nutzer*innen das Zusammenstellen einer einfachen Aufgabenliste. Dazu können
 * neue Aufgaben erstellt und der Zustand (offen/geschlossen) existierende Aufgaben umgeschaltet werden.
 * In dieser Version der App werden die Einträge der Liste nicht gespeichert, d.h. nach Beenden der
 * App gehen alle erstellten Aufgaben verloren. Diese Variante stellt eine einfache fortgeschrittene Lösung
 * für die ursprüngliche Aufgabenstellung dar.
 *
 * Diese Activity initialisiert die notwendigen Komponenten des User Interface. Die Aufgaben werden
 * in einer Instanz der TaskManger-Klasse verwaltet. Für deren Darstellung wird ein RecyclerView
 * verwendet, der über einen entsprechenden Adapter mit der Datenstruktur verbunden wird. Der Adapter
 * erhält nach Änderungen am TaskManager eine Kopie der dort verwalteten Aufgabenliste übergeben.
 * Interaktionen der Nutzer*innen mit den Inhalten des RecyclerViews werden über einen mehrstufigen
 * Prozess via Listener an diese Activity weitergegeben, hier verarbeitete und zur Manipulation der
 * Inhalte des TaskManagers verwendet.
 */

public class MainActivity extends Activity implements TaskListRecyclerAdapter.TaskListAdapterListener, TaskManager.TaskManagerListener {

    // In diesem Objekt werden die eigentlichen Daten unserer Anwendung, ergo die Aufgabenliste, verwaltet
    private TaskManager taskManager;
    // Eingabefeld für den Beschreibungstext neuer Aufgaben
    private EditText taskInputElement;
    // Adapter zur Verbindung der Datenstruktur (tasks) mit dem RecyclerView, der die Inhalte im UI anzeigt
    private TaskListRecyclerAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTasks();
        initUI();
    }

    /**
     * Initialisiert einen neuen TaskManager zum Speichern der Aufgaben
     */
    private void initTasks() {
        // Änderungen am Zustand des TaskManagers werden über eine Listener-Schnittstelle an diese Activity weitergeben
        // Ändert sich also die Liste im TaskManager oder wird der Status eines Tasks geändert, wird
        // die onTaskListUpdated-Methode dieser Activity aufgerufen.
        taskManager = new TaskManager(this);
    }

    /**
     * Initialisiert Adapter und Liste sowie den Button zum Hinzufügen neuer Aufgaben
     */
    private void initUI() {
        setContentView(R.layout.activity_main);
        RecyclerView taskList = findViewById(R.id.task_list);
        // Die Activity registriert sich selbst als Listener auf dem Adapter, wird ein Element
        // im RecylerView lange geklickt, wird so die onItemSelected-Methode dieser Activity aufgerufen.
        taskListAdapter = new TaskListRecyclerAdapter(this);
        taskList.setAdapter(taskListAdapter);
        taskInputElement = findViewById(R.id.input_text);
        Button button = findViewById(R.id.input_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskFromUI();
            }
        });
    }

    /**
     * Prüft, ob der aktuelle Inhalt des EditTexts leer ist, falls nein, wird er als Beschreibung eines
     * neuen Tasks verwendet und dessen Basis eine neue Aufgabe im TaskManager erstellt.
     */
    private void addTaskFromUI() {
        String description = taskInputElement.getText().toString();
        if (description.length() > 0) {
            taskManager.addTask(description);
            taskInputElement.setText("");
            // versucht das Eingabefeld zu fokussieren, so dass direkt die nächste Aufgabe eingegeben werden kann.
            taskInputElement.requestFocus();
        }
    }

    /**
     * Wird aufgerufen, wenn im RecyclerView einer der Listeneinträge ausgewählt wurde
     *
     * @param task Task, der mit dem Listenelement korrespondiert, das lange gedrückt wurde.
     */
    @Override
    public void onItemSelected(Task task) {
        taskManager.toggleTaskStateForId(task.getID());
    }

    /**
     * Wird aufgerufen, wenn neue Inhalte zum TaskManager hinzugefügt wurden oder der Status existierender
     * Aufgaben angepasst wurde
     * Dann wird der Adapter über diese Änderung informiert und die angezeigte Liste angepasst.
     * Dafür wird eine tiefe Kopie der Liste verwendet, um UI-Repräsentation und interne Datenstruktur
     * strikt zu trennen.
     */
    @Override
    public void onTaskListUpdated() {
        taskListAdapter.setTasks(taskManager.getDeepCopyOfCurrentTasks());
    }
}