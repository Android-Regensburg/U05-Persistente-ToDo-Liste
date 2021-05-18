package de.ur.mi.android.demos.todo.room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import de.ur.mi.android.demos.todo.tasks.Task;

// Klasse als RoomDatenbank markieren und über die Entitäten informieren
@Database(entities = {Task.class}, version = 1)
// Datenbank über benötigte TypeConverters informieren
@TypeConverters({TaskAttributeTypeConverter.class})
public abstract class TaskDatabase extends RoomDatabase {

    // TaskDAO ist eine Klasse, die mit @Dao annotiert wurde
    public abstract TaskDAO taskDao();

}
