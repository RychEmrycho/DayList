package com.daylist.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.sun.glass.ui.Screen;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardController implements Initializable {

    @FXML
    private JFXHamburger hamburgerToolbar;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label titleToolbar;

    private FXMLLoader sideDrawerLoader;
    private FXMLLoader dashboardPageLoader;

    private VBox sideDrawer;
//    private ArchivesController archivesController;
//    private DashboardContentController dashboardContentController;
//    private CompleteController completeController;
//    private TrashController trashController;
//
//    public ArchivesController getArchivesController() {
//        return archivesController;
//    }
//
//    public void setArchivesController(ArchivesController archivesController) {
//        this.archivesController = archivesController;
//    }
//
//    public DashboardContentController getDashboardContentController() {
//        return dashboardContentController;
//    }
//
//    public void setDashboardContentController(DashboardContentController dashboardContentController) {
//        this.dashboardContentController = dashboardContentController;
//    }
//
//    public CompleteController getCompleteController() {
//        return completeController;
//    }
//
//    public void setCompleteController(CompleteController completeController) {
//        this.completeController = completeController;
//    }
//
//    public TrashController getTrashController() {
//        return trashController;
//    }
//
//    public void setTrashController(TrashController trashController) {
//        this.trashController = trashController;
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        double h = 600, w = 900;
        try {
            w = Screen.getScreens().get(0).getWidth();
        } catch (Exception e) {

        }

        drawer.setDefaultDrawerSize(w / 5);

        try {            
            sideDrawerLoader = new FXMLLoader(getClass().getResource("/fxml/DrawerContent.fxml"));
            sideDrawer = (VBox) sideDrawerLoader.load();
            DrawerContentController drawerContentController = sideDrawerLoader.getController();
            drawerContentController.setDashboardController(this);

            dashboardPageLoader = new FXMLLoader(getClass().getResource("/fxml/DashboardContent.fxml"));
            StackPane dashboardPage = (StackPane) dashboardPageLoader.load();

            drawer.setSidePane(sideDrawer);
            drawer.setContent(dashboardPage);

        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

        drawer.setOnDrawerOpening(e -> {
            final Transition anim = hamburgerToolbar.getAnimation();
            anim.setRate(1);
            anim.play();
        });

        drawer.setOnDrawerClosing(e -> {
            final Transition anim = hamburgerToolbar.getAnimation();
            anim.setRate(-1);
            anim.play();
        });

        hamburgerToolbar.setOnMouseClicked(e -> {
            if (drawer.isClosed() || drawer.isClosing()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });
    }

    public JFXDrawer getDrawer() {
        return drawer;
    }

    public Label getTitleToolbar() {
        return titleToolbar;
    }

    public VBox getSideDrawer() {
        return sideDrawer;
    }
}
