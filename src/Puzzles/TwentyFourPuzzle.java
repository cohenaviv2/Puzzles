package Puzzles;

public class TwentyFourPuzzle extends Puzzle {
    private static final int SIZE = 5;

    public TwentyFourPuzzle(int[][] startingBoard) {
        super(SIZE, startingBoard);
    }

    public TwentyFourPuzzle(int n) {
        super(SIZE, n);
    }
    
    public TwentyFourPuzzle(TwentyFourPuzzle other){
        super(other);
    }


    public static void main(String[] args) {
        // Test 24-puzzle
        TwentyFourPuzzle puzzle24 = new TwentyFourPuzzle(0);
        System.out.println(puzzle24);
    }

}
