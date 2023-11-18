package Puzzles;

import java.util.Scanner;

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

        int reverseBoard[][] = { { 0, 15, 14, 13 }, { 12, 11, 10, 9 }, { 8, 7, 6, 5 }, { 4, 3, 2, 1 } };
        FifteenPuzzle puzzle15 = new FifteenPuzzle(reverseBoard);
        System.out.println(puzzle15);

        int n = 5000;
        FifteenPuzzle randPuzzle15 = new FifteenPuzzle(n);
        System.out.println(randPuzzle15);

        // int move;
        // Scanner in = new Scanner(System.in);
        // while ((move = Integer.parseInt(in.next()))!= 9) {
        // System.out.println("UP - 0\nDOWN - 1\nLEFT - 2\nRIGHT - 3\n");
        // puzzle15.performMove(move);
        // System.out.println(puzzle15);
        // }
        // in.close();
    }

}
