# Puzzles Solver with Search Algorithms

This repository contains an implementation of the popular 15-puzzle and 24-puzzle problem-solving using Breadth-First Search (BFS), Dijkstra's algorithm, and A* algorithm.

<p align="center">
<img src="https://github.com/cohenaviv2/Puzzles/blob/master/screenshot.jpg?raw=true" width="900px" height="570px">
</p>

## Key Features:

- Efficient implementations of BFS, Dijkstra, and A* algorithms tailored for puzzle-solving scenarios.
- Puzzle state representation, exploration, and graph creation.

## Usage:

1. Explore different puzzle-solving algorithms for the 15-puzzle and 24-puzzle.
2. Visualize the search process and observe the optimal path to the solution.
3. Experiment with variations of initial puzzle states.

## Release:

### Windows

Download the game installer for Windows: <br>
[https://github.com/cohenaviv2/Book-Scrabble/releases/download/v1.1/Book_Scrabble_Setup.exe](https://github.com/cohenaviv2/Puzzles/releases/download/v1.0/Puzzles_steup.exe)

### Other Platforms

For other platforms, ensure you have Java Runtime Environment (JRE) installed. <br> You can play the game by downloading the repository and running the JAR file: <br>
[https://github.com/cohenaviv2/Book-Scrabble/archive/refs/tags/v1.1.zip](https://github.com/cohenaviv2/Puzzles/archive/refs/tags/v1.0.zip)

## Conclusion:

After implementing a graph and the Puzzle class, I tried to produce a graph that represents all possible board states. <br> I noticed that the task takes too much time, and after researching a bit I realized that the number of possible board positions for a puzzle of size nxn is (n^2!), and more precisely the number of possible legal board positions is the number of odd permutations: ((n^2!))/ 2. <br>
For "15-Puzzle" and "24-Puzzle" this is a very large number and creating such a graph can take quite a bit of time, <br> therefore it is necessary to dynamically create the graph while running the search algorithm. <br>
I then moved on to implementing the BFS algorithm when the graph is created dynamically - the first vertex is the initial board state and its neighbors are the possible board states that can be reached from this board.<br> Since BFS detects all possible board states I realized that stopping conditions are also needed.<br>
At first I tried to run BFS on "15-Puzzle" with 5000 random moves from the starting board, the algorithm stopped after 25 (!) minutes due to memory overflow (this after optimizing the graph, before it stopped after 5 minutes).<br>
I understood that the number of random moves is a decisive factor - the maximum number of moves for solving "15-Puzzle" for example is 80, and when the number of random moves (n) is high, the puzzle can be determined with a solution with a high number of moves, which will cause BFS to check almost All possible board modes. <br>
  (A graph with a depth of 54, for example, includes testing 777,302,007,562 different board modes). <br>
Later I implemented the A* algorithm, where the graph is also created dynamically. I set the shortest path weight from the start board to the current board (g) to be the depth of the graph or the "minimum number of moves" from the start board to the current board. <br>
After researching a number of heuristic functions such as "Euclidean distance", "Number of misplaced tiles" and "Number of tiles out of row and column" I discovered that they are all admissible heuristic functions,
That's why I chose to use the heuristic function "Number of premutations inversions" as an inadmissible heuristic function.<br>
The "Number of premutations inversions" function treats the game board as a one-dimensional array, and checks the sum of the inversions required in order for the array to be presented as the solution board - a one-dimensional array in which the numbers are arranged in ascending order with the last place representing the empty tile being 0. <br>
For example, given the following board: <br>
<img src="https://i.postimg.cc/zX8wZBf7/non-admissible-example.png" alt="App" width="398px" height="106px"> <br>
The number of permutations inversions for the solution board is 7 (0 and 13 alternate, 0 and 14 alternate, 0 and 15 alternate, 0 and 12 alternate, then 12 and 15 alternate, 12 and 14 alternate and finally 12 and 13 alternates), where the actual cost to the solution board is 1 (up 12).
After a number of runs of A* with the inadmissible function, I noticed that the behavior of the algorithm cannot be predicted.
In some cases it can estimate an underestimation and in some cases an overestimation, which causes more development of vertices (compared to an admissible heuristic) or less, not finding an optimal solution and in more complicated cases - causing the algorithm not to stop. <br>
The amount of movements in the path found from the starting board to the solution board is equal in every run because in every algorithm it is guaranteed to find the optimal path for the solution, except for certain runs of the inadmissible heuristic (in more difficult puzzles), whose behavior cannot be predicted and sometimes can find a suboptimal solution. <br>
(An optimal solution is unique, or has a single "minimum number of moves for the solution"). <br>
In addition, since each heuristic function is calculated in O(n^2), I created a cache for each function to retrieve repeated calculations.<br>
Another important difference is between BFS and A* with the 0 function. In some cases BFS will develop fewer vertices in the graph because it discovers vertices systematically, level by level, i.e. discovers all possible board states for the same number of moves from the solution board before moving to the next level.
A* with the 0 function, on the other hand, will sometimes prioritize developing long paths that do not lead to a solution before developing the optimal path to a solution, which causes overdevelopment of vertices compared to BFS. <br>
In conclusion, the solution space of a puzzle of size nxn when n>3 is very large, and solving a particular puzzle can be a difficult problem. In the lectures of "24-Puzzle" the calculation time and the number of vertices have increased, because the solution space is significantly larger than in "15-Puzzle".<br>
With the help of a "good" admissible heuristic function, which dominates  other admissible heuristics (Manhattan distance dominates other heuristic I tried such as the Euclidean distance, misplaced tiles, number of tiles out of row and column) you can reach an optimal solution in a significantly shorter time than with The brute-force method, and even solve more "complex" puzzles.
