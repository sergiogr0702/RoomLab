package es.unex.giiis.asee.todomanager_db.database;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract() {}

    public static class TodoItem implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DATE = "date";

    }

}
