package de.ur.mi.android.demos.todo.tasks;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.UUID;

/**
 * Repräsentiert eine Aufgabe auf der ToDo-liste.
 * <p>
 * Aufgaben verfügen über eine eindeutige ID, eine textuelle Beschreibung und ein Erstellungs-
 * datum. Diese Eigenschaften sind nach dem Erzeugen einer Aufgabe nicht veränderbar. Zusätzlich
 * wird für jede Aufgabe festgehalten, ob diese offen oder erledigt ist. Der Zustand kann über
 * öffentliche Methoden geändert werden.
 * <p>
 * Kopien
 * Über die copy-Methode können tiefe (deep) Kopien eines Task-Objekts erstellt werden.
 * <p>
 * Sortierung
 * Die Task-Klasse implementiert das Comparable-Interface das die Sortierung von Task-Objekten
 * ermöglicht. Offene Aufgaben werden vor geschlossenen Aufgaben einsportiert. Aufgaben mit
 * gleichem Status werden nach dem Erstellungsdatum sortiert.
 */
@Entity(tableName = "task_table")
public class Task implements Comparable<Task> {
    @PrimaryKey
    @NonNull
    public UUID id;
    public String description;
    @ColumnInfo(name = "creation_date")
    public Date creationDate;
    @ColumnInfo(name = "task_state")
    public TaskState state;

    public Task(String description) {
        this.id = UUID.randomUUID();
        this.creationDate = new Date();
        this.state = TaskState.OPEN;
        this.description = description;
    }

    private Task(String description, UUID id, Date createdAt, TaskState currentState) {
        this.id = id;
        this.creationDate = createdAt;
        this.state = currentState;
        this.description = description;
    }

    public String getId() {
        return this.id.toString();
    }

    public String getDescription() {
        return this.description;
    }

    public Date getCreationDate() {
        return getCreationDateCopy();
    }

    public boolean isClosed() {
        return this.state == TaskState.CLOSED;
    }

    public void markAsOpen() {
        this.state = TaskState.OPEN;
    }

    public void markAsClosed() {
        this.state = TaskState.CLOSED;
    }

    public Task copy() {
        Date creationDateFromOriginal = getCreationDateCopy();
        return new Task(description, id, creationDateFromOriginal, state);
    }

    private Date getCreationDateCopy() {
        return new Date(creationDate.getTime());
    }

    @Override
    public int compareTo(Task otherTask) {
        if (this.isClosed() && !otherTask.isClosed()) {
            // Diese Aufgabe wird, weil bereits erledigt, nach der anderen sortiert
            return 1;
        }
        if (!this.isClosed() && otherTask.isClosed()) {
            // Diese Aufgabe wird, weil noch nicht erledigt, vor der anderen sortiert
            return -1;
        }
        // Die beiden Aufgaben werden auf Basis des Erstellungsdatums sortiert (neuere vor älteren)
        return -this.creationDate.compareTo(otherTask.creationDate);
    }

    public enum TaskState {
        OPEN,
        CLOSED
    }

}
