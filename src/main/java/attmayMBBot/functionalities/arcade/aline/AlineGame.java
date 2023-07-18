package attmayMBBot.functionalities.arcade.aline;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class AlineGame {
    private AlineBoardInfo boardInfo;
    private EAlineDifficulty difficulty;
    private int xpForCompleting;
    private int leastAmountOfMoves;
    public boolean getIsWon(){
        return this.boardInfo.isWon();
    }
    public boolean getIsGameOver(){
        return this.boardInfo.isGameOver();
    }
    public int getAmountOfMoves(){
        return this.boardInfo.getNumberOfMoves();
    }
    public int getXpForCompleting(){
        return this.xpForCompleting;
    }
    public AlineGame(EAlineDifficulty difficulty) {
        this.difficulty = difficulty;
        this.generateBoard(difficulty);
    }
    private void generateBoard(EAlineDifficulty difficulty) {
        //amount of moves for the puzzle
        int targetMin = 0;
        int targetMax = 0;
        int height = 1;
        int width = 1;
        //adjust values for difficulty
        switch(difficulty){
            case EASY:
                targetMin = 6;
                targetMax = 9;
                height = 7;
                width = 7;
                break;
            case MEDIUM:
                targetMin = 10;
                targetMax = 14;
                height = 9;
                width = 13;
                break;
            case HARD:
                targetMin = 15;
                targetMax = 20;
                height = 11;
                width = 17;
                break;
        }

        EAlineTileType[][] board = new EAlineTileType[width][height]; //15 width, 10 height
        //Fill the board with the initial values
        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board[x].length; y++) {
                board[x][y] = EAlineTileType.NONE;
            }
        }

        //Set the starting point
        board[0][board[0].length-1] = EAlineTileType.LINE;
        int currentX = 0;
        int currentY = board[0].length - 1;

        //Set the target
        board[board.length-1][0] = EAlineTileType.TARGET;

        Random random = new Random();

        boolean targetReached = false;

        long start = System.currentTimeMillis();

        while(!targetReached){
            Point p = new Point(random.nextInt(board.length), random.nextInt(board[0].length));
            if(!(board[p.x][p.y] == EAlineTileType.TARGET) && !(p.x == currentX && p.y == currentY)){ //If not starting point and not the target, set a wall at that position.
                if(random.nextDouble() < 0.05) //5% chance of death tile
                    board[p.x][p.y] = EAlineTileType.DEATH;
                else //95% chance of wall tile
                    board[p.x][p.y] = EAlineTileType.WALL;
                //Check the amount of moves for the puzzle
                int movesForPuzzle = getLeastAmountOfMovesForBoard(board, currentX, currentY);
                if(movesForPuzzle != - 1){
                    if(movesForPuzzle <= targetMax && movesForPuzzle >= targetMin){
                        targetReached = true;
                    } else {
                        //Remove 70% of walls if there are more than 22% walls on the map
                        //Remove 70% of death tiles if there are more than 4% death tiles on the map
                        int sumOfTiles = board.length * board[0].length;
                        int sumOfWalls = 0;
                        int sumOfDeaths = 0;
                        for(int x = 0; x < board.length; x++) {
                            for(int y = 0; y < board[x].length; y++) {
                                if(board[x][y] == EAlineTileType.WALL)
                                    sumOfWalls++;
                                if(board[x][y] == EAlineTileType.DEATH)
                                    sumOfDeaths++;
                            }
                        }
                        if((double) sumOfWalls / sumOfTiles > 0.22){ //22% walls
                            for(int x = 0; x < board.length; x++) {
                                for(int y = 0; y < board[x].length; y++) {
                                    if(board[x][y] == EAlineTileType.WALL && random.nextDouble() < 0.7){ //70% chance to remove a wall
                                        board[x][y] = EAlineTileType.NONE;
                                    }
                                }
                            }
                        }
                        if((double) sumOfDeaths / sumOfTiles > 0.04){ //4% of death tiles
                            for(int x = 0; x < board.length; x++) {
                                for(int y = 0; y < board[x].length; y++) {
                                    if(board[x][y] == EAlineTileType.WALL && random.nextDouble() < 0.7){ //70% chance to remove a death tile
                                        board[x][y] = EAlineTileType.NONE;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //Puzzle no longer solvable, remove the wall again
                    board[p.x][p.y] = EAlineTileType.NONE;
                }
            }
        }

        this.leastAmountOfMoves = getLeastAmountOfMovesForBoard(board, currentX, currentY);
        long difference = System.currentTimeMillis() - start;
        System.out.println("");
        System.out.println("Minimal number of moves for this board: " + leastAmountOfMoves);
        System.out.println("This calculation took " + difference + " ms");

        //Create the BoardInfo object
        this.boardInfo = new AlineBoardInfo(board, currentX, currentY);
    }
    private int getLeastAmountOfMovesForBoard(EAlineTileType[][] board, int startX, int startY){
        //Note that this method does not take a boardInfo object as an argument because this method will be called before the final boardInfo is generated
        //This method will return the least amount of moves needed to reach the target from the starting position for the given board

        //breadth first search - shoutsout to Gaubbe for telling me about this algorithm

        //create the first AlineBoardInfo instance from the initial state of the game.
        AlineBoardInfo startInfo = new AlineBoardInfo(BoardDeepCloner.deepCloneBoard(board), startX, startY, 0);
        //create the queue
        Queue<AlineBoardInfo> queue = new LinkedList<>();
        //add the "root" to the queue
        queue.add(startInfo);

        //Loop until all possible paths are taken into account
        while(!queue.isEmpty()) {
            //Dequeue the next element in the queue
            AlineBoardInfo current = queue.remove();
            //Check for the condition
            if (current.isWon())
                return current.getNumberOfMoves();
            //Only try to move if the current state of the game is not game over
            if (!current.isGameOver()) {
                //Move in all the possible directions from this current board and add them to the queue (if they're possible)
                AlineBoardInfo newInfo;
                //LEFT
                if (moveLeftPossible(current.getBoard(), current.getCurrentX(), current.getCurrentY())) {
                    newInfo = current.clone();
                    this.applyMovementOnGrid(newInfo, EAlineMovementDirection.LEFT);
                    queue.add(newInfo);
                }
                //RIGHT
                if (moveRightPossible(current.getBoard(), current.getCurrentX(), current.getCurrentY())) {
                    newInfo = current.clone();
                    this.applyMovementOnGrid(newInfo, EAlineMovementDirection.RIGHT);
                    queue.add(newInfo);
                }
                //UP
                if (moveUpPossible(current.getBoard(), current.getCurrentX(), current.getCurrentY())) {

                    newInfo = current.clone();
                    this.applyMovementOnGrid(newInfo, EAlineMovementDirection.UP);
                    queue.add(newInfo);
                }
                //DOWN
                if (moveDownPossible(current.getBoard(), current.getCurrentX(), current.getCurrentY())) {

                    newInfo = current.clone();
                    this.applyMovementOnGrid(newInfo, EAlineMovementDirection.DOWN);
                    queue.add(newInfo);
                }
            }
        }
        //Default case which means that there was no path that led to the target :(
        return -1;
    }
    public void moveInDirection(EAlineMovementDirection direction){
        //Apply the movement with the actual data of the game
        this.applyMovementOnGrid(this.boardInfo, direction);
    }
    private void applyMovementOnGrid(AlineBoardInfo boardInfo, EAlineMovementDirection direction){
        //Get the offset for the direction
        int xOffset = 0;
        int yOffset = 0;
        switch(direction){
            case UP:
                yOffset = -1;
                break;
            case DOWN:
                yOffset = 1;
                break;
            case LEFT:
                xOffset = -1;
                break;
            case RIGHT:
                xOffset = 1;
                break;
        }
        //move until it is no longer possible
        boolean canMove = true;
        boolean movementHappened = false;
        while(canMove){
            int provisionalX = boardInfo.getCurrentX() + xOffset;
            int provisionalY = boardInfo.getCurrentY() + yOffset;

            if(provisionalX < 0 || provisionalX >= boardInfo.getBoard().length || provisionalY < 0 || provisionalY >= boardInfo.getBoard()[0].length) //Hit the end of the board
                canMove = false;
            else if(boardInfo.getBoard()[provisionalX][provisionalY] == EAlineTileType.WALL) //Hit a wall
                canMove = false;
            else if(boardInfo.getBoard()[provisionalX][provisionalY] == EAlineTileType.LINE) //Hit a line (itself)
                canMove = false;
            else if (boardInfo.getBoard()[provisionalX][provisionalY] == EAlineTileType.TARGET) { //Hit the target
                movementHappened = true;
                boardInfo.setWon(true);
                canMove = false; //Stop movement after winning the game
                boardInfo.setCurrentX(provisionalX);
                boardInfo.setCurrentY(provisionalY);
                boardInfo.getBoard()[boardInfo.getCurrentX()][boardInfo.getCurrentY()] = EAlineTileType.TARGET_HIT;
            } else if (boardInfo.getBoard()[provisionalX][provisionalY] == EAlineTileType.DEATH){ //Hit the death trigger
                movementHappened = true;
                boardInfo.setGameOver(true);
                canMove = false;
                boardInfo.setCurrentX(provisionalX);
                boardInfo.setCurrentY(provisionalY);
                boardInfo.getBoard()[boardInfo.getCurrentX()][boardInfo.getCurrentY()] = EAlineTileType.DEATH_ACTIVATED;
            }
            else{
                //Movement is possible, that means the provisional position is used to overwrite the current position and the grid gets set to a Line
                movementHappened = true;
                boardInfo.setCurrentX(provisionalX);
                boardInfo.setCurrentY(provisionalY);
                boardInfo.getBoard()[boardInfo.getCurrentX()][boardInfo.getCurrentY()] = EAlineTileType.LINE;
            }
        }
        //If movement occurred, increment the number of moves in the boardInfo object
        if(movementHappened)
            boardInfo.setNumberOfMoves(boardInfo.getNumberOfMoves() + 1);
        //Check if there are moves possible, but only if the game is not won yet
        if(!boardInfo.isWon())
            if(!this.movePossible(boardInfo.getBoard(), boardInfo.getCurrentX(), boardInfo.getCurrentY()))
                boardInfo.setGameOver(true);
        //Set the xp you get for completing this if the game is won
        if(this.boardInfo != null) { //This code only gets executed if the boardInfo object for this instance has been created already
            if (boardInfo.isWon()) {
                int maxXp = 0;
                int minXp = 0;
                int xpRemoveRate = 0;
                switch (this.difficulty) {
                    case EASY:
                        maxXp = 30;
                        minXp = 20;
                        xpRemoveRate = 2;
                        break;
                    case MEDIUM:
                        maxXp = 65;
                        minXp = 40;
                        xpRemoveRate = 3;
                        break;
                    case HARD:
                        maxXp = 100;
                        minXp = 80;
                        xpRemoveRate = 4;
                        break;
                }
                this.xpForCompleting = maxXp - xpRemoveRate * (this.boardInfo.getNumberOfMoves() - this.leastAmountOfMoves);
                if (this.xpForCompleting < minXp) //You can not get less than this amount of xp
                    this.xpForCompleting = minXp;
            }
        }
    }
    public void giveUp(){
        this.boardInfo.setGameOver(true);
    }
    private boolean movePossible(EAlineTileType[][] grid, int x, int y){
        return this.moveLeftPossible(grid, x, y) ||
                this.moveRightPossible(grid, x, y) ||
                this.moveUpPossible(grid, x, y) ||
                this.moveDownPossible(grid, x, y);
    }
    private boolean moveLeftPossible(EAlineTileType[][] grid, int x, int y){
        if(x > 0) //not all the way on the left
            if(grid[x - 1][y] != EAlineTileType.WALL && grid[x - 1][y] != EAlineTileType.LINE) //Block left is not wall nor line
                return true;
        return false;
    }
    private boolean moveRightPossible(EAlineTileType[][] grid, int x, int y){
        if(x < grid.length - 1) //not all the way on the right
            if(grid[x + 1][y] != EAlineTileType.WALL && grid[x + 1][y] != EAlineTileType.LINE) //Block right is not wall nor line
                return true;
        return false;
    }
    private boolean moveUpPossible(EAlineTileType[][] grid, int x, int y){
        if(y > 0) //not all the way on the top
            if(grid[x][y - 1] != EAlineTileType.WALL && grid[x][y - 1] != EAlineTileType.LINE) //Block top is not wall nor line
                return true;
        return false;
    }
    private boolean moveDownPossible(EAlineTileType[][] grid, int x, int y){
        if(y < grid[0].length - 1) //not all the way on the bottom
            if(grid[x][y + 1] != EAlineTileType.WALL && grid[x][y + 1] != EAlineTileType.LINE) //Block bottom is not wall nor line
                return true;
        return false;
    }
    private String getStringFromTileType(EAlineTileType tileType){
        switch(tileType){
            case NONE:
                return "\u2B1B"; //:black_large_square: 	\u2B1C is white large square
            case WALL:
                return "\uD83D\uDD32"; //:black_square_button: 	\uD83D\uDD33 is white square button
            case LINE:
                return "\uD83D\uDFE5"; //:red_square:
            case TARGET:
                return "\uD83D\uDD37"; //:large_blue_diamond:
            case TARGET_HIT:
                return "\u2705"; //:white_check_mark:
            case DEATH:
                return "\uD83D\uDC80"; //:skull:
            case DEATH_ACTIVATED:
                return "\u2620\uFE0F"; //:skull_with_bones:
        }
        return "";
    }
    public String getBoardString(){
        StringBuilder sb = new StringBuilder();
        for(int y = 0; y < this.boardInfo.getBoard()[0].length; y++) {
            for(int x = 0; x < this.boardInfo.getBoard().length; x++) {
                sb.append(this.getStringFromTileType(this.boardInfo.getBoard()[x][y]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}