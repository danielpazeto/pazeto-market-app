package com.pazeto.ceasapazeto.ui;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.ClienteDB;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomAdapter  extends CursorAdapter {
	// CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)

    
	public CustomAdapter(Context context, Cursor c) {
        super(context, c);
        
    }

    public void bindView(View view, Context context, Cursor cursor) {
    	String id = cursor.getString(0);
        String teste = cursor.getString(1);
        String name = cursor.getString(cursor.getColumnIndex(ClienteDB.NAME));
        String phone1 = cursor.getString(cursor.getColumnIndex(ClienteDB.PHONE1));
        //String hangar = cursor.getString(cursor.getColumnIndex(ClienteDB.HANGAR));

        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvPhone1 = (TextView) view.findViewById(R.id.phone1);
        //TextView tvHangar = (TextView) view.findViewById(R.id.hangar);
        Log.d("view",""+view.findViewById(R.id.name));
        
        tvName.setText("88"); // erro aki find view by id é nulo ://
        tvPhone1.setText("89");
        //tvHangar.setText(hangar);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate your view here.
        TextView view = new TextView(context);
        return view;
    }

	

}
