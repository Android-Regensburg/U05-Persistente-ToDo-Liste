package de.ur.mi.android.demos.todo.room;
import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;
import de.ur.mi.android.demos.todo.tasks.Task;


/*
 Die SQLight Datenbank kann keine komplexen Objekte (Strings ausgeschlossen!) speichern, weshalb diese beim Schreiben in die Datenbank in einen entsprechenden
 kompatiblen Datentyp umgewandelt werden müssen. Beim Auslesen wird der Wert dann wieder in den ursprünglichen Datentyp konvertiert.
 Android-Dokumentation: https://developer.android.com/training/data-storage/room/referencing-data
 */
public class TaskAttributeTypeConverter {

    /*
    TypeConverter zum Konvertieren von Date <-> Long
     */
    @TypeConverter
    public static Date millisecondsToDate(Long milliseconds) {
        return milliseconds == null ? null : new Date(milliseconds);
    }
    @TypeConverter
    public static Long dateToMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }

    /*
    TypeConverter zum Konvertieren von UUID <-> String
     */
    @TypeConverter
    public static UUID stringToUUID(String taskID_string) {
        return taskID_string == null ? null : UUID.fromString(taskID_string);
    }
    @TypeConverter
    public static String UUID_toString(UUID taskID) {
        return taskID == null ? null : taskID.toString();
    }

    /*
    TypeConverter zum Konvertieren von TaskState <-> Integer
     */
    @TypeConverter
    public static Task.TaskState intToTaskState(Integer taskStateInt) {
        return Task.TaskState.values()[taskStateInt];
    }
    @TypeConverter
    public static Integer taskStateToInt(Task.TaskState taskState) {
        return taskState.ordinal();
    }
}