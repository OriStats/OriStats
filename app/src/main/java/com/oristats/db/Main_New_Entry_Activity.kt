package com.oristats.db

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.oristats.R

class Main_New_Entry_Activity : AppCompatActivity() {

    private lateinit var edit_start_time_view: EditText
    private lateinit var edit_tag_id_view: EditText
    private lateinit var edit_start_raw_id_view: EditText
    private lateinit var edit_end_raw_id_view: EditText
    private lateinit var edit_minus_one_day_view: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_main_new_entry_activity)
        edit_start_time_view = findViewById(R.id.edit_main_start_time)
        edit_tag_id_view = findViewById(R.id.edit_main_tag_id)
        edit_start_raw_id_view = findViewById(R.id.edit_main_start_raw_id)
        edit_end_raw_id_view = findViewById(R.id.edit_main_end_raw_id)
        edit_minus_one_day_view = findViewById(R.id.edit_main_minus_one_day)

        val button = findViewById<Button>(R.id.button_main_entry_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (
                ((!TextUtils.isEmpty(edit_start_time_view.text)) && TextUtils.isDigitsOnly(edit_start_time_view.text) && edit_start_time_view.text.toString().toLongOrNull()!=null) &&
                ((!TextUtils.isEmpty(edit_tag_id_view.text)) && TextUtils.isDigitsOnly(edit_tag_id_view.text) && edit_tag_id_view.text.toString().toIntOrNull()!=null) &&
                ((!TextUtils.isEmpty(edit_start_raw_id_view.text)) && TextUtils.isDigitsOnly(edit_start_raw_id_view.text) && edit_start_raw_id_view.text.toString().toIntOrNull()!=null) &&
                ((!TextUtils.isEmpty(edit_end_raw_id_view.text)) && TextUtils.isDigitsOnly(edit_end_raw_id_view.text) && edit_end_raw_id_view.text.toString().toIntOrNull()!=null)
            ){
                val start_time_string = edit_start_time_view.text.toString()
                val tag_id_string = edit_tag_id_view.text.toString()
                val start_raw_id_string = edit_start_raw_id_view.text.toString()
                val end_raw_id_string = edit_end_raw_id_view.text.toString()
                val minus_one_day_string = edit_minus_one_day_view.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_START_TIME, start_time_string)
                replyIntent.putExtra(EXTRA_REPLY_TAG_ID, tag_id_string)
                replyIntent.putExtra(EXTRA_REPLY_START_RAW_ID, start_raw_id_string)
                replyIntent.putExtra(EXTRA_REPLY_END_RAW_ID, end_raw_id_string)
                replyIntent.putExtra(EXTRA_REPLY_MINUS_ONE_DAY, minus_one_day_string)
                setResult(Activity.RESULT_OK, replyIntent)
            } else {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_START_TIME = "com.oristats.android.mainlistsql.REPLY_START_TIME"
        const val EXTRA_REPLY_TAG_ID = "com.oristats.android.mainlistsql.REPLY_TAG_ID"
        const val EXTRA_REPLY_START_RAW_ID = "com.oristats.android.mainlistsql.REPLY_START_RAW_ID"
        const val EXTRA_REPLY_END_RAW_ID = "com.oristats.android.mainlistsql.REPLY_END_RAW_ID"
        const val EXTRA_REPLY_MINUS_ONE_DAY = "com.oristats.android.mainlistsql.REPLY_MINUS_ONE_DAY"
    }
}