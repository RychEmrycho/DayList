/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.controller;

import com.daylist.model.TaskDataModel;
import com.daylist.model.TaskDataListViewCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.effects.JFXDepthManager;
import com.sun.glass.ui.Screen;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class DashboardContentController implements Initializable {

    @FXML
    private VBox rightSides;

    @FXML
    private StackPane root;

    @FXML
    private VBox addTaskButtonContainer;

    @FXML
    private BorderPane labelContainer;

    @FXML
    private JFXButton addTaskButton;

    @FXML
    private JFXButton addTaskEmptyButton;

    @FXML
    private JFXButton refreshBtn;

    @FXML
    private JFXButton sortBtnAsc;

    @FXML
    private JFXButton sortBtnDesc;

    @FXML
    private JFXListView priorityList;

    @FXML
    private JFXListView<TaskDataModel> taskListViewContainer;

    private ObservableList<TaskDataModel> dataTaskObservableList;

    //FXMLLoader
    private FXMLLoader addTaskLoader;
    private FXMLLoader viewDetailsLoader;
    private FXMLLoader editTaskLoader;
    private FXMLLoader taskDataCellLoader;
//    private FXMLLoader dashboardLoader;

    //Loader Layout
    private VBox addTaskLayout;
    private BorderPane viewDetailsLayout;
    private VBox editTaskLayout;
    private JFXSnackbar snackbar;

    private TaskDataCellController taskDataCellController;
//    private DrawerContentController drawerContentController;

    //Dialog
    private JFXDialog dialog;

    public DashboardContentController() throws SQLException, ClassNotFoundException {
        dataTaskObservableList = FXCollections.observableArrayList();
        updateUI();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set Dashboard size
        double h = 600, w = 900;
        try {
            //Get main screen size
            h = Screen.getScreens().get(0).getHeight() / 1.35;
            w = Screen.getScreens().get(0).getVisibleWidth();
            rightSides.setPrefWidth(w * 0.2);
        } catch (Exception e) {

        }
        //Set list items
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());

        //Set depth of views
        JFXDepthManager.setDepth(addTaskButtonContainer, 1);
        JFXDepthManager.setDepth(labelContainer, 1);

        //Initialize snackbar
        snackbar = new JFXSnackbar(root);

        //Set datepicker
        DatePicker datePickers = new DatePicker(LocalDate.now());
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePickers);
        Node calendar = datePickerSkin.getPopupContent();
        rightSides.getChildren().add(1, calendar);

        //While datepicker clicked, show AddTask with datepicker date value
        datePickers.addEventHandler(EventType.ROOT, eventHandler -> {
            LocalDate dateValue = datePickers.getValue();
            addTask(dateValue);
        });

        //While item clicked, show viewDetails
        taskListViewContainer.getSelectionModel().selectedItemProperty().addListener((event) -> {
            if (taskListViewContainer.getSelectionModel().selectedItemProperty().getValue() != null) {
                try {
                    viewDetailsLoader = new FXMLLoader(getClass().getResource("/fxml/ViewDetails.fxml"));
                    viewDetailsLayout = (BorderPane) viewDetailsLoader.load();
                    ViewDetailsController viewDetailsController = viewDetailsLoader.getController();
                    viewDetailsController.setDashboardContentController(this);
                    viewDetailsController.setViewDetailsValue(taskListViewContainer.getSelectionModel().getSelectedItem());
                    dialog = new JFXDialog(root, viewDetailsLayout, JFXDialog.DialogTransition.CENTER);
                    dialog.show();
                    dialog.setOnDialogClosed(handler -> {
                        taskListViewContainer.getSelectionModel().clearSelection();
                    });
                } catch (IOException ex) {
                    Logger.getLogger(DashboardContentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //Select items by prority
        priorityList.getSelectionModel().selectedItemProperty().addListener((event) -> {
            switch (priorityList.getSelectionModel().getSelectedIndex()) {
                case 0:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    selectByPriority(0);
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 1:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    selectByPriority(1);
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 2:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    selectByPriority(2);
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 3:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    selectByPriority(3);
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 4:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    selectByPriority(4);
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                default:
                    System.out.println("Unknown priority index");
                    break;
            }
        });

        //Button Add Task while list empty
        addTaskEmptyButton.setOnAction(e -> {
            addTask(LocalDate.now());
        });

        //Button Add Task
        addTaskButton.setOnAction(e -> {
            addTask(LocalDate.now());
        });

        //Button refresh
        refreshBtn.setOnAction(e -> {
            taskListViewContainer.getItems().removeAll(dataTaskObservableList);
            updateUI();
            taskListViewContainer.setItems(dataTaskObservableList);
            taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
            priorityList.getSelectionModel().clearSelection();
            snackbar.enqueue(new SnackbarEvent("List refreshed"));
        });

        JFXListView<String> sortListAsc = new JFXListView<>();
        sortListAsc.getItems().add("Date created");
        sortListAsc.getItems().add("Time created");
        sortListAsc.getItems().add("Priority");

        JFXListView<String> sortListDesc = new JFXListView<>();
        sortListDesc.getItems().add("Date created");
        sortListDesc.getItems().add("Time created");
        sortListDesc.getItems().add("Priority");

        JFXPopup sortPopupAsc = new JFXPopup();
        sortPopupAsc.setPopupContent(sortListAsc);  
        JFXPopup sortPopupDesc = new JFXPopup();
        sortPopupDesc.setPopupContent(sortListDesc);

        //Button sortAsc
        sortBtnAsc.setOnAction(e -> {
            sortPopupAsc.show(sortBtnAsc, PopupVPosition.TOP, PopupHPosition.RIGHT);
        });

        //Button sortDesc
        sortBtnDesc.setOnAction(e -> {
            sortPopupDesc.show(sortBtnDesc, PopupVPosition.TOP, PopupHPosition.LEFT);
        });

        sortListAsc.getSelectionModel().selectedItemProperty().addListener(e -> {
            switch (sortListAsc.getSelectionModel().getSelectedIndex()) {
                case 0:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("DATECREATED", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 1:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("TIMECREATED", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 2:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("PRIORITY", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                default:
                    System.out.println("Unknow Pop Up index");
                    break;
            }
        });

        sortListDesc.getSelectionModel().selectedItemProperty().addListener(e -> {
            switch (sortListDesc.getSelectionModel().getSelectedIndex()) {
                case 0:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("DATECREATED", "DESC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 1:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("TIMECREATED", "DESC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                case 2:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("PRIORITY", "DESC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
                    break;
                default:
                    System.out.println("Unknow PopUp index");
                    break;
            }
        });

        taskDataCellLoader = new FXMLLoader(getClass().getResource("/fxml/TaskDataCell.fxml"));
//        dashboardLoader = new FXMLLoader(getClass().getResource("/fxml/DrawerContent.fxml"));
        try {
            taskDataCellLoader.load();
            taskDataCellController = taskDataCellLoader.getController();
            taskDataCellController.setDashboardContentController(this);

//            dashboardLoader.load();
//            drawerContentController = dashboardLoader.getController();
//            drawerContentController.setDashboardContentController(this);
        } catch (IOException ex) {
            Logger.getLogger(DashboardContentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTask(LocalDate dateValue) {
        try {
            addTaskLoader = new FXMLLoader(getClass().getResource("/fxml/AddTask.fxml"));
            addTaskLayout = (VBox) addTaskLoader.load();
            AddTaskController addTaskController = addTaskLoader.getController();
            addTaskController.setDashboardContentController(this);
            addTaskController.setDate(dateValue);
        } catch (IOException ex) {
            Logger.getLogger(DashboardContentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        dialog = new JFXDialog(root, addTaskLayout, JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }

    public void updateList() {
        if (dialog.isVisible()) {
            dialog.close();
        }
        taskListViewContainer.getItems().removeAll(dataTaskObservableList);
        updateUI();
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
        snackbar.enqueue(new SnackbarEvent("List updated"));
    }

    public void updateIsComplete() {
        taskListViewContainer.getItems().removeAll(dataTaskObservableList);
        updateUI();
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new TaskDataListViewCell());
        snackbar.enqueue(new SnackbarEvent("Item moved to complete"));
    }

    public void closeDialog() {
        dialog.close();
    }

    public void editTask(LocalDate dateValue, TaskDataModel items, int controllerID) {
        try {
            editTaskLoader = new FXMLLoader(getClass().getResource("/fxml/EditTask.fxml"));
            editTaskLayout = (VBox) editTaskLoader.load();
            EditTaskController editTaskController = editTaskLoader.getController();
            editTaskController.setDashboardContentController(this);
            editTaskController.setEditTaskValue(items, 0);
        } catch (IOException ex) {
            Logger.getLogger(DashboardContentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        dialog = new JFXDialog(root, editTaskLayout, JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }

    public void updateUI() {
        try {
            Class.forName("org.h2.Driver");
            try {
                StringConverter<LocalTime> timeConverter = new StringConverter<LocalTime>() {

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

                    public String toString(LocalTime time) {
                        if (time != null) {
                            return timeFormatter.format(time);
                        } else {
                            return "";
                        }
                    }

                    public LocalTime fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalTime.parse(string, timeFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    @Override
                    public String toString(LocalDate date) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                Date checkDate = Date.valueOf(dateConverter.toString(LocalDate.now()));
                ResultSet rs = stmt.executeQuery("SELECT * FROM TaskDayListDB WHERE DATECREATED >= '" + checkDate + "' AND TASKLOCATION = 0 AND ISCOMPLETE = 0 ORDER BY DATECREATED ASC, TIMECREATED ASC, TIMESTAMPCREATED DESC");
                while (rs.next()) {
                    int taskID = rs.getInt(1);
                    Date dateCreated = rs.getDate(2);
                    Time timeCreated = rs.getTime(3);
                    int priority = rs.getInt(4);
                    String taskTitle = rs.getString(5);
                    String taskDescription = rs.getString(6);
                    int taskComplete = rs.getInt(7);
                    Timestamp taskTimestampCreated = rs.getTimestamp(8);
                    int taskLocation = rs.getInt(9);
                    //System.out.println(dateCreated + " " + timeCreated  +" "+ priority +" "+ taskTitle +" "+ taskDescription +" "+ isComplete + " " + timestamp);
                    dataTaskObservableList.add(new TaskDataModel(taskID, dateCreated, timeCreated, priority, taskTitle, taskDescription, taskComplete, taskTimestampCreated, taskLocation));
                }
                rs.close();
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

    public void setSnackbar(String message) {
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(message));
    }

    public void sortBy(String sortParam, String sortMode) {
        try {
            Class.forName("org.h2.Driver");
            try {
                StringConverter<LocalTime> timeConverter = new StringConverter<LocalTime>() {

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

                    public String toString(LocalTime time) {
                        if (time != null) {
                            return timeFormatter.format(time);
                        } else {
                            return "";
                        }
                    }

                    public LocalTime fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalTime.parse(string, timeFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    @Override
                    public String toString(LocalDate date) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                Date checkDate = Date.valueOf(dateConverter.toString(LocalDate.now()));
                ResultSet rs = stmt.executeQuery("SELECT * FROM TaskDayListDB WHERE DATECREATED >= '" + checkDate + "' AND TASKLOCATION = 0 AND ISCOMPLETE = 0 ORDER BY " + sortParam + " " + sortMode);
                while (rs.next()) {
                    int taskID = rs.getInt(1);
                    Date dateCreated = rs.getDate(2);
                    Time timeCreated = rs.getTime(3);
                    int priority = rs.getInt(4);
                    String taskTitle = rs.getString(5);
                    String taskDescription = rs.getString(6);
                    int taskComplete = rs.getInt(7);
                    Timestamp taskTimestampCreated = rs.getTimestamp(8);
                    int taskLocation = rs.getInt(9);

                    //System.out.println(dateCreated + " " + timeCreated  +" "+ priority +" "+ taskTitle +" "+ taskDescription +" "+ isComplete + " " + timestamp);
                    dataTaskObservableList.add(new TaskDataModel(taskID, dateCreated, timeCreated, priority, taskTitle, taskDescription, taskComplete, taskTimestampCreated, taskLocation));
                }
                rs.close();
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

    public void selectByPriority(int selectedPriority) {
        try {
            Class.forName("org.h2.Driver");
            try {
                StringConverter<LocalTime> timeConverter = new StringConverter<LocalTime>() {

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

                    public String toString(LocalTime time) {
                        if (time != null) {
                            return timeFormatter.format(time);
                        } else {
                            return "";
                        }
                    }

                    public LocalTime fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalTime.parse(string, timeFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {

                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    @Override
                    public String toString(LocalDate date) {
                        if (date != null) {
                            return dateFormatter.format(date);
                        } else {
                            return "";
                        }
                    }

                    @Override
                    public LocalDate fromString(String string) {
                        if (string != null && !string.isEmpty()) {
                            return LocalDate.parse(string, dateFormatter);
                        } else {
                            return null;
                        }
                    }
                };

                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                Date checkDate = Date.valueOf(dateConverter.toString(LocalDate.now()));
                ResultSet rs = stmt.executeQuery("SELECT * FROM TaskDayListDB WHERE DATECREATED >= '" + checkDate + "' AND TASKLOCATION = 0 AND ISCOMPLETE = 0 AND PRIORITY = " + selectedPriority + " ORDER BY DATECREATED ASC, TIMECREATED ASC, TIMESTAMPCREATED DESC");
                while (rs.next()) {
                    int taskID = rs.getInt(1);
                    Date dateCreated = rs.getDate(2);
                    Time timeCreated = rs.getTime(3);
                    int priority = rs.getInt(4);
                    String taskTitle = rs.getString(5);
                    String taskDescription = rs.getString(6);
                    int taskComplete = rs.getInt(7);
                    Timestamp taskTimestampCreated = rs.getTimestamp(8);
                    int taskLocation = rs.getInt(9);
                    //System.out.println(dateCreated + " " + timeCreated  +" "+ priority +" "+ taskTitle +" "+ taskDescription +" "+ isComplete + " " + timestamp);
                    dataTaskObservableList.add(new TaskDataModel(taskID, dateCreated, timeCreated, priority, taskTitle, taskDescription, taskComplete, taskTimestampCreated, taskLocation));
                }
                rs.close();
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
