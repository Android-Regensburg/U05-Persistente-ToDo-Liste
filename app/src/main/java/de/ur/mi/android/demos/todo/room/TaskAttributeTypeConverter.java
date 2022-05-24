package de.ur.mi.android.demos.todo.room;
import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import de.ur.mi.android.demos.todo.tasks.Task;

/**
 * TypeConverter zur Umwandlung komplexer in primitive Datentypen und vice versa
 */
public class TaskAttributeTypeConverter {

    // Konvertieren eines Datums (Date) in die entsprechenden Millisekunden (Long)
    @TypeConverter
    public static LocalDateTime millisecondsToDate(Long milliseconds) {
        return milliseconds == null ? null : Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // Konvertieren von Millisekunden (Long) in ein entsprechendes Datum (Date)
    @TypeConverter
    public static Long dateToMilliseconds(LocalDateTime date) {
        return date == null ? null : date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
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