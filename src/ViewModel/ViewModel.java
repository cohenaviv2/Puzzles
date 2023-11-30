package ViewModel;

import Model.Model;
import Model.Puzzles.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ViewModel implements Observer {
    private Model model;
    private ObjectProperty<Puzzle> puzzleProperty;
    private IntegerProperty difficultyProperty;
    private IntegerProperty countProperty;
    private BooleanProperty finishedProperty;
    private ListProperty<String> logListProperty;
    private ListProperty<Puzzle> movementsProperty;
    private ListProperty<Solution> solutionsProperty;
    private List<String> algorithmList;
    private int numOfAlgorithms;

    public ViewModel() {
        this.model = new Model();
        this.model.addObserver(this);
        this.puzzleProperty = new SimpleObjectProperty<>(null);
        this.difficultyProperty = new SimpleIntegerProperty(0);
        this.countProperty = new SimpleIntegerProperty(0);
        this.finishedProperty = new SimpleBooleanProperty(false);
        this.logListProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.movementsProperty = new SimpleListProperty<>(null);
        this.solutionsProperty = new SimpleListProperty<>(null);
    }

    public void createRandomPuzzle(int size, int radomMoves) {
        Puzzle randomPuzzle = model.createRandomPuzzle(size, radomMoves);
        puzzleProperty.set(randomPuzzle);
        estimateDifficulty();
    }

    public void createCustomPuzzle(int size, int[][] startingBoard) {
        Puzzle customPuzzle = model.createCustomPuzzle(size, startingBoard);
        puzzleProperty.set(customPuzzle);
        estimateDifficulty();
    }

    public void estimateDifficulty() {
        int difficulty = model.estimateDifficulty(puzzleProperty.get());
        difficultyProperty.set(difficulty);
    }

    public void addToLog(String line) {
        logListProperty.add(line);
    }

    public void increaseCount(int count) {
        if (count == numOfAlgorithms) {
            addToLog("Done.");
            finishedProperty.set(true);
            List<Solution> solutions = model.geSolutions();
            solutionsProperty.setAll(solutions);
            movementsProperty.setAll(solutions.get(0).getMovementsList());
        } else {
            addToLog("Solving using " + algorithmList.get(count - 1) + "...");
        }
    }

    public void Solve(List<String> algorithms) {
        this.algorithmList = algorithms;
        numOfAlgorithms = algorithmList.size();
        // Solve with all the algorithms in the list
        model.Solve(puzzleProperty.get(), algorithms);
    }

    public void reset() {
        finishedProperty.set(false);
    }

    @Override
    public void update(Observable o, Object arg) {
        String solveUpdate = (String) arg;
        System.out.println(solveUpdate);
        addToLog(solveUpdate);
        int count = countProperty.get() + 1;
        increaseCount(count);
    }

    public ObjectProperty<Puzzle> getPuzzleProperty() {
        return puzzleProperty;
    }

    public IntegerProperty getDifficultyProperty() {
        return difficultyProperty;
    }

    public IntegerProperty getCountProperty() {
        return countProperty;
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
