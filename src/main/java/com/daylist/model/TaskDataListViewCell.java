/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.model;

import com.daylist.controller.TaskDataCellController;
import com.jfoenix.controls.JFXListCell;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author rychemrycho
 */
public class TaskDataListViewCell extends JFXListCell<TaskDataModel> {

    private FXMLLoader mLoader;
    private TaskDataCellController taskDataController;

    @Override
    protected void updateItem(TaskDataModel item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource("/fxml/TaskDataCell.fxml"));

                try {
                    mLoader.load();
                    taskDataController = mLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            taskDataController.getDateTask().setText(String.valueOf(item.getTaskDateCreated()));
            taskDataController.getTimeTask().setText(String.valueOf(item.getTaskTimeCreated()));

            switch (item.getTaskPriority()) {
                case 0:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:grey;");
                    break;
                case 1:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:green;");
                    break;
                case 2:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:yellow;");
                    break;
                case 3:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:orange;");
                    break;
                case 4:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:red;");
                    break;
                default:
                    taskDataController.getLabelTask().getGraphic().setStyle("-fx-fill:grey;");
                    break;
            }

            taskDataController.getTitleTask().setText(String.valueOf(item.getTaskTitle()));
            taskDataController.getDescriptionTask().setText(String.valueOf(item.getTaskDescription()));

            if (item.getTaskComplete() == 0) {
                taskDataController.getCheckTask().setSelected(false);
            } else if (item.getTaskComplete() == 1) {
                taskDataController.getCheckTask().setSelected(true);
            }

            taskDataController.getCheckTask().setOnAction(e -> {
                if (taskDataController.getCheckTask().isSelected() && item.getTaskComplete() != 1) {
                    updateItemStatus(item.getTaskID(), 1);
                    TaskDataCellController.getDashboardContentController().updateIsComplete();
                } else {
                    updateItemStatus(item.getTaskID(), 0);
                    TaskDataCellController.getDashboardContentController().updateIsComplete();
                }
            });

            setText(null);
            setGraphic(taskDataController.getRootTaskData());
        }
    }

    public void updateItemStatus(int id, int isComplete) {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE TaskDayListDB SET ISCOMPLETE = " + isComplete + " WHERE ID = " + id);
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("SQLExeption: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("Vendor Error: " + e.getErrorCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
