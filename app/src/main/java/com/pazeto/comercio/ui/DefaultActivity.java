package com.pazeto.comercio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pazeto.comercio.R;
import com.pazeto.comercio.db.AuthHandler;

import java.util.Arrays;
import java.util.List;


public abstract class DefaultActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 63535;
    public final static String DATE_FORMAT_STRING = "dd/MM/yy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            new AuthHandler(getApplicationContext()).setLoggedUserID(auth.getCurrentUser().getUid());
        } else {
            launchLoginScreen();
        }
        super.onCreate(savedInstanceState);
    }

    void launchLoginScreen() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher)      // Set logo drawable
                        .setTheme(R.style.AppTheme)      // Set theme
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onNewClientBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, EditClientActivity.class), 1);
    }

    public void onNewStockedProductBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, StockListListActivity.class),
                1);
    }

    public void onNewProductBtn(View v) {
        startActivityForResult(new Intent(DefaultActivity.this, EditProductActivity.class),
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                new AuthHandler(getApplicationContext()).setLoggedUserID(user.getUid());

                // ...
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    //showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

//                showSnackbar(R.string.unknown_error);
//                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
}
