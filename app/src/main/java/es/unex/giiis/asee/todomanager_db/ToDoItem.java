package es.unex.giiis.asee.todomanager_db;

import android.content.Intent;

import androidx.room.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.unex.giiis.asee.todomanager_db.roomdb.DateConverter;
import es.unex.giiis.asee.todomanager_db.roomdb.PriorityConverter;
import es.unex.giiis.asee.todomanager_db.roomdb.StatusConverter;

@Entity(tableName = "todo")
public class ToDoItem {

	@Ignore
	public static final String ITEM_SEP = System.getProperty("line.separator");

	public enum Priority {
		LOW, MED, HIGH
	};

	public enum Status {
		NOTDONE, DONE
	};

	@Ignore
	public final static String ID = "ID";
	@Ignore
	public final static String TITLE = "title";
	@Ignore
	public final static String PRIORITY = "priority";
	@Ignore
	public final static String STATUS = "status";
	@Ignore
	public final static String DATE = "date";
	@Ignore
	public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.US);

	@PrimaryKey(autoGenerate = true)
	private long id;

	private String title = new String();
	@TypeConverters(PriorityConverter.class)
	private Priority priority = Priority.LOW;
	@TypeConverters(StatusConverter.class)
	private Status status = Status.NOTDONE;
	@TypeConverters(DateConverter.class)
	private Date date = new Date();

	@Ignore
	ToDoItem(String title, Priority priority, Status status, Date date) {
		this.title = title;
		this.priority = priority;
		this.status = status;
		this.date = date;
	}

    public ToDoItem(long id, String title, Priority priority, Status status, Date date) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
		this.date = date;
    }

	@Ignore
	public ToDoItem(long id, String title, String priority, String status, String date) {
		this.id = id;
		this.title = title;
		this.priority = Priority.valueOf(priority);
		this.status = Status.valueOf(status);
		try {
			this.date = ToDoItem.FORMAT.parse(date);
		} catch (ParseException e) {
			this.date = new Date();
		}
	}

	// Create a new ToDoItem from data packaged in an Intent
	@Ignore
	ToDoItem(Intent intent) {
		id = intent.getLongExtra(ToDoItem.ID,0); //TODO think best default value for ID
		title = intent.getStringExtra(ToDoItem.TITLE);
		priority = Priority.valueOf(intent.getStringExtra(ToDoItem.PRIORITY));
		status = Status.valueOf(intent.getStringExtra(ToDoItem.STATUS));

		try {
			date = ToDoItem.FORMAT.parse(intent.getStringExtra(ToDoItem.DATE));
		} catch (ParseException e) {
			date = new Date();
		}
	}

    public long getId() { return id; }

    public void setId(long ID) { this.id = ID; }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	// Take a set of String data values and 
	// package them for transport in an Intent

	public static void packageIntent(Intent intent, String title,
									 Priority priority, Status status, String date) {

		intent.putExtra(ToDoItem.TITLE, title);
		intent.putExtra(ToDoItem.PRIORITY, priority.toString());
		intent.putExtra(ToDoItem.STATUS, status.toString());
		intent.putExtra(ToDoItem.DATE, date);
	
	}

	public String toString() {
		return id + ITEM_SEP + title + ITEM_SEP + priority + ITEM_SEP + status + ITEM_SEP
				+ FORMAT.format(date);
	}

	public String toLog() {
		return "ID: " + id + ITEM_SEP + "Title:" + title + ITEM_SEP + "Priority:" + priority
				+ ITEM_SEP + "Status:" + status + ITEM_SEP + "Date:"
				+ FORMAT.format(date);
	}

}
