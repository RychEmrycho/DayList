/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.daylist.controller;

import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author rychemrycho
 */
public class DrawerContentController implements Initializable {
    @FXML
    private JFXListView<?> sideDrawerList;

    private DashboardController dashboardController;

    private FXMLLoader dashboardPageLoader;
    private FXMLLoader completePageLoader;
    private FXMLLoader archivesPageLoader;
    private FXMLLoader trashPageLoader;
    
//    private DashboardContentController dashboardContentController;
//
//    public void setDashboardContentController(DashboardContentController dashboardContentController) {
//        this.dashboardContentController = dashboardContentController;
//    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            dashboardPageLoader = new FXMLLoader(getClass().getResource("/fxml/DashboardContent.fxml"));
            StackPane dashboardPage = (StackPane) dashboardPageLoader.load();

            completePageLoader = new FXMLLoader(getClass().getResource("/fxml/Complete.fxml"));
            StackPane completePage = (StackPane) completePageLoader.load();

            archivesPageLoader = new FXMLLoader(getClass().getResource("/fxml/Archives.fxml"));
            StackPane archivesPage = (StackPane) archivesPageLoader.load();

            trashPageLoader = new FXMLLoader(getClass().getResource("/fxml/Trash.fxml"));
            StackPane trashPage = (StackPane) trashPageLoader.load();

            BorderPane aboutPage = FXMLLoader.load(getClass().getResource("/fxml/About.fxml"));
            
            sideDrawerList.getSelectionModel().selectedItemProperty().addListener(e -> {
                switch (sideDrawerList.getSelectionModel().getSelectedIndex()) {
                    case 0://Dashboard
                        dashboardController.getDrawer().close();
                        dashboardController.getTitleToolbar().setText("Dashboard");
                        
                        FadeTransition fadeTransitionDashboard
                                = new FadeTransition(Duration.millis(500), dashboardPage);
                        fadeTransitionDashboard.setFromValue(0.0f);
                        fadeTransitionDashboard.setToValue(1.0f);
                        fadeTransitionDashboard.setCycleCount(1);
                        fadeTransitionDashboard.setAutoReverse(true);
                        fadeTransitionDashboard.play();
                        dashboardController.getDrawer().setContent(dashboardPage);
//                            dashboardContentController.updateIsComplete();
                        break;
                    case 1://Complete
                        dashboardController.getDrawer().close();
                        dashboardController.getTitleToolbar().setText("Complete");
                        
                        FadeTransition fadeTransitionComplete
                                = new FadeTransition(Duration.millis(500), completePage);
                        fadeTransitionComplete.setFromValue(0.0f);
                        fadeTransitionComplete.setToValue(1.0f);
                        fadeTransitionComplete.setCycleCount(1);
                        fadeTransitionComplete.setAutoReverse(true);
                        fadeTransitionComplete.play();
                        dashboardController.getDrawer().setContent(completePage);
//                            getCompleteController().updateList();
                        break;
                    case 2://Archives
                        dashboardController.getDrawer().close();
                        dashboardController.getTitleToolbar().setText("Archives");

                        FadeTransition fadeTransitionArchives
                                = new FadeTransition(Duration.millis(500), archivesPage);
                        fadeTransitionArchives.setFromValue(0.0f);
                        fadeTransitionArchives.setToValue(1.0f);
                        fadeTransitionArchives.setCycleCount(1);
                        fadeTransitionArchives.setAutoReverse(true);
                        fadeTransitionArchives.play();
                        dashboardController.getDrawer().setContent(archivesPage);
//                            getArchivesController().updateList();
                        break;
                    case 3://Trash
                        dashboardController.getDrawer().close();
                        dashboardController.getTitleToolbar().setText("Trash");

                        FadeTransition fadeTransitionTrash
                                = new FadeTransition(Duration.millis(500), trashPage);
                        fadeTransitionTrash.setFromValue(0.0f);
                        fadeTransitionTrash.setToValue(1.0f);
                        fadeTransitionTrash.setCycleCount(1);
                        fadeTransitionTrash.setAutoReverse(true);
                        fadeTransitionTrash.play();
                        dashboardController.getDrawer().setContent(trashPage);
                        
//                            getTrashController().updateList();
                        break;
                    case 4://About
                        dashboardController.getDrawer().close();
                        dashboardController.getTitleToolbar().setText("About");

                        FadeTransition fadeTransitionAbout
                                = new FadeTransition(Duration.millis(500), aboutPage);
                        fadeTransitionAbout.setFromValue(0.0f);
                        fadeTransitionAbout.setToValue(1.0f);
                        fadeTransitionAbout.setCycleCount(1);
                        fadeTransitionAbout.setAutoReverse(true);
                        fadeTransitionAbout.play();
                        dashboardController.getDrawer().setContent(aboutPage);
                        break;
                    default://Unknown index
                        System.out.println("Unknown option index");
                        break;
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(DrawerContentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
