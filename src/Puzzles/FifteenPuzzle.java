package Puzzles;

public class FifteenPuzzle extends Puzzle {
    private static final int SIZE = 4;

    public FifteenPuzzle(int[][] startingBoard) {
        super(SIZE, startingBoard);
    }

    public FifteenPuzzle(int n) {
        super(SIZE, n);
    }

    public FifteenPuzzle(FifteenPuzzle other){
        super(other);
    }


    public static void main(String[] args) {
        // Test 15-puzzle

        // invalid
        int reverseBoard[][] = { { 0, 15, 14, 13 }, { 12, 11, 10, 9 }, { 8, 7, 6, 5 }, { 4, 3, 2, 1 } };
        FifteenPuzzle puzzle15 = new FifteenPuzzle(reverseBoard);
        System.out.println(puzzle15);

        int n = 5000;
        FifteenPuzzle randPuzzle15 = new FifteenPuzzle(n);
        System.out.println(randPuzzle15);

    }

}
