package ViewModel;

import Model.Model;
import Model.Puzzles.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ViewModel {
    private Model model;
    private ExecutorService executorService;
    private ObjectProperty<Puzzle> puzzleProperty;
    private IntegerProperty difficultyProperty;
    private BooleanProperty finishedProperty;
    private ListProperty<String> logListProperty;
    private ListProperty<Puzzle> movementsProperty;
    private ListProperty<Solution> solutionsProperty;

    public ViewModel() {
        this.model = new Model();
        this.puzzleProperty = new SimpleObjectProperty<>(null);
        this.difficultyProperty = new SimpleIntegerProperty(0);
        this.finishedProperty = new SimpleBooleanProperty(false);
        this.logListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.movementsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.solutionsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public void createRandomPuzzle(int size, int radomMoves) {
        finishedProperty.set(false);
        Puzzle randomPuzzle = model.createRandomPuzzle(size, radomMoves);
        puzzleProperty.set(randomPuzzle);
    }

    public void createCustomPuzzle(int size, int[][] startingBoard) {
        finishedProperty.set(false);
        Puzzle customPuzzle = model.createCustomPuzzle(size, startingBoard);
        puzzleProperty.set(customPuzzle);
    }

    public void estimateDifficulty(int bfsExtra) {
        int difficulty = model.estimateDifficulty(puzzleProperty.get());
        if (bfsExtra != 0) {
            difficulty = difficulty + bfsExtra > 5 ? 5 : difficulty + bfsExtra;
        }
        if (puzzleProperty.get().size() == 5) {
            difficulty = difficulty + 1 > 5 ? 5 : difficulty + 1;
        }
        difficultyProperty.set(difficulty);
    }

    public void addToLog(String line) {
        Platform.runLater(() -> {
            logListProperty.add(line);
        });
    }

    public void solvePuzzleWithAlgorithms(List<String> algorithms) {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            CountDownLatch latch = new CountDownLatch(algorithms.size());
            for (String algorithm : algorithms) {
                // Solve the puzzle for the current algorithm
                addToLog("Solving using " + algorithm + "...");
                Solution solution;
                try {
                    solution = model.solve(puzzleProperty.get(), algorithm);
                    addToLog("Solved!");
                    // addToLog("\n");
                } catch (InterruptedException e) {
                    return;
                }

                // Update UI on the JavaFX Application Thread
                Platform.runLater(() -> {
                    solutionsProperty.add(solution);
                    movementsProperty.setAll(solution.getMovementsList());
                    latch.countDown();
                });

            }

            try {
                latch.await(); // Wait for all tasks to complete
            } catch (InterruptedException e) {
            }

            javafx.application.Platform.runLater(() -> {
                addToLog("Done.");
                addToLog("\n");
                finishedProperty.set(true);
            });
        });
        executorService.shutdown();
    }

    public void stop() {
        if (executorService != null) {
            addToLog("Stopped!");
            addToLog("\n");
            executorService.shutdownNow();
        }
    }

    public void clearLog() {
        logListProperty.clear();
    }

    public void reset() {
        finishedProperty.set(false);
        // logListProperty.clear();
        movementsProperty.clear();
        solutionsProperty.clear();
    }

    public void setDifficultyProperty(int difficulty) {
        this.difficultyProperty.set(difficulty);
    }

    public ObjectProperty<Puzzle> getPuzzleProperty() {
        return puzzleProperty;
    }

    public IntegerProperty getDifficultyProperty() {
        return difficultyProperty;
    }

    public BooleanProperty getFinishedProperty() {
        return finishedProperty;
    }

    public ListProperty<String> getLogListProperty() {
        return logListProperty;
    }

    public ListProperty<Puzzle> getMovementsProperty() {
        return movementsProperty;
    }

    public ListProperty<Solution> getSolutionsProperty() {
        return solutionsProperty;
    }

}
