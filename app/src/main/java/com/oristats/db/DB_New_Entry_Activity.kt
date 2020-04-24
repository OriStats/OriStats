package com.oristats.db

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.oristats.R

class DB_New_Entry_Activity : AppCompatActivity() {

    private lateinit var editMillisView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db_new_entry)
        editMillisView = findViewById(R.id.edit_db_entry)

        val button = findViewById<Button>(R.id.button_db_entry_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if ((!TextUtils.isEmpty(editMillisView.text)) && TextUtils.isDigitsOnly(editMillisView.text) && editMillisView.text.toString().toLongOrNull()!=null) {
                val millis_string = editMillisView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, millis_string)
                setResult(Activity.RESULT_OK, replyIntent)
            } else {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.oristats.android.rawlistsql.REPLY"
    }
}
