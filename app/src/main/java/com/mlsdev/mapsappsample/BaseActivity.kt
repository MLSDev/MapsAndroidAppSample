package com.mlsdev.mapsappsample

import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    protected fun showBackButton(show: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(show)
            supportActionBar!!.setDisplayHomeAsUpEnabled(show)
        }
    }

    fun showAlertDialog(title: String?, message: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)

        builder.create().show()
    }

}
