package com.diegolorden.todo.todo;

public class TodoItem {
    String text;
    Boolean checked;

    public TodoItem() {}

    public TodoItem(String text) {
        this.text = text;
        this.checked = false;
    }

    public String toString(){
        return this.text;
    }

    public String toFileString() {
        return text + "," + checked.toString();
    }

    public void fromFileString(String s){
        String[] parts = s.split(",");
        this.text = parts[0];
        this.checked = Boolean.parseBoolean(parts[1]);
    }

}
