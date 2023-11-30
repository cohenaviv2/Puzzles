package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PuzzleSolverApp extends Application {
    private Stage mainStage;
    private View view;

    public void initialize() {
        this.mainStage = new Stage();
        this.mainStage.setTitle("Puzzle Solver");
        this.view = new View();
    }

    public void render(boolean withSolutions) {
        Scene mainScene;
        if(withSolutions) {

        } else {
            BorderPane mainBox = view.createMainBox();
            mainScene = new Scene(mainBox, 1000, 560);
            mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            mainStage.setScene(mainScene);
            mainStage.show();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        render(false);
    }

    
    public static void main(String[] args) {
        launch(args);
    }

}
