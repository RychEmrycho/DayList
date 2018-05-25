/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.controller;

import com.daylist.model.TaskDataModel;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class ViewDetailsController implements Initializable {

    @FXML
    private Label taskDate;
    @FXML
    private Label taskTime;
    @FXML
    private Label taskPriority;
    @FXML
    private Label taskStatus;

    @FXML
    private JFXButton deleteBtn;
    @FXML
    private JFXButton editBtn;
    @FXML
    private JFXButton closeDialogBtn;
    @FXML
    private Label taskTitle;
    @FXML
    private Label taskDescription;

    private DashboardContentController dashboardContentController;

    private TaskDataModel taskDataModel;

    public void setDashboardContentController(DashboardContentController dashboardContentController) {
        this.dashboardContentController = dashboardContentController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteBtn.setOnAction(e -> {
            dashboardContentController.closeDialog();
            deleteItem(taskDataModel.getTaskID(), 1);
            dashboardContentController.updateList();
            dashboardContentController.setSnackbar("Item has moved to trash");
        });

        editBtn.setOnAction(e -> {
            dashboardContentController.closeDialog();
            dashboardContentController.editTask(LocalDate.now(), taskDataModel, 0);
        });

        closeDialogBtn.setOnAction(e -> {
            dashboardContentController.closeDialog();
        });
    }

    public void setViewDetailsValue(TaskDataModel taskDataModel) {
        this.taskDataModel = taskDataModel;
        this.taskDate.setText(taskDataModel.getTaskDateCreated().toString());
        this.taskTime.setText(this.taskDataModel.getTaskTimeCreated().toString());
        switch (taskDataModel.getTaskPriority()) {
            case 0:
                this.taskPriority.getGraphic().setStyle("-fx-fill:grey;");
                this.taskPriority.setText("Optional");
                break;
            case 1:
                this.taskPriority.getGraphic().setStyle("-fx-fill:green;");
                this.taskPriority.setText("Normal");
                break;
            case 2:
                this.taskPriority.getGraphic().setStyle("-fx-fill:yellow;");
                this.taskPriority.setText("Medium");
                break;
            case 3:
                this.taskPriority.getGraphic().setStyle("-fx-fill:orange;");
                this.taskPriority.setText("Important");
                break;
            case 4:
                this.taskPriority.getGraphic().setStyle("-fx-fill:red;");
                this.taskPriority.setText("Emergency");
                break;
            default:
                this.taskPriority.getGraphic().setStyle("-fx-fill:grey;");
                this.taskPriority.setText("Optional");
                break;
        }
        if (taskDataModel.getTaskComplete() == 0) {
            this.taskStatus.setText("Not yet complete");
        } else {
            this.taskStatus.setText("Completed");
        }
        this.taskTitle.setText(taskDataModel.getTaskTitle());
        this.taskDescription.setText(taskDataModel.getTaskDescription());
    }

    public void deleteItem(int taskID, int taskLocation) {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE TaskDayListDB set TASKLOCATION = " + taskLocation + " WHERE ID = " + taskID);
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
