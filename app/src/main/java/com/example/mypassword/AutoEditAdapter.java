package com.example.mypassword;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AutoEditAdapter extends CursorAdapter implements Filterable {
    private Context context;

    private SQLiteDatabase db;


    public AutoEditAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.auto_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txt = view.findViewById(R.id.xs);
        txt.setText(cursor.getString(0));
        //cursor.close();
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(0);
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (constraint != null) {
            String sql = "select DISTINCT bz1 ,_id from date where bz1 like \'%" + constraint.toString() + "%\'";
            if (db == null) {
                db = new DatabaseHelper(context, "p.db", null, 1).getWritableDatabase();
            }
            Cursor cursor = db.rawQuery(sql, null);
            Log.d("MyAuto", "runQueryOnBackgroundThread: " + cursor.getCount());
            return cursor;
        } else {
            return null;
        }


    }
}
