package ifce.ppd.tuplespace;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InitApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InitApplication.class.getResource("init-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        stage.setTitle("IFCE - PPD - Java Space Chat - João Marcus Maia Rocha");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("java.security.policy","/home/joaomarcus/Projetos/tuple-space/src/main/java/ifce/ppd/tuplespace/all.policy");
        System.setSecurityManager(new SecurityManager());

        launch();
    }
}