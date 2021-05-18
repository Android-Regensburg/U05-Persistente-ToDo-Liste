package de.ur.mi.android.demos.todo.room;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import de.ur.mi.android.demos.todo.tasks.Task;

/*
Das DAO enthält Methoden, die abstrakten Zugriff auf die Datenbank erlauben.
Room erzeugt zur Compile-Zeit automatisch eine Implementierung der von uns definierten DAOs.
Android-Dokumentation: https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface TaskDAO {
    // einzelnen Task in die Datenbank einfügen
    @Insert
    void insertTask(Task task);

    // einzelnen Task in der Datenbank updaten
    @Update
    void updateTask(Task task);

    // Alle Datenpunkte der Tabelle "task_table" zurückgeben
    @Query("SELECT * FROM task_table")
    List<Task> getAllTasks();
}
