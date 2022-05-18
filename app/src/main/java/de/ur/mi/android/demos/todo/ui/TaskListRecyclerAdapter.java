package de.ur.mi.android.demos.todo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.ur.mi.android.demos.todo.R;
import de.ur.mi.android.demos.todo.tasks.Task;
import de.ur.mi.android.demos.todo.ui.viewholder.TaskListViewHolder;

/**
 * Dieser Adapter verbindet eine ArrayList von Task-Objekten mit einem RecyclerView im UserInterface. Der
 * Adapter stellt dem RecyclerView die notwendigen Views zu Darstellung der einzelnen Aufgaben bereit. Dabei
 * unterscheidet der Adpater zwischen offen und bereits erledigten Aufgaben und gibt je nach Zustand der
 * Aufgabe andere Views an das RecyclerView (die Taskliste im UI) weiter. Dadurch werden die beiden
 * "Typen" von Aufgaben im User Interface auf unterschiedliche Art dargestellt.
 */

public class TaskListRecyclerAdapter extends RecyclerView.Adapter<TaskListViewHolder> implements TaskListViewHolder.TaskListViewHolderListener {

    /*
     * Konstanten, die unterschiedliche Typen von Einträgen des angeschlossenen RecyclerViews identifizieren.
     * Das ist dann notwendig, wenn nicht alle Einträge einer Liste auf die gleiche Art und Weise dargestellt werden sollen,
     * in unserem Fall können wir so zwischen offenen und geschlossenen Aufgaben unterscheiden. In einer überschriebenen Methode
     * des Adapters werden den Task-Objekten jeweils einer dieser Typen zugeordnet. Diese "ID" wird dann in anderen Methoden des Adapters
     * verwendet, um die passenden Views für diese Art von Aufgabe zu erstellen.
     */
    private static final int VIEW_TYPE_FOR_OPEN_ITEMS = 1; // Repräsentiert eine noch offene Aufgabe
    private static final int VIEW_TYPE_FOR_CLOSED_ITEMS = 2; // Repräsentiert eine bereits geschlossene Aufgabe

    // Listener, der über Klicks auf einzelne Einträge im RecyclerView informiert werden soll
    private final TaskListAdapterListener listener;

    /**
     * Achtung: Dieser Adapter nutzt eine Kopie der Aufgabenliste, die vom TaskManager verwaltet wird. Ändern sich
     * dort die Inhalte, wird zuerst die Activity informiert, die dem Adapter dann eine aktualisierte Kopie der
     * Aufgabenliste übergibt.
     */

    // Liste der Aufgaben, die aktuell vom Adapter verwaltet werden
    private ArrayList<Task> tasks;

    /**
     * Erzeugt einen neuen Adapter
     *
     * @param listener Listener, der über die Interaktionen der Nutzer*innen mit den Listeneinträgen informiert werden soll
     */
    public TaskListRecyclerAdapter(TaskListAdapterListener listener) {
        this.listener = listener;
        this.tasks = new ArrayList<>();
    }

