package de.ur.mi.android.demos.todo.room;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import de.ur.mi.android.demos.todo.tasks.Task;
import de.ur.mi.android.demos.todo.tasks.TaskAttributeTypeConverter;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({TaskAttributeTypeConverter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract TaskDAO taskDao();
    private static TaskRoomDatabase INSTANCE;

    //Singleton
    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, "task_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
