package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pazeto.comercio.R;
import com.pazeto.comercio.db.FirebaseHandler;
import com.pazeto.comercio.vo.Client;

public class EditClientActivity extends DefaultActivity {

	protected static final String TAG = EditClientActivity.class.getName();
	EditText etName, etLastName, etTel, etCellPhone1, etCellPhone2, etAddres,
			etHangar, etCity, etDescription;
	FloatingActionButton fltBtnSave;

	public static final String ID_EXTRA = "_id";

	Client currentClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_client);
		etName = (EditText) findViewById(R.id.etClientName);
		etLastName = (EditText) findViewById(R.id.etClientLastName);
		etHangar = (EditText) findViewById(R.id.etClientHangar);
		etTel = (EditText) findViewById(R.id.etClientTel);
		etCellPhone1 = (EditText) findViewById(R.id.etClientCellphone1);
		etCellPhone2 = (EditText) findViewById(R.id.etClientCellphone2);
		etAddres = (EditText) findViewById(R.id.etClientAddress);
		etCity = (EditText) findViewById(R.id.etClientCity);

		fltBtnSave = findViewById(R.id.saveButton);
		fltBtnSave.setOnClickListener(btnSaveClickListener);

		String idClient = getIntent().getStringExtra(ID_EXTRA);
		if (idClient != null) {
			getClient(idClient);
		}else{
			currentClient = new Client();
		}
	}

	private void loadClient(Client client) {
		currentClient = client;
		if (client != null) {
			etName.setText(currentClient.getName());
			etLastName.setText(currentClient.getLastname());
			// TODO colocar todos os campos
		}
	}

	View.OnClickListener btnSaveClickListener = v -> persistClient();

	private void persistClient() {
		currentClient.setName(etName.getText().toString());
		currentClient.setLastname(etLastName.getText().toString());
		currentClient.setCity(etCity.getText().toString());
		currentClient.setTelephone(etTel.getText().toString());
		currentClient.setCellPhone1(etCellPhone1.getText().toString());
		currentClient.setCellPhone2(etCellPhone2.getText().toString());
		// TODO falta outros atributos aki
		saveClient(currentClient);

	}

	public void saveClient(Client client) {
		Log.d(TAG, "Saving client");
		fltBtnSave.setEnabled(false);
		CollectionReference collectionRef = new FirebaseHandler(getApplicationContext()).getClientsCollectionReference();
		Task<Void> ref;
		if (client.getId() != null){
			ref = collectionRef.document(client.getId()).set(client);
		} else {
			ref = collectionRef.document().set(client);
		}
		ref.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.d(TAG, "DocumentSnapshot added ");

				Intent intent = new Intent();
				intent.putExtra(EditClientActivity.ID_EXTRA, client);
				setResult(RESULT_OK, intent);
				finish();
			}

		})
		.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d(TAG, "N�o salvou cliente.");
				fltBtnSave.setEnabled(true);
			}
		});
	}

	public void getClient(String clientId) {
		DocumentReference docRef = new FirebaseHandler(getApplicationContext()).getClientsCollectionReference().document(clientId);
		docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if(documentSnapshot.exists()) {
					Client client = documentSnapshot.toObject(Client.class);
					client.setId(documentSnapshot.getId());

					Log.d(TAG, client.getName());
					loadClient(client);
				} else {
					Log.d(TAG, "Not exsits");
				}
			}
		});
	}
}
