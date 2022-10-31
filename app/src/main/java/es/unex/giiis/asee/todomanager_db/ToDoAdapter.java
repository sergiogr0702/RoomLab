package es.unex.giiis.asee.todomanager_db;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.todomanager_db.database.ToDoItemCRUD;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoItem> mItems = new ArrayList<ToDoItem>();
    Context mContext;

    public interface OnItemClickListener {
        void onItemClick(ToDoItem item);     //Type of the element to be returned
    }

    private final OnItemClickListener listener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToDoAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ToDoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // - Inflate the View for every element
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new ViewHolder(mContext,v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mItems.get(position),listener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(ToDoItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public void clear(){

        mItems.clear();
        notifyDataSetChanged();

    }

    public void load(List<ToDoItem> items){

        mItems.clear();
        mItems = items;
        notifyDataSetChanged();

    }

    public Object getItem(int pos) { return mItems.get(pos); }

     static class ViewHolder extends RecyclerView.ViewHolder {

         private Context mContext;

        private TextView title;
        private CheckBox statusView;
        private TextView priorityView;
        private TextView dateView;

        public ViewHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;

            // - Get the references to every widget of the Item View
            title =  itemView.findViewById(R.id.titleView);
            statusView =  itemView.findViewById(R.id.statusCheckBox);
            priorityView =  itemView.findViewById(R.id.priorityView);
            dateView =  itemView.findViewById(R.id.dateView);

        }

        public void bind(final ToDoItem toDoItem, final OnItemClickListener listener) {

            // - Display Title in TextView
            title.setText(toDoItem.getTitle());

            // - Display Priority in a TextView
            priorityView.setText(toDoItem.getPriority().toString());

            //  - Display Time and Date.
            // Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String
            dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));

            //  - Set up Status CheckBox
            statusView.setChecked(toDoItem.getStatus() == ToDoItem.Status.DONE);
            if (toDoItem.getStatus() == ToDoItem.Status.DONE)
                title.setBackgroundColor(Color.GREEN);

            statusView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {

                    //  Set up and implement an OnCheckedChangeListener
                    // is called when the user toggles the status checkbox
                    if (isChecked) {
                        toDoItem.setStatus(ToDoItem.Status.DONE);

                        title.setBackgroundColor(Color.GREEN);
                    } else {
                        toDoItem.setStatus(ToDoItem.Status.NOTDONE);
                        title.setBackgroundColor(Color.WHITE);
                    }

                    ToDoItemCRUD crud = ToDoItemCRUD.getInstance(mContext);
                    crud.updateStatus(toDoItem.getId(),toDoItem.getStatus());

                }});

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(toDoItem);
                }
            });
        }
    }

}
