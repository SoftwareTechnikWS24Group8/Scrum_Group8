package de.hse.swb8;

import de.hse.swb8.observer.Observable;
import de.hse.swb8.observer.Observer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DataBaseLogin implements Observer<String> {

    Callback callback;

    public void LoginIntoDataBase(Callback onComplete)
    {
        callback = onComplete;
        try {
            startWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startWindow() throws IOException {
        try {
            Stage stage = new Stage();  // Create a new stage (window)
            FXMLLoader fxmlLoader = new FXMLLoader(DataBaseLogin.class.getResource("DataBaseLogin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNIFIED);
            stage.setTitle("DataBase Login");
            stage.setScene(scene);
            stage.show();

            DataBaseLoginController controller = fxmlLoader.getController();
            controller.addObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable<String> observable, String newValue) {

        //checks
        System.out.println(newValue);
        DataBaseInfo info = new DataBaseInfo("","","");
        callback.execute(info);
    }
}
