package es.unex.giiis.asee.todomanager_db.roomdb;

import androidx.room.TypeConverter;

import es.unex.giiis.asee.todomanager_db.ToDoItem;

public class PriorityConverter {

    @TypeConverter
    public static ToDoItem.Priority toPriority(String priority) {
        return  ToDoItem.Priority.valueOf(priority);
    }

    @TypeConverter
    public static String toString(ToDoItem.Priority priority){
        return priority.name();
    }
}
