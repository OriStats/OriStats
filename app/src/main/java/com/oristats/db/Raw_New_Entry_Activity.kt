package com.oristats.db

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.oristats.R

class Raw_New_Entry_Activity : AppCompatActivity() {

    private lateinit var edit_millis_view: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_raw_new_entry_activity)
        edit_millis_view = findViewById(R.id.edit_raw_millis)

        val button = findViewById<Button>(R.id.button_raw_entry_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if ((!TextUtils.isEmpty(edit_millis_view.text)) && TextUtils.isDigitsOnly(edit_millis_view.text) && edit_millis_view.text.toString().toLongOrNull()!=null) {
                val millis_string = edit_millis_view.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_MILLIS, millis_string)
                setResult(Activity.RESULT_OK, replyIntent)
            } else {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_MILLIS = "com.oristats.android.rawlistsql.REPLY_MILLIS"
    }
}