package es.unex.giiis.asee.todomanager_db;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Do not modify 

public class ToDoItem {

	public static final String ITEM_SEP = System.getProperty("line.separator");

	public enum Priority {
		LOW, MED, HIGH
	};

	public enum Status {
		NOTDONE, DONE
	};

	public final static String ID = "ID";
	public final static String TITLE = "title";
	public final static String PRIORITY = "priority";
	public final static String STATUS = "status";
	public final static String DATE = "date";
	public final static String FILENAME = "filename";

	public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.US);

	private long mID;
	private String mTitle = new String();
	private Priority mPriority = Priority.LOW;
	private Status   mStatus = Status.NOTDONE;
	private Date mDate = new Date();

	ToDoItem(String title, Priority priority, Status status, Date date) {
		this.mTitle = title;
		this.mPriority = priority;
		this.mStatus = status;
		this.mDate = date;
	}

//	ToDoItem(long ID, String title, Priority priority, Status status, Date date) {
//		this.mID = ID;
//		this.mTitle = title;
//		this.mPriority = priority;
//		this.mStatus = status;
//		this.mDate = date;
//	}

    public ToDoItem(long ID, String title, String priority, String status, String date) {
        this.mID = ID;
        this.mTitle = title;
        this.mPriority = Priority.valueOf(priority);
        this.mStatus = Status.valueOf(status);
        try {
            mDate = ToDoItem.FORMAT.parse(date);
        } catch (ParseException e) {
            mDate = new Date();
        }
    }

	// Create a new ToDoItem from data packaged in an Intent

	ToDoItem(Intent intent) {
		mID = intent.getLongExtra(ToDoItem.ID,0); //TODO think best default value for ID
		mTitle = intent.getStringExtra(ToDoItem.TITLE);
		mPriority = Priority.valueOf(intent.getStringExtra(ToDoItem.PRIORITY));
		mStatus = Status.valueOf(intent.getStringExtra(ToDoItem.STATUS));

		try {
			mDate = ToDoItem.FORMAT.parse(intent.getStringExtra(ToDoItem.DATE));
		} catch (ParseException e) {
			mDate = new Date();
		}
	}

    public long getID() { return mID; }

    public void setID(long ID) { this.mID = ID; }

    public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Priority getPriority() {
		return mPriority;
	}

	public void setPriority(Priority priority) {
		mPriority = priority;
	}

	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status status) {
		mStatus = status;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
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

	public static void packageIntent(long ID, Intent intent, String title,
									 Priority priority, Status status, String date) {

		intent.putExtra(ToDoItem.ID, ID);
		intent.putExtra(ToDoItem.TITLE, title);
		intent.putExtra(ToDoItem.PRIORITY, priority.toString());
		intent.putExtra(ToDoItem.STATUS, status.toString());
		intent.putExtra(ToDoItem.DATE, date);

	}

	public String toString() {
		return mID + ITEM_SEP + mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP
				+ FORMAT.format(mDate);
	}

	public String toLog() {
		return "ID: " + mID + ITEM_SEP + "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
				+ ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
				+ FORMAT.format(mDate);
	}

}
