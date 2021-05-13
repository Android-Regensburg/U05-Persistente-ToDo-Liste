package de.ur.mi.android.demos.todo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import de.ur.mi.android.demos.todo.tasks.Task;
import de.ur.mi.android.demos.todo.tasks.TaskManager;
import de.ur.mi.android.demos.todo.ui.TaskListAdapter;

public class MainActivity extends AppCompatActivity implements TaskManager.TaskManagerListener {

    private TaskManager taskManager;
    private TaskListAdapter taskListAdapter;
    private EditText taskDescriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initTaskManager();
    }

    private void initTaskManager() {
        taskManager = new TaskManager(getApplicationContext(), this);
        taskManager.requestUpdate();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        initListView();
        initInputElements();
    }

    private void initListView() {
        taskListAdapter = new TaskListAdapter(this);
        ListView taskList = findViewById(R.id.task_list);
        taskList.setAdapter(taskListAdapter);
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                toggleTaskAtPosition(position);
                return true;
            }
        });
    }

    private void initInputElements() {
        taskDescriptionInput = findViewById(R.id.input_text);
        Button inputButton = findViewById(R.id.input_button);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentInput = taskDescriptionInput.getText().toString();
                onUserInputClicked(currentInput);
            }
        });
    }

    private void toggleTaskAtPosition(int position) {
        taskManager.toggleTaskStateAtPosition(position);
    }

    private void onUserInputClicked(String input) {
        if (input.length() > 0) {
            taskManager.addTask(input);
            taskDescriptionInput.setText("");
            taskDescriptionInput.requestFocus();
        }
    }

    @Override
    public void onTaskListUpdated() {
        ArrayList<Task> currentTasks = taskManager.getCurrentTasks();
        taskListAdapter.setTasks(currentTasks);
    }
}