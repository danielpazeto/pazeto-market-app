package com.pazeto.comercio.db;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHandler {

    private enum ReferenceName {
        USERS("users"),
        CLIENTS("clients"),
        PRODUCTS("products"),
        SALESTOCKS("sales_stocks");

        public String name;

        private ReferenceName(String name) {
            this.name = name;
        }

    }

    Context context;

    public FirebaseHandler(Context context) {
        this.context = context;
    }

    private DocumentReference getDatabase() {
        AuthHandler auth = new AuthHandler(this.context);
        return FirebaseFirestore.getInstance().collection(ReferenceName.USERS.name).document(auth.getLoggedUserID());
    }

    public CollectionReference getSaleStocksCollectionReference() {
        return getDatabase().collection(ReferenceName.SALESTOCKS.name);
    }

    public CollectionReference getProductsCollectionReference() {
        return getDatabase().collection(ReferenceName.PRODUCTS.name);
    }

    public CollectionReference getClientsCollectionReference() {
        return getDatabase().collection(ReferenceName.CLIENTS.name);
    }
}
