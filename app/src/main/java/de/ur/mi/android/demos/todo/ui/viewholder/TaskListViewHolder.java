package de.ur.mi.android.demos.todo.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder für einzelne Einträge des RecyclerViews. Ein ViewHolder umschließt einen einzelnen Eintrag
 * innerhalb eines RecyclerViews. D.h. für jeden Listeneintrag wird ein solcher ViewHolder benötigt, der
 * in sich dann das eigentlichen View beeinhaltet, in dem die Informationen des jeweiligen Task-Objektes
 * dargestellt werden.
 */
public class TaskListViewHolder extends RecyclerView.ViewHolder {

    public final View taskView;
    public final TaskListViewHolderListener viewHolderListener;

    /**
     * Wird im TaskListRecyclerAdapter aufgerufen, wenn ein neuer View für die Darstellung eines Eintrags
     * benötigt wird.
     *
     * Der Listener ist bei uns der RecyclerViewAdapter, der das TaskViewHolderListener-
     * Interface implementiert.
     *
     * @param itemView View für eigentlichen Eintrag innerhalb des RecyclerViews
     * @param listener Eigene Ergänzung: Listener, der über Interaktion der Nutzer*innen mit diesem ViewHolder informiert werden soll
     */
    public TaskListViewHolder(@NonNull View itemView, TaskListViewHolderListener listener) {
        super(itemView);
        this.viewHolderListener = listener;
        taskView = itemView;
        // Beim Erstellen des neuen ViewHolders registrieren wird zusätzlich einen LongClick-Listener.
        // Die Interaktion fangen wir im Holder ab und geben die Information, dass dieser View
        // angeklickt wurde an den im Konstruktor übergebenen Listener (TaskListViewHolderListener) weiter.
        taskView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewHolderListener.onViewHolderLongClicked(getAdapterPosition());
                return true;
            }
        });
    }

    /**
     * Interface für die Komponenten, die sich für die Interaktion der Nutzer*innen mit diesem ViewHolder bzw. dem
     * umschlossenen View interessieren
     */
    public interface TaskListViewHolderListener {
        /**
         * Wird aufgerufen, wenn das View, das über diesen ViewHolder verwaltet wird, angeklickt wurde
         *
         * @param position Position dieses ViewHolders innerhalb des angeschlossenen RecyclerView
         */
        void onViewHolderLongClicked(int position);
    }


}