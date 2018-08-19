package Sudoku;

import javafx.beans.property.SimpleBooleanProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Rudko
 * @version 30.06.2018
 **/

public class Sudoku_Model {
    public Object[] solvedGameBoards = new Object[3];
    public Object[] unsolvedGameBoards = new Object[3];
    int[] tempSolvedBoard;
    int[] tempUnsolvedBoard;
    public int[] solvedBoard;
    public int[] unsolvedBoard;
    public int timerTimeLeft;
    public int timerGameTime;
    public int gameLevel = 0;
    public int hints = 0;
    public double score = 0;
    SimpleBooleanProperty isGameSolved = new SimpleBooleanProperty(false);
    boolean gameSolvedProperly = true;


    public Sudoku_Model(){
        generateBoards();
    }

    public void generateBoards(){
        int[] easySolved = {8,5,2,9,1,3,6,4,7,4,7,3,8,6,2,1,9,5,9,6,1,5,4,7,2,8,3,7,1,8,4,3,5,9,2,6,5,4,9,7,2,6,8,3,1,3,2,6,1,8,9,7,5,4,6,3,7,2,9,4,5,1,8,1,9,4,6,5,8,3,7,2,2,8,5,3,7,1,4,6,9};
        int[] normalSolved = {7,5,1,6,3,4,8,9,2,9,4,6,8,5,2,1,3,7,2,3,8,7,9,1,4,5,6,5,2,7,3,4,8,6,1,9,1,8,4,5,6,9,2,7,3,3,6,9,2,1,7,5,4,8,8,1,2,4,7,3,9,6,5,6,9,3,1,8,5,7,2,4,4,7,5,9,2,6,3,8,1};
        int[] hardSolved = {2,3,4,5,8,6,9,7,1,7,5,8,4,1,9,2,6,3,6,1,9,7,2,3,8,5,4,3,9,2,6,7,8,4,1,5,8,7,1,3,4,5,6,9,2,4,6,5,1,9,2,3,8,7,5,2,3,9,6,1,7,4,8,9,8,7,2,5,4,1,3,6,1,4,6,8,3,7,5,2,9};

        int[] easyUnsolved = {0,5,2,9,1,3,6,4,7,4,7,0,8,6,2,1,9,5,9,6,1,5,4,7,2,0,3,0,1,8,4,3,5,9,2,6,0,4,9,7,2,6,8,3,1,3,2,6,1,8,9,7,5,4,6,3,7,2,9,4,5,1,8,1,9,4,6,5,8,3,7,2,2,8,5,3,7,1,4,6,9};
        int[] normalUnsolved = {0,0,1,6,3,4,0,9,2,9,4,0,8,5,2,1,3,7,0,0,0,7,9,1,4,5,6,0,2,7,3,4,8,6,1,0,1,8,4,5,6,9,2,0,3,0,6,9,2,0,7,5,4,8,8,1,2,4,7,3,9,6,5,6,9,3,1,8,5,7,2,4,4,7,5,9,2,6,3,8,1};
        int[] hardUnsolved = {2,0,0,5,8,6,9,7,1,7,5,0,0,1,9,0,0,3,6,1,0,0,2,3,0,0,4,3,0,2,6,7,0,4,1,0,0,0,0,3,4,5,6,0,0,4,6,5,1,0,0,3,8,7,5,2,3,9,6,1,7,4,8,9,8,7,2,5,4,1,3,6,1,4,6,8,3,7,5,2,9};

        this.solvedGameBoards[0] = easySolved;
        this.solvedGameBoards[1] = normalSolved;
        this.solvedGameBoards[2] = hardSolved;
        this.unsolvedGameBoards[0] = easyUnsolved;
        this.unsolvedGameBoards[1] = normalUnsolved;
        this.unsolvedGameBoards[2] = hardUnsolved;
    }

    public int[] getNewGameBoard(int gameLevel){
        this.timerTimeLeft = 0;
        this.timerGameTime = 0;
        this.hints = 0;
        this.score = 0;
        this.gameLevel = gameLevel;
        this.solvedBoard = new int[81];
        this.unsolvedBoard = new int[81];
        fillNewBoards(this.gameLevel);
        setIsGameSolved();
        return this.unsolvedBoard;
    }
    public void fillNewBoards(int gameLevel){
        this.tempSolvedBoard = (int[]) this.solvedGameBoards[gameLevel];
        this.tempUnsolvedBoard = (int[]) this.unsolvedGameBoards[gameLevel];
        for(int i=0;i<81;i++) {
            this.solvedBoard[i] = tempSolvedBoard[i];
            this.unsolvedBoard[i] = tempUnsolvedBoard[i];
        }
    }
    public int[] getGameBoard(){
        return this.unsolvedBoard;
    }
    public int getHintsUsed(){
        return this.hints;
    }
    public int getTimerGameTime(){
        return this.timerGameTime;
    }
    public double getScore(){
        return this.score;
    }
    public int getGameLevel(){
        return this.gameLevel;
    }
    public void changeUnsolvedBoardIndex(int index, String value){
        this.unsolvedBoard[index] = Integer.parseInt(value);
        setIsGameSolved();
    }
    public void saveGame(){

    }
    public void loadGame(){

    }
    public void restartGame(){
        for(int i=0;i<81;i++) {
            this.solvedBoard[i] = tempSolvedBoard[i];
            this.unsolvedBoard[i] = tempUnsolvedBoard[i];
        }
    }
    public int[] getHint(){
        this.hints += 1;
        List<Integer> posibleMoves = new ArrayList<>();
        for(int i=0;i<81;i++){
            if(this.unsolvedBoard[i]==0){
                posibleMoves.add(i);
            }
        }
        if(posibleMoves.size()>1) {
            int buttonIndex = posibleMoves.get((int) (Math.random() * (posibleMoves.size() - 1)));
            int buttonText = this.solvedBoard[buttonIndex];
            int[] hint = {buttonIndex, buttonText};
            changeUnsolvedBoardIndex(buttonIndex, Integer.toString(buttonText));
            return hint;
        } else return null;
    }
    public void solveGame(){
        this.score = 0;
        this.timerGameTime = 0;
        this.timerTimeLeft = 0;
        for (int i=0;i<81;i++){
            this.unsolvedBoard[i] = this.solvedBoard[i];
        }
        setIsGameSolved();
    }
    public void setTimeLeft(int timerTimeLeft){
        this.timerTimeLeft = timerTimeLeft;
    }
    public void setGameTime(int timerGameTime){
        this.timerGameTime = timerGameTime;
    }
    public void setScore(){
        if(gameSolvedProperly) {
            this.score = ((1000 * (this.gameLevel + 1)) / (this.hints + 1)) - this.timerGameTime;
        } else this.score = 0;
    }
    public void setIsGameSolvedProperly(boolean isGameSolvedProperly){
        this.gameSolvedProperly= false;
    }
    public void setIsGameSolved(){
        boolean solved = true;
        for (int i=0;i<81;i++){
            if(this.unsolvedBoard[i]!=this.solvedBoard[i]){
                solved = false;
            }
        }
        this.isGameSolved.set(solved);
    }
    public boolean getIsGameSolved(){
        return this.isGameSolved.getValue();
    }

}
