package es.unex.giiis.asee.todomanager_db;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import es.unex.giiis.asee.todomanager_db.ToDoItem.Priority;
import es.unex.giiis.asee.todomanager_db.ToDoItem.Status;
import es.unex.giiis.asee.todomanager_db.database.ToDoItemCRUD;

public class ToDoManagerActivity extends AppCompatActivity {

    // Add a ToDoItem Request Code
    private static final int ADD_TODO_ITEM_REQUEST = 0;

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ToDoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - Attach Listener to FloatingActionButton. Implement onClick()
                Intent intent = new Intent(ToDoManagerActivity.this, AddToDoActivity.class);
                startActivityForResult(intent,ADD_TODO_ITEM_REQUEST);
            }
        });

        //TODO - Get a reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //TODO - Set a Linear Layout Manager to the RecyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //TODO - Create a new Adapter for the RecyclerView
        // specify an adapter (see also next example)
        mAdapter = new ToDoAdapter(this, new ToDoAdapter.OnItemClickListener() {
            @Override public void onItemClick(ToDoItem item) {
                Snackbar.make(ToDoManagerActivity.this.getCurrentFocus(), "Item "+item.getTitle()+" Clicked", Snackbar.LENGTH_LONG).show();
            }
        });

        //TODO - Attach the adapter to the RecyclerView
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        log("Entered onActivityResult()");

        // TODO - Check result code and request code.
        // If user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_TODO_ITEM_REQUEST){
            if (resultCode == RESULT_OK){
                ToDoItem item = new ToDoItem(data);

                //insert into DB
                ToDoItemCRUD crud = ToDoItemCRUD.getInstance(this);
                long id = crud.insert(item);

                //update item ID
                item.setID(id);

                //insert into adapter list
                mAdapter.add(item);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

        if (mAdapter.getItemCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save ToDoItems

//        saveItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_DUMP:
                dump();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
            log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
        }

    }

    // Load stored ToDoItems
    private void loadItems() {
        ToDoItemCRUD crud = ToDoItemCRUD.getInstance(this);
        List<ToDoItem> items = crud.getAll();
        mAdapter.load(items);
    }

//    private void loadItems() {
//        BufferedReader reader = null;
//        try {
//            FileInputStream fis = openFileInput(FILE_NAME);
//            reader = new BufferedReader(new InputStreamReader(fis));
//
//            String title = null;
//            String priority = null;
//            String status = null;
//            Date date = null;
//
//            while (null != (title = reader.readLine())) {
//                priority = reader.readLine();
//                status = reader.readLine();
//                date = ToDoItem.FORMAT.parse(reader.readLine());
//                mAdapter.add(new ToDoItem(title, Priority.valueOf(priority),
//                        Status.valueOf(status), date));
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != reader) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    // Save ToDoItems to file
//    private void saveItems() {
//        PrintWriter writer = null;
//        try {
//            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
//                    fos)));
//
//            for (int idx = 0; idx < mAdapter.getItemCount(); idx++) {
//
//                writer.println(mAdapter.getItem(idx));
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != writer) {
//                writer.close();
//            }
//        }
//    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }


}
