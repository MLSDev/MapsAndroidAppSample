package com.mlsdev.mapsappsample;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

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

}
