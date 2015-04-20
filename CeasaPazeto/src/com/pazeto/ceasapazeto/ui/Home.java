package com.pazeto.ceasapazeto.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pazeto.ceasapazeto.R;
import com.pazeto.ceasapazeto.db.DBFacade;

public class Home extends Activity {

	Button btCadProd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		DBFacade db = new DBFacade(getApplicationContext());
	
		btCadProd = (Button) findViewById(R.id.ca);
		
		btCadProd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

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
