package com.diegolorden.todo.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends ActionBarActivity {

    ListView lvItems;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupViewListener();
    }

    public void addTodoItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        updateItems();
    }

    public void setupViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                items.remove(pos);
                updateItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent i = new Intent(TodoActivity.this, EditActivity.class);
                i.putExtra("item", items.get(pos));
                i.putExtra("position", pos);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    private File getFile() {
        File filesDir = getFilesDir();
        return new File(filesDir, "todo.txt");
    }

    private void saveItems() {
        File todoFile = getFile();
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            Log.e(getLocalClassName(), "unable to save items", e);
        }
    }

    private void readItems() {
        File todoFile = getFile();
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
            Log.i(getLocalClassName(), items.toString());
        } catch (IOException e) {
            items = new ArrayList<String>();
            Log.w(getLocalClassName(), "unable to load items", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String item = data.getExtras().getString("item");
            int position = data.getExtras().getInt("position");
            items.set(position, item);
            updateItems();
        }
    }

    public void updateItems() {
        itemsAdapter.notifyDataSetChanged();
        saveItems();
    }
}
