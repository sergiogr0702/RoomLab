package es.unex.giiis.asee.todomanager_db.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.giiis.asee.todomanager_db.ToDoItem;

@Dao
public interface ToDoItemDao {

    @Query("SELECT * FROM todo")
    public List<ToDoItem> getAll();

    @Insert
    public long insert(ToDoItem item);

    @Update
    public int updateStatus(ToDoItem item);

    @Query("DELETE FROM todo")
    public void deleteAll();
}
