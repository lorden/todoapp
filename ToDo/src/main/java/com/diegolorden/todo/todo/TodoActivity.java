package com.diegolorden.todo.todo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoActivity extends ListActivity {

    ListView lvItems;
    ArrayList<TodoItem> items;
    ArrayAdapter<TodoItem> itemsAdapter;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvItems = (ListView) findViewById(android.R.id.list);
        lvItems.setTextFilterEnabled(true);
        lvItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        items = new ArrayList<TodoItem>();
        readItems();
        itemsAdapter = new ArrayAdapter<TodoItem>(this, android.R.layout.simple_list_item_checked, items);
        lvItems.setAdapter(itemsAdapter);
        updateItems();
        setupViewListener();
    }


    public void addTodoItem(View v) {

        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString().trim();
        if (itemText.length() > 0) {
            itemsAdapter.add(new TodoItem(itemText));
            etNewItem.setText("");
            updateItems();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        CheckedTextView item = (CheckedTextView)v;
        items.get(pos).checked = !items.get(pos).checked;
        updateItems();
    }


    public void setupViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent i = new Intent(TodoActivity.this, EditActivity.class);
                i.putExtra("item", items.get(pos).text);
                i.putExtra("position", pos);
                startActivityForResult(i, REQUEST_CODE);
                return true;
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
            ArrayList<String>itemsToSave = new ArrayList<String>();
            for (TodoItem i : items) {
                itemsToSave.add(i.toFileString());
            }
            FileUtils.writeLines(todoFile, itemsToSave);
        } catch (IOException e) {
            Log.e(getLocalClassName(), "unable to save items", e);
        }
    }

    private void readItems() {
        File todoFile = getFile();
        items = new ArrayList<TodoItem>();
        try {
            for(String s : FileUtils.readLines(todoFile)){
                TodoItem item = new TodoItem();
                item.fromFileString(s);
                items.add(item);
            }

        } catch (IOException e) {
            items = new ArrayList<TodoItem>();
            Log.w(getLocalClassName(), "unable to load items", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String item = data.getExtras().getString("item");
            int position = data.getExtras().getInt("position");
            if (item.equals("")){
               items.remove(position);
            } else {
                items.set(position, new TodoItem(item));
            }
            updateItems();
        }
    }

    public void updateItems() {
        int pos = 0;
        for(TodoItem i : items) {
            if (i.checked) {
                lvItems.setItemChecked(pos, true);
            }
            pos += 1;
        }
        itemsAdapter.notifyDataSetChanged();
        saveItems();
    }
}
