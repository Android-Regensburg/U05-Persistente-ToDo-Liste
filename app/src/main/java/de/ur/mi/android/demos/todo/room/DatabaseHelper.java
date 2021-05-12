package de.ur.mi.android.demos.todo.room;

import android.content.Context;

import java.util.ArrayList;

import de.ur.mi.android.demos.todo.tasks.Task;

public class DatabaseHelper {

    TaskRoomDatabase db;

    public DatabaseHelper(Context context){
        db = TaskRoomDatabase.getDatabase(context);
    }

    public void addSingleTaskToDB(Task task){
        db.taskDao().insertTask(task);
    }

    public void updateSingleTaskInDB(Task task){
        db.taskDao().updateTask(task);
    }

    public ArrayList<Task> getAllTasksFromDB(){
        return new ArrayList<Task>(db.taskDao().getAllTasks());
    }
}
