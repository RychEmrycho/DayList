/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.controller;

import com.jfoenix.controls.JFXCheckBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class TaskDataCellController implements Initializable {

    @FXML
    private HBox rootTaskData;
    
    @FXML
    private Label dateTask;
    
    @FXML
    private Label timeTask;
    
    @FXML
    private Label labelTask;

    @FXML
    private Label titleTask;
    
    @FXML
    private Label descriptionTask;
    
    @FXML
    private JFXCheckBox checkTask;

    private static DashboardContentController dashboardContentController;

    public static DashboardContentController getDashboardContentController() {
        return dashboardContentController;
    }

    public void setDashboardContentController(DashboardContentController dashboardContentController) {
        TaskDataCellController.dashboardContentController = dashboardContentController;
    }

    public HBox getRootTaskData() {
        return rootTaskData;
    }

    public Label getDateTask() {
        return dateTask;
    }

    public Label getTimeTask() {
        return timeTask;
    }

    public Label getLabelTask() {
        return labelTask;
    }

    public Label getTitleTask() {
        return titleTask;
    }

    public Label getDescriptionTask() {
        return descriptionTask;
    }

    public JFXCheckBox getCheckTask() {
        return checkTask;
    }

    public void setRootTaskData(HBox rootTaskData) {
        this.rootTaskData = rootTaskData;
    }

    public void setDateTask(Label dateTask) {
        this.dateTask = dateTask;
    }

    public void setTimeTask(Label timeTask) {
        this.timeTask = timeTask;
    }

    public void setLabelTask(Label labelTask) {
        this.labelTask = labelTask;
    }

    public void setTitleTask(Label titleTask) {
        this.titleTask = titleTask;
    }

    public void setDescriptionTask(Label descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public void setCheckTask(JFXCheckBox checkTask) {
        this.checkTask = checkTask;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
