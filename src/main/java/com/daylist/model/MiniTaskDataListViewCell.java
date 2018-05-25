/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.model;

import com.jfoenix.controls.JFXListCell;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import com.daylist.controller.MiniTaskDataCellController;

/**
 *
 * @author rychemrycho
 */
public class MiniTaskDataListViewCell extends JFXListCell<TaskDataModel> {

    private FXMLLoader mLoader;

    private MiniTaskDataCellController miniTaskDataCellController;

//    private ArchivesController archivesController;
//    private CompleteController completeController;
//    private TrashController trashController;
//    private DashboardContentController dashboardContentController;
    @Override
    protected void updateItem(TaskDataModel item, boolean empty) {
        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource("/fxml/MiniTaskDataCell.fxml"));

                try {
                    mLoader.load();
                    miniTaskDataCellController = mLoader.getController();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            miniTaskDataCellController.getDateTask().setText(String.valueOf(item.getTaskDateCreated()));
            miniTaskDataCellController.getTimeTask().setText(String.valueOf(item.getTaskTimeCreated()));

            switch (item.getTaskPriority()) {
                case 0:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:grey;");
                    break;
                case 1:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:green;");
                    break;
                case 2:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:yellow;");
                    break;
                case 3:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:orange;");
                    break;
                case 4:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:red;");
                    break;
                default:
                    miniTaskDataCellController.getLabelTask().getGraphic().setStyle("-fx-fill:grey;");
                    break;
            }

            miniTaskDataCellController.getTitleTask().setText(String.valueOf(item.getTaskTitle()));
            miniTaskDataCellController.getDescriptionTask().setText(String.valueOf(item.getTaskDescription()));

            if (item.getTaskLocation() != 1) {
                miniTaskDataCellController.getCheckTask().setVisible(true);
                if (item.getTaskComplete() == 0) {
                    miniTaskDataCellController.getCheckTask().setSelected(false);
                } else if (item.getTaskComplete() == 1) {
                    miniTaskDataCellController.getCheckTask().setSelected(true);
                }

                miniTaskDataCellController.getCheckTask().setOnAction(e -> {
                    if (miniTaskDataCellController.getCheckTask().isSelected() && item.getTaskComplete() != 1) {
                        updateItemStatus(item.getTaskID(), 1);
                        MiniTaskDataCellController.getCompleteController().updateList();
                        MiniTaskDataCellController.getArchivesController().updateList();
                    } else {
                        updateItemStatus(item.getTaskID(), 0);
                        MiniTaskDataCellController.getCompleteController().updateList();
                        MiniTaskDataCellController.getCompleteController().setSnackbar("Item moved to dashboard");
                        MiniTaskDataCellController.getArchivesController().updateList();
                    }
                });
            } else {
                miniTaskDataCellController.getCheckTask().setVisible(false);
            }

            setText(null);
            setGraphic(miniTaskDataCellController.getRootTaskData());
        }
    }

    public void updateItemStatus(int id, int isComplete) {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE TaskDayListDB set ISCOMPLETE = " + isComplete + " WHERE ID = " + id);
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
