package com.daylist.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rychemrycho
 */
public class TaskDataModel {

    private int taskID;
    private Date taskDateCreated;
    private Time taskTimeCreated;
    private int taskPriority;
    private String taskTitle;
    private String taskDescription;
    private int taskComplete;
    private Timestamp taskTimestampCreated;
    private int taskLocation;

    public TaskDataModel(int taskID, Date taskDateCreated, Time taskTimeCreated, int taskPriority, String taskTitle, String taskDescription, int taskComplete, Timestamp taskTimestampCreated, int taskLocation) {
        this.taskID = taskID;
        this.taskDateCreated = taskDateCreated;
        this.taskTimeCreated = taskTimeCreated;
        this.taskPriority = taskPriority;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskComplete = taskComplete;
        this.taskTimestampCreated = taskTimestampCreated;
        this.taskLocation = taskLocation;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Date getTaskDateCreated() {
        return taskDateCreated;
    }

    public void setTaskDateCreated(Date taskDateCreated) {
        this.taskDateCreated = taskDateCreated;
    }

    public Time getTaskTimeCreated() {
        return taskTimeCreated;
    }

    public void setTaskTimeCreated(Time taskTimeCreated) {
        this.taskTimeCreated = taskTimeCreated;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskComplete() {
        return taskComplete;
    }

    public void setTaskComplete(int taskComplete) {
        this.taskComplete = taskComplete;
    }

    public Timestamp getTaskTimestampCreated() {
        return taskTimestampCreated;
    }

    public void setTaskTimestampCreated(Timestamp taskTimestampCreated) {
        this.taskTimestampCreated = taskTimestampCreated;
    }

    public int getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(int taskLocation) {
        this.taskLocation = taskLocation;
    }

}
