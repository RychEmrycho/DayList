package com.daylist.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.daylist.model.TaskDataModel;
import com.daylist.model.MiniTaskDataListViewCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXSnackbar;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class ArchivesController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private Label taskDate;

    @FXML
    private Label taskTime;

    @FXML
    private Label taskPriority;

    @FXML
    private Label taskStatus;

    @FXML
    private Label taskTitle;

    @FXML
    private Label taskDescription;

    @FXML
    private JFXButton refreshBtn;

    @FXML
    private JFXButton editBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private JFXButton sortBtnAsc;

    @FXML
    private JFXButton sortBtnDesc;

    @FXML
    private JFXListView<TaskDataModel> taskListViewContainer;

    private JFXSnackbar snackbar;

    private ObservableList<TaskDataModel> dataTaskObservableList;

    //FXMLLoader
    private FXMLLoader editTaskLoader;
    private FXMLLoader miniTaskDataCellLoader;
//    private FXMLLoader dashboardLoader;

    //Loader Layout
    private VBox editTaskLayout;

    //Dialog
    private JFXDialog dialog;

    //Controller
    private MiniTaskDataCellController miniTaskDataCellController;
//    private DashboardController dashboardController;

    public ArchivesController() throws SQLException, ClassNotFoundException {
        dataTaskObservableList = FXCollections.observableArrayList();
        updateUI();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());

        selectFirstItem();

        taskListViewContainer.getSelectionModel().selectedItemProperty().addListener(evt -> {
            if (!taskListViewContainer.getItems().isEmpty()) {
                updateDetails();
            } else {
                resetUI();
            }
        });

        //Initialize snackbar
        snackbar = new JFXSnackbar(root);

        refreshBtn.setOnAction(e -> {
            taskListViewContainer.getItems().removeAll(dataTaskObservableList);
            updateUI();
            resetUI();
            taskListViewContainer.setItems(dataTaskObservableList);
            taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
            selectFirstItem();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("List refreshed"));
        });

        //Button Move to trash
        deleteBtn.setOnAction(e -> {
            deleteItem(taskListViewContainer.getSelectionModel().getSelectedItem().getTaskID(), 1);
            taskListViewContainer.getItems().removeAll(dataTaskObservableList);
            updateUI();
            taskListViewContainer.setItems(dataTaskObservableList);
            taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
            selectFirstItem();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent("Item has moved to trash"));
        });

        //Edit Button
        editBtn.setOnAction(e -> {
            editTask(LocalDate.now(), taskListViewContainer.getSelectionModel().getSelectedItem());
        });

        JFXListView<String> sortList = new JFXListView<>();
        sortList.getItems().add("Date created");
        sortList.getItems().add("Time created");
        sortList.getItems().add("Priority");

        JFXPopup sortPopup = new JFXPopup();
        sortPopup.setPopupContent(sortList);

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
            sortPopupAsc.show(sortBtnAsc, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
        });

        //Button sortDesc
        sortBtnDesc.setOnAction(e -> {
            sortPopupDesc.show(sortBtnDesc, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
        });

        sortListAsc.getSelectionModel().selectedItemProperty().addListener(e -> {
            switch (sortListAsc.getSelectionModel().getSelectedIndex()) {
                case 0:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("DATECREATED", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
                    break;
                case 1:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("TIMECREATED", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
                    break;
                case 2:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("PRIORITY", "ASC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
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
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
                    break;
                case 1:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("TIMECREATED", "DESC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
                    break;
                case 2:
                    taskListViewContainer.getItems().removeAll(dataTaskObservableList);
                    sortBy("PRIORITY", "DESC");
                    taskListViewContainer.setItems(dataTaskObservableList);
                    taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
                    break;
                default:
                    System.out.println("Unknow PopUp index");
                    break;
            }
        });

        miniTaskDataCellLoader = new FXMLLoader(getClass().getResource("/fxml/MiniTaskDataCell.fxml"));
//        dashboardLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        try {
            miniTaskDataCellLoader.load();
            miniTaskDataCellController = miniTaskDataCellLoader.getController();
            miniTaskDataCellController.setArchivesController(this);
            //
//            dashboardLoader.load();
//            dashboardController = dashboardLoader.getController();
//            dashboardController.setArchivesController(this);
//            miniTaskDataCellController.setArchivesController(this);
        } catch (IOException ex) {
            Logger.getLogger(ArchivesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectFirstItem() {
        if (!taskListViewContainer.getItems().isEmpty()) {
            taskListViewContainer.getSelectionModel().selectFirst();
            updateDetails();
        } else {
            resetUI();
        }
    }

    public void updateListItem() {
        if (dialog.isVisible()) {
            dialog.close();
        }
        taskListViewContainer.getItems().removeAll(dataTaskObservableList);
        updateUI();
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
    }

    public void updateList() {
        taskListViewContainer.getItems().removeAll(dataTaskObservableList);
        updateUI();
        taskListViewContainer.setItems(dataTaskObservableList);
        taskListViewContainer.setCellFactory(dataTaskListView -> new MiniTaskDataListViewCell());
        selectFirstItem();
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent("List refreshed"));
    }

    public void updateDetails() {
        taskDate.setText(taskListViewContainer.getSelectionModel().getSelectedItem().getTaskDateCreated().toString());
        taskTime.setText(taskListViewContainer.getSelectionModel().getSelectedItem().getTaskTimeCreated().toString());
        switch (taskListViewContainer.getSelectionModel().getSelectedItem().getTaskPriority()) {
            case 0:
                taskPriority.getGraphic().setStyle("-fx-fill:grey;");
                taskPriority.setText("Normal");
                break;
            case 1:
                taskPriority.getGraphic().setStyle("-fx-fill:blue;");
                taskPriority.setText("Medium");
                break;
            case 2:
                taskPriority.getGraphic().setStyle("-fx-fill:green;");
                taskPriority.setText("Important");
                break;
            case 3:
                taskPriority.getGraphic().setStyle("-fx-fill:red;");
                taskPriority.setText("Emergency");
                break;
            case 4:
                taskPriority.getGraphic().setStyle("-fx-fill:yellow;");
                taskPriority.setText("Fast");
                break;
            default:
                taskPriority.getGraphic().setStyle("-fx-fill:grey;");
                taskPriority.setText("Normal");
                break;
        }

        if (taskListViewContainer.getSelectionModel().getSelectedItem().getTaskComplete() == 0) {
            taskStatus.setText("Not yet complete");
        } else {
            taskStatus.setText("Completed");
        }
        taskTitle.setText(taskListViewContainer.getSelectionModel().getSelectedItem().getTaskTitle());
        taskDescription.setText(taskListViewContainer.getSelectionModel().getSelectedItem().getTaskDescription());
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }

    private void updateUI() {
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
                ResultSet rs = stmt.executeQuery("SELECT * FROM TaskDayListDB WHERE DATECREATED < '" + checkDate + "' AND TASKLOCATION = 0 ORDER BY DATECREATED ASC, TIMECREATED ASC, TIMESTAMPCREATED DESC");
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

    public void closeDialog() {
        dialog.close();
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

    public void editTask(LocalDate dateValue, TaskDataModel items) {
        try {
            editTaskLoader = new FXMLLoader(getClass().getResource("/fxml/EditTask.fxml"));
            editTaskLayout = (VBox) editTaskLoader.load();
            EditTaskController editTaskController = editTaskLoader.getController();
            editTaskController.setArchivesController(this);
            editTaskController.setEditTaskValue(items, 1);
        } catch (IOException ex) {
            Logger.getLogger(DashboardContentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        dialog = new JFXDialog(root, editTaskLayout, JFXDialog.DialogTransition.CENTER);
        dialog.show();
    }

    public void updateItem(int taskID, Date dateCreated, Time timeCreated, int priority, String taskTitle, String taskDescription, int isComplete, Timestamp timestamp) {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE TaskDayListDB SET DATECREATED = PARSEDATETIME('" + dateCreated + "', 'yyyy-MM-dd'), TIMECREATED = PARSEDATETIME('" + timeCreated + "', 'HH:mm:ss'), PRIORITY = " + priority + ", TASKTITLE = '" + taskTitle + "', TASKDESCRIPTION = '" + taskDescription + "', ISCOMPLETE = " + isComplete + ", TIMESTAMPCREATED = PARSEDATETIME('" + timestamp + "', 'yyyy-MM-dd HH:mm:ss.SS') WHERE ID = " + taskID);
                System.out.println(taskID + " " + dateCreated + " " + timeCreated + " " + priority + " " + taskTitle + " " + taskDescription + " " + isComplete + " " + timestamp);
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
                ResultSet rs = stmt.executeQuery("SELECT * FROM TaskDayListDB WHERE DATECREATED < '" + checkDate + "' AND TASKLOCATION = 0 ORDER BY " + sortParam + " " + sortMode);
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

    public void resetUI() {
        taskDate.setText("No item selected");
        taskTime.setText("No item selected");
        taskPriority.setText("No item selected");
        taskPriority.getGraphic().setStyle("-fx-fill:grey");
        taskStatus.setText("No item selected");
        taskTitle.setText("No item selected");
        taskDescription.setText("No item selected");
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }
}
