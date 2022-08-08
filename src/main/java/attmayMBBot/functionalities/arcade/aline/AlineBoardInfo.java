package attmayMBBot.functionalities.arcade.aline;

public class AlineBoardInfo implements Cloneable{
    //This is a class is used to store the board and the current position of the red line and possibly other information
    private EAlineTileType[][] board;
    private int currentX;
    private int currentY;
    private boolean isWon;
    private boolean isGameOver;
    private int numberOfMoves;

    public EAlineTileType[][] getBoard() {
        return board;
    }
    public int getCurrentX() {
        return currentX;
    }
    public int getCurrentY() {
        return currentY;
    }
    public boolean isWon() {
        return isWon;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public AlineBoardInfo(EAlineTileType[][] board, int currentX, int currentY) {
        this.board = board;
        this.currentX = currentX;
        this.currentY = currentY;
        this.isWon = false;
        this.isGameOver = false;
        this.numberOfMoves = 0;
    }

    public AlineBoardInfo(EAlineTileType[][] board, int currentX, int currentY, int numberOfMoves) {
        this.board = board;
        this.currentX = currentX;
        this.currentY = currentY;
        this.isWon = false;
        this.isGameOver = false;
        this.numberOfMoves = numberOfMoves;
    }

    @Override
    public AlineBoardInfo clone() {
        AlineBoardInfo clone = new AlineBoardInfo(BoardDeepCloner.deepCloneBoard(this.board), this.currentX, this.currentY, this.numberOfMoves);
        clone.isWon = this.isWon;
        clone.isGameOver = this.isGameOver;
        return clone;
    }
}
