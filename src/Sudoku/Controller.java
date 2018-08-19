package Sudoku;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.util.Duration;

/**
 * @author Marcin Rudko
 * @version 30.06.2018
 **/

public class Controller {
    Sudoku_View view;
    Sudoku_Model model;

    public Controller(Sudoku_View view, Sudoku_Model model){
        this.view = view;
        this.model = model;

        setGameButtonsAE();
        setChooseButtonsAE();
        setButtonsActions();
        setIsGameSolvedPropertyListener();

    }
    public void setGameButtonsAE(){
        for (int i=0;i<81;i++){
            view.gameButtons[i].setOnAction(gameButtonsActionEvent());
        }
    }
    public EventHandler<ActionEvent> gameButtonsActionEvent(){
        EventHandler<ActionEvent> gameButtonsAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.setButtonIndexToChange(getButtonIndex(event,view.gameButtons));
                view.choseNumberDialog();
            }
        };
        return gameButtonsAction;
    }
    public void setChooseButtonsAE(){
        for (int i=0;i<9;i++){
            view.chooseButtons[i].setOnAction(chooseButtonsActionEvent());
        }
    }
    public EventHandler<ActionEvent> chooseButtonsActionEvent(){
        EventHandler<ActionEvent> chooseButtonAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedButton = ((Button) event.getSource()).getText();
                view.changeButtonDisplayText(view.getButtonIndexToChange(),selectedButton);
                model.changeUnsolvedBoardIndex(view.getButtonIndexToChange(),selectedButton);
            }
        };
        return chooseButtonAction;
    }
    public int getButtonIndex(ActionEvent event, Button[] buttonsTable){
        for(int i=0;i<buttonsTable.length;i++){
            if(event.getSource().equals(buttonsTable[i])){
                return i;
            }
        }
        return 0;
    }
    public void setButtonsActions(){
        this.view.newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
            }
        });
        this.view.decreaseTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(view.getTimerTimeLeftInSec()>10){
                    view.decreaseTimerTimeLeft(10);
                } else view.limitTimerReached();
            }
        });
        this.view.increaseTimeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.increaseTimerTimeLeft(10);
            }
        });

        this.view.levelEasy.setOnAction(setlevelButtonsAE(this.view.levelEasy,0));
        this.view.levelNormal.setOnAction(setlevelButtonsAE(this.view.levelNormal,1));
        this.view.levelHard.setOnAction(setlevelButtonsAE(this.view.levelHard,2));

        this.view.solveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.setTimerGameTimeInSec(0);
                model.setIsGameSolvedProperly(false);
                model.solveGame();
                view.setUpABoard(model.getGameBoard());
            }
        });
        this.view.hintButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    view.placeHint(model.getHint());
                } catch (NullPointerException exeption){
                    view.hintButton.setDisable(true);
                    view.limitHintsReached();
                }
            }
        });
        this.view.restartGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.restartGame();
                stopTimer();
                view.resetTimer();
                view.setGameIsRunning(false);
                view.clearBoard();
                view.setTimerGameTimeInSec(0);
                startGame();
            }
        });
    }

    public EventHandler<ActionEvent> setlevelButtonsAE(ToggleButton toggleButton, int level){
        EventHandler<ActionEvent> levelButtonsAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!toggleButton.isSelected()) {
                    toggleButton.setSelected(true);
                } else { view.setGameLevel(level); }
            }
        };
        return levelButtonsAction;
    }
    public void setIsGameSolvedPropertyListener(){
        this.model.isGameSolved.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(model.getIsGameSolved()) {
                    endGame();
                }
            }
        });
    }
    public void endGame(){
        stopTimer();
        model.setGameTime(view.getGameTimeInSec());
        model.setTimeLeft(view.getTimerTimeLeftInSec());
        model.setScore();
        view.resetTimer();
        view.setGameIsRunning(false);
        view.setBoardButtonsClickable(false);
        view.setLevelButtonsClickable(true);
        view.setControlButtonsClickable(false);
        view.clearBoard();
        view.newGameButton.setText("NOWA GRA");
        view.decreaseTimeButton.setDisable(false);
        view.increaseTimeButton.setDisable(false);
        view.finalScore(model.getScore(), model.getTimerGameTime(), model.getGameLevel(), model.getHintsUsed());
        view.setTimerGameTimeInSec(0);

    }
    public void startGame(){
        if(!view.isGameRunning){
            view.setBoardButtonsClickable(true);
            view.setLevelButtonsClickable(false);
            view.setControlButtonsClickable(true);
            view.setUpABoard(model.getNewGameBoard(view.getGameLevel()));
            startTimer();
            view.setGameIsRunning(true);
            view.newGameButton.setText("ZAKOŃCZ");
            view.decreaseTimeButton.setDisable(true);
            view.increaseTimeButton.setDisable(true);
        } else if (view.isGameRunning){
            stopTimer();
            view.resetTimer();
            view.setGameIsRunning(false);
            view.setBoardButtonsClickable(false);
            view.setLevelButtonsClickable(true);
            view.setControlButtonsClickable(false);
            view.clearBoard();
            view.newGameButton.setText("NOWA GRA");
            view.decreaseTimeButton.setDisable(false);
            view.increaseTimeButton.setDisable(false);
        }
    }

    public void startTimer(){
        this.view.timerTimeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                view.timerGameTime++;
                view.timerTimeLeft--;
                view.timerTextField.setText(view.timerFormat.format(view.timerTimeLeft * 1000));
                if (view.timerTimeLeft <= 10 && view.timerTimeLeft >= 1) {
                    view.timerTextField.setStyle("-fx-font-size: 18; -fx-text-fill: brown");
                } else if (view.timerTimeLeft == 0) {
                    model.setIsGameSolvedProperly(false);
                    endGame();
                }
            }
        });
        this.view.timerTimeline.getKeyFrames().add(keyFrame);
        this.view.timerTimeline.setCycleCount(Animation.INDEFINITE);
        this.view.timerTimeline.play();
    }
    public void stopTimer(){
        this.view.timerTimeline.stop();
    }
}
