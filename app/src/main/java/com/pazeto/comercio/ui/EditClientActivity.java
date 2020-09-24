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
import com.pazeto.comercio.utils.Constants;
import com.pazeto.comercio.vo.Client;

public class EditClientActivity extends DefaultActivity {

	protected static final String TAG = EditClientActivity.class.getName();
	EditText etName, etLastName, etTel, etCellPhone, etEmail, etAddress, etCity;
	FloatingActionButton fltBtnSave;

	Client currentClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_client);
		etName = findViewById(R.id.etClientName);
		etLastName = findViewById(R.id.etClientLastName);
		etTel = findViewById(R.id.etClientTel);
		etCellPhone =  findViewById(R.id.etClientCellphone);
		etEmail = findViewById(R.id.etClientEmail);
		etCity = findViewById(R.id.etClientCity);
		etAddress = findViewById(R.id.etClientAddress);

		fltBtnSave = findViewById(R.id.saveButton);
		fltBtnSave.setOnClickListener(btnSaveClickListener);

		String idClient = getIntent().getStringExtra(Constants.INTENT_EXTRA_CLIENT);
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
			etTel.setText(currentClient.getTelephone());
			etCellPhone.setText(currentClient.getCellPhone());
			etCity.setText(currentClient.getCity());
			etAddress.setText(currentClient.getAddress());
			etEmail.setText(currentClient.getEmail());
		}
	}

	View.OnClickListener btnSaveClickListener = v -> persistClient();

	private void persistClient() {
		currentClient.setName(etName.getText().toString());
		currentClient.setLastname(etLastName.getText().toString());
		currentClient.setCity(etCity.getText().toString());
		currentClient.setAddress(etAddress.getText().toString());
		currentClient.setTelephone(etTel.getText().toString());
		currentClient.setCellPhone(etCellPhone.getText().toString());
		currentClient.setEmail(etEmail.getText().toString());

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
			DocumentReference doc = collectionRef.document();
			client.setId(doc.getId());
			ref = doc.set(client);
		}
		ref.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Intent intent = new Intent();
				intent.putExtra(Constants.INTENT_EXTRA_CLIENT, client);
				setResult(RESULT_OK, intent);
				finish();
			}

		})
		.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e(TAG, "Client not saved.", e);
				fltBtnSave.setEnabled(true);
			}
		});
	}

	private void getClient(String clientId) {
		DocumentReference docRef = new FirebaseHandler(getApplicationContext()).getClientsCollectionReference().document(clientId);
		docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				if(documentSnapshot.exists()) {
					Client client = documentSnapshot.toObject(Client.class);
					client.setId(documentSnapshot.getId());

					loadClient(client);
				} else {
					Log.e(TAG, "Client "+ clientId + " not exists");
				}
			}
		});
	}
}
