package de.ur.mi.android.demos.todo.tasks;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import de.ur.mi.android.demos.todo.room.RoomDatabaseHelper;

public class TaskManager {

    private final TaskManagerListener listener;
    private ArrayList<Task> tasks;
    private final RoomDatabaseHelper dbHelper;

    public TaskManager(TaskManagerListener listener, RoomDatabaseHelper dbHelper) {
        this.listener = listener;
        this.dbHelper = dbHelper;
        this.tasks = new ArrayList<>();
    }

    public void initTaskList(){
        this.tasks = dbHelper.getAllTasksFromDB();
        listener.onTasksLoadedFromDatabase();
    }

    public void addTask(String description) {
        Task taskToAdd = new Task(description);
        dbHelper.addSingleTaskToDB(taskToAdd);
        tasks.add(taskToAdd);
        listener.onTaskAdded(taskToAdd);
    }

    public void toggleTaskStateForId(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                toggleTaskState(task);
            }
        }
    }

    private void toggleTaskState(Task taskToToggle) {
        if (taskToToggle != null) {
            if (taskToToggle.isClosed()) {
                taskToToggle.markAsOpen();
            } else {
                taskToToggle.markAsClosed();
            }
            dbHelper.updateSingleTaskInDB(taskToToggle);
            listener.onTaskChanged(taskToToggle);
        }
    }

    public ArrayList<Task> getCurrentTasks() {
        ArrayList<Task> currentTasks = new ArrayList<>();
        for (Task task : tasks) {
            currentTasks.add(task.copy());
        }
        Collections.sort(currentTasks);
        return currentTasks;
    }

    public interface TaskManagerListener {
        void onTasksLoadedFromDatabase();
        void onTaskAdded(Task task);
        void onTaskChanged(Task task);
    }
}
