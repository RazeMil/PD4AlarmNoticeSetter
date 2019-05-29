package com.myapplicationdev.android.pd4_alarm_notice_setter;

import java.io.Serializable;

public class Task implements Serializable {

    private int id;
    private String task;
    private String description;

    public Task(int id, String task, String description) {
        this.id = id;
        this.task = task;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return id + " " + task + "\n" + description;
    }
}
