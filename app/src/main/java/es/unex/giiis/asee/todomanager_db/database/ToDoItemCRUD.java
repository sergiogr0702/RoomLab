package es.unex.giiis.asee.todomanager_db.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.todomanager_db.ToDoItem;

public final class ToDoItemCRUD {

    private ToDoManagerDbHelper mDbHelper;
    private static ToDoItemCRUD mInstance;

    private ToDoItemCRUD(Context context) {
        mDbHelper = new ToDoManagerDbHelper(context);
    }

    public static ToDoItemCRUD getInstance(Context context){
        if (mInstance == null)
            mInstance = new ToDoItemCRUD(context);

        return mInstance;
    }

    public List<ToDoItem> getAll(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DBContract.TodoItem._ID,
                DBContract.TodoItem.COLUMN_NAME_TITLE,
                DBContract.TodoItem.COLUMN_NAME_STATUS,
                DBContract.TodoItem.COLUMN_NAME_PRIORITY,
                DBContract.TodoItem.COLUMN_NAME_DATE
        };

        String selection = null;
        String[] selectionArgs = null;

        String sortOrder = null;

        Cursor cursor = db.query(
                DBContract.TodoItem.TABLE_NAME,           // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        ArrayList<ToDoItem> items = new ArrayList<>();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                items.add(getToDoItemFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public long insert(ToDoItem item){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.TodoItem.COLUMN_NAME_TITLE, item.getTitle());
        values.put(DBContract.TodoItem.COLUMN_NAME_PRIORITY, item.getPriority().name());
        values.put(DBContract.TodoItem.COLUMN_NAME_STATUS, item.getStatus().name());
        values.put(DBContract.TodoItem.COLUMN_NAME_DATE, ToDoItem.FORMAT.format(item.getDate()));


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBContract.TodoItem.TABLE_NAME, null, values);

        return newRowId;
    }

    public void deleteAll() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = null;
        // Specify arguments in placeholder order.
        String[] selectionArgs = null;

        // Issue SQL statement.
        db.delete(DBContract.TodoItem.TABLE_NAME, selection, selectionArgs);
    }

    public int updateStatus(long ID, ToDoItem.Status status) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Log.d("ToDoItemCRUD","Item ID: "+ID);

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DBContract.TodoItem.COLUMN_NAME_STATUS, status.name());

        // Which row to update, based on the ID
        String selection = DBContract.TodoItem._ID + " = ?";
        String[] selectionArgs = { Long.toString(ID) };

        int count = db.update(
                DBContract.TodoItem.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public void close(){
        if (mDbHelper!=null) mDbHelper.close();
    }

    public static ToDoItem getToDoItemFromCursor(Cursor cursor) {

        long ID = cursor.getInt(cursor.getColumnIndex(DBContract.TodoItem._ID));
        String title = cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_TITLE));
        String priority = cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_PRIORITY));
        String status = cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_STATUS));
        String date = cursor.getString(cursor.getColumnIndex(DBContract.TodoItem.COLUMN_NAME_DATE));

        ToDoItem item = new ToDoItem(ID,title,priority,status,date);

        Log.d("ToDoItemCRUD",item.toLog());

        return item;
    }
}
