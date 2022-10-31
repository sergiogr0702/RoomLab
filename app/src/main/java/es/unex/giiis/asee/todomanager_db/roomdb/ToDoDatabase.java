package es.unex.giiis.asee.todomanager_db.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giiis.asee.todomanager_db.ToDoItem;

@Database(entities = {ToDoItem.class}, version = 1)
public abstract class ToDoDatabase extends RoomDatabase {
    private static ToDoDatabase instance;

    public static ToDoDatabase getDatabase(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, "todo.db").build();
        return instance;
    }

    public abstract ToDoItemDao toDoItemDao();
}
