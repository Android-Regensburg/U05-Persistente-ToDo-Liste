package de.ur.mi.android.demos.todo.room;
import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;
import de.ur.mi.android.demos.todo.tasks.Task;

/**
 * TypeConverter zur Umwandlung komplexer in primitive Datentypen und vice versa
 */
public class TaskAttributeTypeConverter {

    // Konvertieren eines Datums (Date) in die entsprechenden Millisekunden (Long)
    @TypeConverter
    public static Date millisecondsToDate(Long milliseconds) {
        return milliseconds == null ? null : new Date(milliseconds);
    }

    // Konvertieren von Millisekunden (Long) in ein entsprechendes Datum (Date)
    @TypeConverter
    public static Long dateToMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }

    // Konvertieren von String zu UUID
    @TypeConverter
    public static UUID stringToUUID(String taskID_string) {
        return taskID_string == null ? null : UUID.fromString(taskID_string);
    }

    // Konvertieren von UUID zu String
    @TypeConverter
    public static String UUID_toString(UUID taskID) {
        return taskID == null ? null : taskID.toString();
    }

    // Konvertieren von Integer zu einem TaskState
    @TypeConverter
    public static Task.TaskState intToTaskState(Integer taskStateInt) {
        return Task.TaskState.values()[taskStateInt];
    }

    // Konvertieren eines TaskStates in den entsprechenden Integer Wert
    @TypeConverter
    public static Integer taskStateToInt(Task.TaskState taskState) {
        return taskState.ordinal();
    }
}