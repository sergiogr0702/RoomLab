package es.unex.giiis.asee.todomanager_db.roomdb;

import androidx.room.TypeConverter;

import es.unex.giiis.asee.todomanager_db.ToDoItem;

public class StatusConverter {

    @TypeConverter
    public static ToDoItem.Status toStatus(String status) {
        return  ToDoItem.Status.valueOf(status);
    }

    @TypeConverter
    public static String toString(ToDoItem.Status status){
        return status.name();
    }
}