    /**
     * Überschreibt die Aufabenliste des Adapters mit der neuen, als Parameter übergebenen Liste und informiert im Anschluss
     * das angeschlossene RecyclerView
     *
     * @param tasks
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

    /**
     * Gibt den Typ von View zurück, der für die Darstellung der Aufgabe an der übergebenen Listenposition verwendet
     * werden soll. Diese Typen-Beschreibung wird dann an anderer Stelle verwendet, um das passende View
     * zur Darstellung zu erstellen. Die möglichen Typen werden über int-Werte unterschieden, zur besseren
     * Lesbarkeit nutzen wir die Konstanten, die zu Beginn der Adapterklasse definiert wurden.
     *
     * @param position Position des Elements in der Liste, dessen Typ abgefragt werden soll.
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Task task = tasks.get(position);
        if (task == null || task.isClosed()) {
            return VIEW_TYPE_FOR_CLOSED_ITEMS; // Typ für bereits geschlossene Aufgaben
        }
        return VIEW_TYPE_FOR_OPEN_ITEMS; // Typ für noch offene Aufgaben
    }

    /**
     * Wird aufgerufen, wenn ein neuer ViewHolder benötigt wird
     *
     * @param parent Elternelement unseres Layouts, hier der RecyclerView.
     * @param viewType Anzeige-Typ, der benötigt wird (wird automatisch über Aufruf der getItemViewType-Methode ausgewählt)
     * @return Der erstellte ViewHOlder
     */
    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskListViewHolder vh = createViewHolderForType(parent, viewType);
        return vh;
    }

    /**
     * Wird aufgerufen, wenn die Inhalte eines ViewHolders aktualisiert werden sollen
     *
     * @param holder   ViewHolder, der aktualisiert werden soll
     * @param position Position des zu aktualisierenden ViewHolders innerhalb des RecyclerViews (indirekter Verweis auf den zugehörigen Datensatz in der ArrayList)
     */
    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        // Wir identifzieren über den Positionsparameter, für welchen Datensatz der ViewHolder aktualisiert werden soll
        Task task = tasks.get(position);
        // Referenzieren der einzelnen TextViews im übergebenen View
        TextView description = holder.taskView.findViewById(R.id.list_item_description);
        TextView createdAt = holder.taskView.findViewById(R.id.list_item_creationDate);
        // Auslesen der Task-Eigenschaften und übertragen in die jeweiligen TextViews
        description.setText(task.getDescription());
        createdAt.setText(getFormattedDateForUI(task.getCreationDate()));
    }

    /**
     * Wird vom RecyclerView aufgerufen, um zu erfahren, wie viele Datensätze im Adapter verwaltet werden
     *
     * @return Anazhl der Einträge/Datensätze im Adapter (hier: Anzahl der Tasks in der ArrayList)
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * Erzeugt einen ViewHolder für eine Aufgabe des übergebenen Typs. Hier wird nur der leere View erzeugt,
     * es werden noch keine Inhalte einer konkreten Aufgaben in dem View eingetragen.
     *
     * @param parent   Das Elternelement im UI, zu dem der neue View hinzugefügt werden kann, z.B. das RecyclerView
     * @param viewType Typ des Views, das für die Darstellung dieser Aufgabe verwendet werden soll (siehe: getItemViewType)
     * @return Der erstellte VieHolder
     */
    private TaskListViewHolder createViewHolderForType(ViewGroup parent, int viewType) {
        View v;
        if (viewType == VIEW_TYPE_FOR_OPEN_ITEMS) {
            // Erstellen des View für offene Aufgaben mittels LayoutInflater
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        } else {
            // Alternativ: Erstellen des View für abgeschlossene Aufgaben
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item_done, parent, false);
        }
        // Erstellen des ViewHolders auf Basis des oben ausgewählten und erstellten Views
        TaskListViewHolder vh = new TaskListViewHolder(v, this);
        return vh;
    }

    /**
     * Wandelt das übergeben Datum in einen sauber formatiertern String zur Darstellung im UI um.
     * Datumsangaben werden in der Regel im Format [Tag]. [Monat] zurückgegeben. Liegt das übergebene Datum
     * weniger als 24h zurück, wird stattdessen das Format  [Stunden]:[Minuten] verwendet.
     *
     * @param date Datum, das formatiert werden soll
     * @return Formatierte Stringrepräsentation des übergebenen Datums
     */
    private String getFormattedDateForUI(LocalDateTime date) {
        // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now(TimeZone.getDefault().toZoneId());
        long timeDifferenceInMilliseconds = Duration.between(date, now).abs().toMillis();
        long timeDifferenceInDays = TimeUnit.DAYS.convert(timeDifferenceInMilliseconds, TimeUnit.MILLISECONDS);
        if (timeDifferenceInDays > 0) {
            formatter = DateTimeFormatter.ofPattern("dd. MMMM");
        }
        return formatter.format(date);
    }

    /**
     * Fängt lange Klicks auf einzelne ViewHolder ab. Der Adapter implementiert das TaskListViewHolderListener-Interface
     * und registriert sich selbst als Listener auf allen neuen ViewHolder. Diese informieren den Adapter über den Aufruf
     * dieser Methode, wenn sie von den Nutzer*innen angeklickt wurden.
     *
     * @param position Position dieses ViewHolders innerhalb des angeschlossenen RecyclerView
     */
    @Override
    public void onViewHolderLongClicked(int position) {
        Task task = tasks.get(position);
        if (task != null) {
            listener.onItemSelected(task);
        }
    }

    /**
     * Interface für Observer, die über die Interaktion der Nutzer*innen mit diesem Adapter bzw. dem
     * angeschlossenen RecyclerView informiert werden wollen - wird von der MainActiviy implementiert,
     * die sich selbst als Listener beim Erstellen dieses Adapters übergibt.
     */
    public interface TaskListAdapterListener {
        /**
         * Wird aufgerufen, wenn ein Eintrag des Views per LongClick ausgewählt wurde
         *
         * @param task Task, dessen UI-Repräsentation ausgewählt wurde
         */
        void onItemSelected(Task task);
    }

}