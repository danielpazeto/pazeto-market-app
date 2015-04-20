package com.pazeto.ceasapazeto.ui;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class listaClientes extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Entrou","Activity listaclientes");
		setContentView(R.layout.list_clientes);
		DBFacade db = new DBFacade(this);
		ListView clientListView = (ListView) findViewById(R.id.listViewClientes);
		Cursor c = db.listCLientes();
		Log.d("Colum names", ""+c.getColumnIndex("name")+ c.getColumnIndex("id"));
		CustomAdapter adapter = new CustomAdapter(this, c);
		clientListView.setAdapter(adapter);
		
	}


}
