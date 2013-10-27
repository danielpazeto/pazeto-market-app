package com.pazeto.ceasapazeto.ui;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;

public class Home extends Activity {

	Button btVendas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		DBFacade db = new DBFacade(getApplicationContext());
		listaClientes();
		//db.insertClientes();
	
	
	}

	
	public void listaClientes() {
		btVendas = (Button) findViewById(R.id.button1);
		
		btVendas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//DBFacade db = new DBFacade(getApplicationContext());
				
				Intent i = new Intent(Home.this, listaClientes.class);
				startActivity(i);
				
			}
		});
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
