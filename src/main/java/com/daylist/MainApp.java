package com.daylist;

import com.sun.glass.ui.Screen;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public void createTable() {
        try {
            Class.forName("org.h2.Driver");
            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:~/DaylistDB", "root", "root");
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TaskDayListDB(ID INT AUTO_INCREMENT PRIMARY KEY, DATECREATED DATE NOT NULL, TIMECREATED TIME NOT NULL, PRIORITY INT NOT NULL, TASKTITLE VARCHAR(500) NOT NULL, TASKDESCRIPTION VARCHAR, ISCOMPLETE INT NOT NULL, TIMESTAMPCREATED TIMESTAMP NOT NULL, TASKLOCATION INT NOT NULL);");
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

    @Override
    public void start(Stage stage) throws Exception {
        createTable();
        double h = 600, w = 900;
        try {
            h = Screen.getScreens().get(0).getHeight();
            w = Screen.getScreens().get(0).getWidth();
        } catch (Exception e) {

        }

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Dashboard.fxml"));
        
        Scene scene = new Scene(root, w, h);
        //scene.getStylesheets().add(getClass().getResource("/font/fontStyle.css").toExternalForm());
//        scene.getStylesheets().add("/styles/Styles.css");
        
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(MainApp.class.getResource("/font/fontStyle.css").toExternalForm(),
            MainApp.class.getResource("/styles/Styles.css").toExternalForm());

        //stage.setIconified(true);
        stage.setTitle("DayList");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
