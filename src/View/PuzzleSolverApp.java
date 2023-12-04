package View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

public class PuzzleSolverApp extends Application implements Observer {
    private Stage mainStage;
    private Scene mainScene;
    private View view;

    public void initialize() {
        this.mainStage = new Stage();
        this.mainStage.setTitle("Puzzle Solver");
        this.view = new View();
        this.view.addObserver(this);
    }

    private void render(HBox solutions) {
        // Create main window box
        BorderPane mainBox = view.createMainBox();
        // Set scene
        mainScene = new Scene(mainBox, 1250, 650);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    private void renderSolutions(){
        mainStage.setHeight(850);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        this.mainStage.setOnCloseRequest(e -> {
            view.close();
        });
        render(null);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==view){
            renderSolutions();
        }
    }

}
