package com.diegolorden.todo.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class EditActivity extends ActionBarActivity {

    EditText etEditItem;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(getIntent().getStringExtra("item"));
        position = getIntent().getIntExtra("position", 0);
        etEditItem.setSelection(etEditItem.length());
        etEditItem.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    public void saveItem(View v) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = new Intent();
        intent.putExtra("item", etEditItem.getText().toString());
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void removeItem(View v) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = new Intent();
        intent.putExtra("item", "");
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
