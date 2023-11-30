package View;

import java.util.ArrayList;
import java.util.List;

import Model.Model.ALGORITHMS;
import Model.Puzzles.Puzzle;
import ViewModel.ViewModel;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class View {
    private ViewModel viewModel;
    private VBox logBox;
    private VBox puzzleBox;
    private VBox buttonsBox;
    private VBox solutionsBox;
    // Initial Puzzle
    private RadioButton customOption;
    private RadioButton randomOption;
    private VBox customBoardBox;
    private Spinner<Integer> randomMovesSpinner;
    private int puzzleSize = 4;
    private Button generateButton;

    public View() {
        this.viewModel = new ViewModel();
    }

    public BorderPane createMainBox() {
        BorderPane mainBox = new BorderPane();
        mainBox.setPadding(new Insets(-20, 0, 0, 0));

        // Create log box
        logBox = createLogBox(viewModel.getLogListProperty());
        mainBox.setLeft(logBox);

        // Create puzzle box
        puzzleBox = createPuzzleBox();
        mainBox.setCenter(puzzleBox);

        // Create buttons box
        buttonsBox = createButtonsBox();
        mainBox.setRight(buttonsBox);

        return mainBox;
    }

    public VBox createPuzzleBox() {
        VBox puzzleBox = new VBox(20);
        puzzleBox.setAlignment(Pos.CENTER);
        // puzzleBox.setMinWidth(200);
        // puzzleBox.setMinHeight(600);
        puzzleBox.setStyle("-fx-border-color: black;");

        // Create difficulty box
        VBox difficultyBar = createDifficultyBar(viewModel.getDifficultyProperty());

        // Create board box
        VBox boardBox = createBoard(viewModel.getPuzzleProperty());

        // Create movements box
        HBox movementsBar = createMomvementsBar(viewModel.getMovementsProperty());

        puzzleBox.getChildren().addAll(difficultyBar, boardBox, movementsBar);

        return puzzleBox;
    }

    public VBox createBoard(ObjectProperty<Puzzle> puzzleProperty) {
        VBox boardBox = new VBox();
        // Create puzzle board
        GridPane board = new GridPane();
        board.setAlignment(Pos.CENTER);
        board.setMinWidth(10);
        board.getStyleClass().add("board");
        board.setMinHeight(300);

        puzzleProperty.addListener((obs, oldBoard, newBoard) -> {
            int size = newBoard.size();

            // Clear the existing board
            board.getChildren().clear();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int value = newBoard.getBoard()[i][j];
                    VBox tilePane = createTilePane(value);
                    board.add(tilePane, j, i);
                }
            }
        });

        boardBox.getChildren().add(board);

        return boardBox;
    }

    public VBox createDifficultyBar(IntegerProperty difficultyProperty) {
        VBox difficultyBar = new VBox(10);
        difficultyBar.setAlignment(Pos.CENTER);
        Label label = new Label("Difficulty:");
        Label difficultyLabel = new Label("");
        difficultyLabel.getStyleClass().add("bold-text");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMinWidth(150);
        progressBar.progressProperty().bind(difficultyProperty.divide(5.0));

        // Bind the progress bar color to the difficulty level
        difficultyProperty.addListener((obs, oldDifficulty, newDifficulty) -> {
            if (newDifficulty.intValue() == 0) {
                progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            }
            // Set the color based on the difficulty level
            switch (newDifficulty.intValue()) {
                case 1:
                    progressBar.setStyle("-fx-accent: green;");
                    difficultyLabel.setText("Easy");
                    break;
                case 2:
                    progressBar.setStyle("-fx-accent: rgb(252, 220, 78)");
                    difficultyLabel.setText("Medium");
                    break;
                case 3:
                    progressBar.setStyle("-fx-accent: orange;");
                    difficultyLabel.setText("Challenging");

                    break;
                case 4:
                    progressBar.setStyle("-fx-accent: rgb(243, 99, 99);");
                    difficultyLabel.setText("Hard");

                    break;
                case 5:
                    progressBar.setStyle("-fx-accent: red;");
                    difficultyLabel.setText("Very Hard");

                    break;
                default:
                    progressBar.setStyle(""); // Clear the style
            }
        });

        difficultyBar.getChildren().addAll(label, progressBar, difficultyLabel);

        return difficultyBar;
    }

    public HBox createMomvementsBar(ListProperty<Puzzle> movementsProperty) {
        HBox movementsBar = new HBox(10);
        movementsBar.setAlignment(Pos.CENTER);
        // Buttons
        Button prevButton = new Button("<");
        prevButton.setDisable(true);
        prevButton.setPrefSize(35, 35);
        Button nextButton = new Button(">");
        nextButton.setDisable(true);
        nextButton.setPrefSize(35, 35);
        // Number of movements box
        HBox numbersBox = new HBox(5);
        Label stepLabel = new Label("0");
        Label seperatorLabel = new Label("/");
        Label totalStepsLabel = new Label("0");
        numbersBox.getChildren().addAll(stepLabel, seperatorLabel, totalStepsLabel);
        numbersBox.setAlignment(Pos.CENTER);

        movementsProperty.addListener((obs, oldList, newList) -> {
            boolean disable = newList.size() > 0;
            prevButton.setDisable(disable);
            nextButton.setDisable(disable);
            totalStepsLabel.setText(String.valueOf(newList.size()));
        });

        // Handle buttons
        prevButton.setOnAction(e -> handlePreviousButtonClick(stepLabel, totalStepsLabel));
        nextButton.setOnAction(e -> handleNextButtonClick(stepLabel, totalStepsLabel));

        movementsBar.getChildren().addAll(prevButton, numbersBox, nextButton);

        return movementsBar;
    }

    private void handlePreviousButtonClick(Label stepLabel, Label totalStepsLabel) {
        int currentStep = Integer.parseInt(stepLabel.getText());
        if (currentStep > 1) {
            // Move to the previous step
            currentStep--;
            stepLabel.setText(String.valueOf(currentStep));
            updatePuzzleForStep(currentStep);
        }
    }

    private void handleNextButtonClick(Label stepLabel, Label totalStepsLabel) {
        int currentStep = Integer.parseInt(stepLabel.getText());
        int totalSteps = Integer.parseInt(totalStepsLabel.getText());
        if (currentStep < totalSteps) {
            // Move to the next step
            currentStep++;
            stepLabel.setText(String.valueOf(currentStep));
            updatePuzzleForStep(currentStep);
        }
    }

    private void updatePuzzleForStep(int step) {
        // Get the puzzle for the specified step and update the puzzle property
        ListProperty<Puzzle> movementsProperty = viewModel.getMovementsProperty();
        ObjectProperty<Puzzle> puzzleProperty = viewModel.getPuzzleProperty();

        if (movementsProperty != null && movementsProperty.size() >= step) {
            puzzleProperty.set(movementsProperty.get(step - 1));
        }
    }

    public VBox createButtonsBox() {
        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setMinWidth(250);
        buttonsBox.setMinHeight(600);
        buttonsBox.setStyle("-fx-border-color: black;");
        // Labels
        Label mainLabel = new Label("Generate Puzzle");
        mainLabel.getStyleClass().add("bold-text");
        Text invalidBoardText = new Text("Invalid Board");
        invalidBoardText.setFill(Color.RED);
        invalidBoardText.setVisible(false);
        VBox initialPuzzleBox = new VBox();
        // Puzzle type box
        HBox puzzleTypeBox = new HBox(10);
        puzzleTypeBox.setAlignment(Pos.CENTER);
        Label typeLabel = new Label("Select Puzzle:");
        ComboBox<String> typesComboBox = new ComboBox<>();
        typesComboBox.getItems().addAll("8-puzzle", "15-puzzle", "24-puzzle");
        typesComboBox.setValue("15-puzzle");
        typesComboBox.valueProperty().addListener((obs, oldType, newType) -> {
            if (invalidBoardText.isVisible()) {
                invalidBoardText.setVisible(false);
            }
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
            // Adjust the size of the puzzle
            switch (newType) {
                case "8-puzzle":
                    this.puzzleSize = 3;
                    break;
                case "15-puzzle":
                    this.puzzleSize = 4;
                    break;
                case "24-puzzle":
                    this.puzzleSize = 5;
                    break;
            }
            if (customOption.isSelected())
                handleCustomBoardSelection(initialPuzzleBox);
        });
        puzzleTypeBox.getChildren().addAll(typeLabel, typesComboBox);
        // How to initialize box
        randomOption = new RadioButton("Random Moves");
        customOption = new RadioButton("Custom Board");
        ToggleGroup toggleGroup = new ToggleGroup();
        customOption.setToggleGroup(toggleGroup);
        randomOption.setSelected(true);
        randomOption.setToggleGroup(toggleGroup);
        HBox radioButtonsBox = new HBox(10, randomOption, customOption);
        radioButtonsBox.setAlignment(Pos.CENTER);
        // radioButtonsBox.setPadding(new Insets(0, 0, 0, 70));
        ;
        // Create a spinner for random moves
        initialPuzzleBox.setAlignment(Pos.CENTER);
        randomMovesSpinner = new Spinner<>();
        randomMovesSpinner.setPrefWidth(85);
        randomMovesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000000));
        randomMovesSpinner.setEditable(true); // Allow manual input
        randomMovesSpinner.setOnMouseClicked(e -> {
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
        });
        TextField editor = randomMovesSpinner.getEditor();
        editor.setOnMouseClicked(e -> {
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
        });
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Try to parse the editor text to an integer
                int newValueInt = Integer.parseInt(newValue);

                // Validate that the parsed value is within the allowed range
                if (newValueInt >= 1 && newValueInt <= 1000000) {
                    randomMovesSpinner.getStyleClass().remove("invalid");
                    randomMovesSpinner.getValueFactory().setValue(newValueInt);
                } else {
                    randomMovesSpinner.getStyleClass().add("invalid");
                }
            } catch (NumberFormatException e) {
            }
        });
        handleRandomMovesSelection(initialPuzzleBox);
        // Set the event handlers for the radio buttons
        customOption.setOnAction(e -> {
            if (invalidBoardText.isVisible()) {
                invalidBoardText.setVisible(false);
            }
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
            handleCustomBoardSelection(initialPuzzleBox);
        });
        randomOption.setOnAction(e -> {
            if (invalidBoardText.isVisible()) {
                invalidBoardText.setVisible(false);
            }
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
            handleRandomMovesSelection(initialPuzzleBox);
        });
        // Generate button
        generateButton = new Button("Generate");
        generateButton.setMinHeight(40);
        generateButton.setMinWidth(150);
        generateButton.setOnAction(e -> {
            if (generateButton.getStyleClass().contains("invalid")) {
                generateButton.getStyleClass().remove("invalid");
            }
            if (customOption.isSelected()) {
                // Custom starting board
                int[][] customBoard = extractCustomBoard(customBoardBox);
                try {
                    viewModel.createCustomPuzzle(puzzleSize, customBoard);
                } catch (IllegalArgumentException exc) {
                    invalidBoardText.setVisible(true);
                }
            } else {
                // Number of random moves
                int randomMoves = randomMovesSpinner.getValue();
                viewModel.createRandomPuzzle(puzzleSize, randomMoves);
            }
        });
        // Algorithms check-box
        Label solveLabel = new Label("Solve Using:");
        solveLabel.getStyleClass().add("bold-text");
        VBox algorithmsBox = new VBox(10);
        algorithmsBox.setPadding(new Insets(0, 0, 0, 80));
        // algorithmsBox.setAlignment(Pos.CENTER);
        CheckBox algorithm1CheckBox = new CheckBox("BFS");
        CheckBox algorithm2CheckBox = new CheckBox("Dijkstra");
        CheckBox algorithm3CheckBox = new CheckBox("A* (Manhattan)");
        CheckBox algorithm4CheckBox = new CheckBox("A* (Euclidean)");
        CheckBox algorithm5CheckBox = new CheckBox("A* (Misplaced Tiles)");
        algorithmsBox.getChildren().addAll(algorithm1CheckBox, algorithm2CheckBox, algorithm3CheckBox,
                algorithm4CheckBox, algorithm5CheckBox);

        // Solve button
        Button solveButton = new Button("Solve");
        solveButton.setMinHeight(50);
        solveButton.setMinWidth(200);
        solveButton.getStyleClass().add("bold-text");
        solveButton.setOnAction(e -> {
            if (viewModel.getPuzzleProperty().get() == null) {
                generateButton.getStyleClass().add("invalid");
            }
            List<String> algorithmList = new ArrayList<>();
            if (algorithm1CheckBox.isSelected()){
                System.out.println("selected");
                algorithmList.add(ALGORITHMS.BFS);
            }
            if (algorithm2CheckBox.isSelected()){
                algorithmList.add(ALGORITHMS.DIJKSTRA);
            }
            if (algorithm3CheckBox.isSelected()){
                algorithmList.add(ALGORITHMS.ASTAR_MANHATTAN);
            }
            if (algorithm4CheckBox.isSelected()){
                algorithmList.add(ALGORITHMS.ASTAR_EUCLIDEAN);
            }
            if (algorithm5CheckBox.isSelected()){
                algorithmList.add(ALGORITHMS.ASTAR_MISPLACED);
            }

            viewModel.Solve(algorithmList);
        });

        buttonsBox.getChildren().addAll(mainLabel, puzzleTypeBox, radioButtonsBox, initialPuzzleBox, generateButton,
                invalidBoardText, solveLabel, algorithmsBox, solveButton);
        return buttonsBox;
    }

    private int[][] extractCustomBoard(VBox customBoardBox) {
        List<List<Integer>> boardList = new ArrayList<>();

        for (Node rowNode : customBoardBox.getChildren()) {
            if (rowNode instanceof HBox) {
                List<Integer> rowList = new ArrayList<>();
                for (Node cellNode : ((HBox) rowNode).getChildren()) {
                    if (cellNode instanceof TextField) {
                        String input = ((TextField) cellNode).getText().trim();
                        int value = input.isEmpty() ? 0 : Integer.parseInt(input);
                        rowList.add(value);
                    }
                }
                boardList.add(rowList);
            }
        }

        int[][] board = new int[boardList.size()][];
        for (int i = 0; i < boardList.size(); i++) {
            List<Integer> rowList = boardList.get(i);
            board[i] = new int[rowList.size()];
            for (int j = 0; j < rowList.size(); j++) {
                board[i][j] = rowList.get(j);
            }
        }

        return board;
    }

    private void handleRandomMovesSelection(VBox initialPuzzleBox) {
        // Clear previous content
        initialPuzzleBox.getChildren().clear();

        initialPuzzleBox.getChildren().add(randomMovesSpinner);
    }

    private void handleCustomBoardSelection(VBox initialPuzzleBox) {
        // Clear previous content
        initialPuzzleBox.getChildren().clear();

        // Create a VBox for the custom board inputs
        customBoardBox = new VBox();
        customBoardBox.setAlignment(Pos.CENTER);
        customBoardBox.setPadding(new Insets(-10, -10, -10, -10));

        for (int i = 0; i < puzzleSize; i++) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);

            for (int j = 0; j < puzzleSize; j++) {
                TextField textField = new TextField();
                textField.setPrefSize(30, 20);
                textField.setOnMouseClicked(e -> {
                    if (generateButton.getStyleClass().contains("invalid")) {
                        generateButton.getStyleClass().remove("invalid");
                    }
                });

                // Add a listener to the text field to validate the input
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!isValidInput(newValue)) {
                        // Display a red alert text if the input is invalid
                        // You can replace this with your actual error handling logic
                        textField.setStyle("-fx-border-color: red;");
                    } else {
                        textField.setStyle(null);
                    }
                });

                row.getChildren().add(textField);
            }

            customBoardBox.getChildren().add(row);
        }

        initialPuzzleBox.getChildren().add(customBoardBox);
    }

    private boolean isValidInput(String input) {
        if (input.isEmpty()) {
            return true; // Empty input is valid for the empty tile
        }

        try {
            int value = Integer.parseInt(input);
            int maxValue = puzzleSize * puzzleSize - 1;
            return value >= 1 && value <= maxValue;
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }

    public VBox createLogBox(ListProperty<String> logListProperty) {
        VBox logBox = new VBox();
        logBox.setAlignment(Pos.CENTER);
        logBox.setMinWidth(300);
        logBox.setMinHeight(600);
        logBox.setStyle("-fx-border-color: black;");

        // Bind the logText to the list property
        for (String log : logListProperty) {
            Text logText = new Text(log);
            logText.getStyleClass().add("log-text");
            logBox.getChildren().add(logText);
        }

        // Add a listener to update the VBox when the list property changes
        logListProperty.addListener((observable, oldValue, newValue) -> {
            logBox.getChildren().clear(); // Clear existing Text nodes
            for (String log : newValue) {
                Text logText = new Text(log);
                logText.getStyleClass().add("log-text");
                logBox.getChildren().add(logText);
            }
        });

        return logBox;
    }

    public VBox createSolutionsBox() {
        solutionsBox = new VBox(10);

        return solutionsBox;
    }

    private VBox createTilePane(int value) {
        VBox tilePane = new VBox();
        tilePane.setAlignment(Pos.CENTER);
        tilePane.getStyleClass().add("tile");
        tilePane.setPrefSize(45, 45);

        Label valueLabel = new Label(value == 0 ? "" : String.valueOf(value));

        tilePane.getChildren().add(valueLabel);

        if (value == 0) {
            tilePane.getStyleClass().add("empty-tile");
        }

        return tilePane;
    }
}
