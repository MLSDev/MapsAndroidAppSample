package com.mlsdev.mapsappsample;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    protected void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(show);
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    public void showAlertDialog(@Nullable String title, @NonNull String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);

        builder.create().show();
    }

}
