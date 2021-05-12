package de.ur.mi.android.demos.todo.tasks;
import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;

public class TaskAttributeTypeConverter {

    // TypeConverter for Date <-> Long
    @TypeConverter
    public static Date millisecondsToDate(Long milliseconds) {
        return milliseconds == null ? null : new Date(milliseconds);
    }

    @TypeConverter
    public static Long dateToMilliseconds(Date date) {
        return date == null ? null : date.getTime();
    }

    // TypeConverter for UUID <-> String
    @TypeConverter
    public static UUID stringToUUID(String taskID_string) {
        return taskID_string == null ? null : UUID.fromString(taskID_string);
    }

    @TypeConverter
    public static String UUID_toString(UUID taskID) {
        return taskID == null ? null : taskID.toString();
    }

    // TypeConverter for TaskState <-> Integer
    @TypeConverter
    public static Task.TaskState intToTaskState(Integer taskStateInt) {
        switch(taskStateInt){
            case 0:
                return Task.TaskState.OPEN;
            case 1:
                return Task.TaskState.CLOSED;
            default:
                return null;
        }
    }

    @TypeConverter
    public static Integer taskStateToInt(Task.TaskState taskState) {
        switch(taskState){
            case OPEN:
                return 0;
            case CLOSED:
                return 1;
            default:
                return null;
        }
    }
}
