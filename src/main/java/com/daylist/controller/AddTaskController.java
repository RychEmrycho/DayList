/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class AddTaskController implements Initializable {

    @FXML
    private JFXTextField addTitle;

    @FXML
    private RequiredFieldValidator validator;

    @FXML
    private JFXTextArea addDescription;

    @FXML
    private JFXComboBox<Label> jComboBox;

    @FXML
    private JFXTimePicker jTimePicker;

    @FXML
    private JFXDatePicker jDatePicker;

    @FXML
    private JFXButton submitDialogBtn;

    @FXML
    private JFXButton closeDialogBtn;

    private DashboardContentController dashboardContentController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Circle optionalCircle = new Circle(8, Paint.valueOf("grey"));
        Circle normalCircle = new Circle(8, Paint.valueOf("green"));
        Circle mediumCircle = new Circle(8, Paint.valueOf("yellow"));
        Circle importantCircle = new Circle(8, Paint.valueOf("orange"));
        Circle emergencyCircle = new Circle(8, Paint.valueOf("red"));

        jComboBox.getItems().add(new Label("Optional", optionalCircle));
        jComboBox.getItems().add(new Label("Normal", normalCircle));
        jComboBox.getItems().add(new Label("Medium", mediumCircle));
        jComboBox.getItems().add(new Label("Important", importantCircle));
        jComboBox.getItems().add(new Label("Emergency", emergencyCircle));

        jDatePicker.setValue(LocalDate.now());
        jTimePicker.setValue(LocalTime.now());

        //Button close
        closeDialogBtn.setOnAction(e -> {
            dashboardContentController.closeDialog();
        });

        //Set Title Empty Validation
        submitDialogBtn.setDisable(true);
        validator.setMessage("You should add title for your task before saving it!");
        validator.setIcon(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE));
        addTitle.getValidators().add(validator);
        addTitle.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                addTitle.validate();
                submitDialogBtn.setDisable(false);
            }
        });

        addDescription.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (addTitle.getText().isEmpty()) {
                addTitle.validate();
                submitDialogBtn.setDisable(true);
            }
        });

        submitDialogBtn.setOnAction(e -> {
            if (!addTitle.getText().isEmpty()) {
                StringConverter<LocalTime> timeConverter = new StringConverter<LocalTime>() {

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
                String timeCreatedString = timeConverter.toString(jTimePicker.getValue());
                Time timeCreated = Time.valueOf(timeConverter.fromString(timeCreatedString));
                String dateCreatedString = dateConverter.toString(jDatePicker.getValue());
                Date dateCreated = Date.valueOf(dateConverter.fromString(dateCreatedString));
                int priority;
                if (jComboBox.getSelectionModel().getSelectedIndex() == -1) {
                    priority = 0;
                } else {
                    priority = jComboBox.getSelectionModel().getSelectedIndex();
                }
                String taskTitle = addTitle.getText();
                String taskDescription = addDescription.getText();
                Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                insertData(dateCreated, timeCreated, priority, taskTitle, taskDescription, timestamp);
                dashboardContentController.updateList();
            } else {
                addTitle.validate();
                submitDialogBtn.setDisable(true);
            }
        }
        );
    }

    public void setDate(LocalDate dateValue) {
        jDatePicker.setValue(dateValue);
    }

    public void insertData(Date dateCreated, Time timeCreated, int priority, String taskTitle, String taskDescription, Timestamp timestamp) {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("INSERT INTO TaskDayListDB VALUES(default, PARSEDATETIME('" + dateCreated + "', 'yyyy-MM-dd'), PARSEDATETIME('" + timeCreated + "', 'HH:mm:ss')  , " + priority + ", '" + taskTitle + "', '" + taskDescription + "', 0, PARSEDATETIME('" + timestamp + "', 'yyyy-MM-dd HH:mm:ss.SS'),0);");
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

    public void setDashboardContentController(DashboardContentController dashboardContentController) {
        this.dashboardContentController = dashboardContentController;
    }

}
