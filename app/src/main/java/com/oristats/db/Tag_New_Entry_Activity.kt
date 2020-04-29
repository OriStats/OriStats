package com.oristats.db

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.oristats.R

class Tag_New_Entry_Activity : AppCompatActivity() {

    private lateinit var edit_path_name_view: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_tag_new_entry_activity)
        edit_path_name_view = findViewById(R.id.edit_tag_path_name)

        val button = findViewById<Button>(R.id.button_tag_entry_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (!TextUtils.isEmpty(edit_path_name_view.text)) {
                val path_name_string = edit_path_name_view.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_PATH_NAME, path_name_string)
                setResult(Activity.RESULT_OK, replyIntent)
            } else {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_PATH_NAME = "com.oristats.android.taglistsql.REPLY_PATH_NAME"
    }
}