package Sudoku;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

/**
 * @author Marcin Rudko
 * @version 30.06.2018
 **/

public class Sudoku_View extends Application {

    VBox mainPane;
    VBox gamePane;
    HBox timerPane;
    HBox levelPane;
    TilePane buttonsPane;

    TextField timerTextField;
    Timeline timerTimeline;
    SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss");
    Button increaseTimeButton;
    Button decreaseTimeButton;
    int timerTimeLeft = 300;
    int timerGameTime = 0;

    boolean isGameRunning = false;
    int gameLevel = 0;

    Button newGameButton = new Button("NOWA GRA");
    Button restartGameButton = new Button("RESTART");
    Button saveGameButton = new Button("ZAPISZ GRĘ");
    Button loadGameButton = new Button("WCZYTAJ GRĘ");
    Button hintButton = new Button("PODPOWIEDŹ");
    Button solveButton = new Button("ROZWIĄŻ");
    Button saveGameBoard = new Button("ZAPISZ PLANSZĘ");
    Button loadGameBoard = new Button("WCZYTAJ PLANSZĘ");

    ToggleButton levelEasy = new ToggleButton("ŁATWY");
    ToggleButton levelNormal = new ToggleButton("NORMALNY");
    ToggleButton levelHard = new ToggleButton("TRUDNY");

    int buttonIndexToChange = 0;

    Button[] gameButtons = new Button[81];
    Button[] chooseButtons = new Button[9];
    int gameButtonsCounter = 0;

    Image iconImage = new Image(getClass().getResourceAsStream("sudoku_icon.png"));

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(getMainPane(),600,600));
        initControllerAndModel();
        primaryStage.getIcons().add(iconImage);
        primaryStage.setTitle(" - SUDOKU - ");
        primaryStage.show();
    }
    public VBox getMainPane(){
        createGamePane();
        createTimerTextField();
        setChooseButtons();
        createButtonsPane();
        createLevelPane();

        this.mainPane = new VBox();
        this.mainPane.setAlignment(Pos.TOP_CENTER);

        VBox.setMargin(this.timerPane,new Insets(10,10,5,10));
        VBox.setMargin(this.levelPane,new Insets(5,10,5,10));
        VBox.setMargin(this.gamePane,new Insets(5,10,10,10));

        this.mainPane.getChildren().add(this.timerPane);
        this.mainPane.getChildren().add(this.levelPane);
        this.mainPane.getChildren().add(this.gamePane);
        this.mainPane.getChildren().add(this.buttonsPane);

        return this.mainPane;
    }

    public void createGamePane(){
        createGameButtons();
        this.gamePane = new VBox();
        this.gamePane.setSpacing(5);
        for (int i=1;i<=9;i++){
            this.gamePane.getChildren().add(getGameButtonsRow());
        }

    }
    public TilePane getGameButtonsRow(){
        TilePane tileRow = new TilePane();
        tileRow.setAlignment(Pos.CENTER);
        tileRow.setPrefColumns(9);
        tileRow.setVgap(5);
        tileRow.setHgap(5);
        tileRow.setMinSize(270,30);
        fillRowWithGameButtons(tileRow);
        return tileRow;
    }
    public void fillRowWithGameButtons(TilePane tileRow){
        for (int i=1;i<=9;i++){
            tileRow.getChildren().add(gameButtons[gameButtonsCounter]);
            gameButtonsCounter++;
        }
    }
    public void createGameButtons(){
        for(int i=1;i<=81;i++){
            gameButtons[i-1] = new Button("");
            gameButtons[i-1].setMinSize(38,38);
            gameButtons[i-1].setDisable(true);
        }
    }
    public void setUpABoard(int[] boardButtons){
        for (int i=0;i<boardButtons.length;i++) {
            if(boardButtons[i]==0) {
                this.gameButtons[i].setText("");
                this.gameButtons[i].setDisable(false);
            }
            else {
                this.gameButtons[i].setText(boardButtons[i]+"");
                this.gameButtons[i].setDisable(true);
            }
        }
    }
    public void clearBoard(){
        this.timerTextField.setStyle(null);
        for(int i=0;i<81;i++){
            gameButtons[i].setText("");
            gameButtons[i].setStyle(null);
        }
    }
    public void setBoardButtonsClickable(boolean clickable){
        for(int i=1;i<=81;i++){
            gameButtons[i-1].setDisable(!clickable);
        }
    }
    public void createTimerTextField(){
        this.timerPane = new HBox();
        this.timerPane.setAlignment(Pos.CENTER);
        this.timerPane.setSpacing(5);

        this.decreaseTimeButton = new Button("<");
        this.increaseTimeButton = new Button(">");

        this.timerTextField = new TextField();
        this.timerTextField.setText(this.timerFormat.format(timerTimeLeft*1000));
        this.timerTextField.setAlignment(Pos.CENTER);
        this.timerTextField.setEditable(false);
        this.timerTextField.setMaxSize(100,20);

        this.timerPane.getChildren().add(this.decreaseTimeButton);
        this.timerPane.getChildren().add(this.timerTextField);
        this.timerPane.getChildren().add(this.increaseTimeButton);

    }

    public Alert limitTimerReached(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Osiągnięto limit timera.");
        alert.setContentText("Nie można ustawić timera poniżej 10s.");
        alert.showAndWait();
        return alert;
    }
    public Alert limitHintsReached(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Osiągnięto limit podpowiedzi.");
        alert.setContentText("Plansza prawie rozwiązana !"+"\n"+"Pozostało jedno pole do uzupełniania.");
        alert.showAndWait();
        return alert;
    }
    public Alert finalScore(double score, int time, int level, int hints){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Gra zakończona.");
        alert.setContentText("TWÓJ WYNIK: " + score + "\n\n" + "Poziom gry: "+ level + "\n"+"Czas gry: " + timerFormat.format(time*1000) + "\n" +"Podpowiedzi: "+hints);
        alert.show();
        return alert;
    }

    public void resetTimer(){
        this.timerTimeLeft = 300;
        this.timerTextField.setText(timerFormat.format(timerTimeLeft*1000));
    }
    public void setTimerGameTimeInSec(int time){
        this.timerGameTime = time;
    }
    public int getGameTimeInSec(){
        return this.timerGameTime;
    }
    public void decreaseTimerTimeLeft(int sec){
        this.timerTimeLeft -= sec;
        this.timerTextField.setText(timerFormat.format(timerTimeLeft*1000));
    }
    public void increaseTimerTimeLeft(int sec){
        this.timerTimeLeft += sec;
        this.timerTextField.setText(timerFormat.format(timerTimeLeft*1000));
    }
    public int getTimerTimeLeftInSec(){
        return this.timerTimeLeft;
    }
    public void setGameIsRunning(boolean isRunning){
        this.isGameRunning = isRunning;
    }

    public Dialog choseNumberDialog(){
        TilePane dialogPane = new TilePane();
        dialogPane.setPrefRows(3);
        dialogPane.setPrefColumns(3);
        dialogPane.setAlignment(Pos.CENTER);
        dialogPane.setVgap(5);
        dialogPane.setHgap(5);
        dialogPane.setMaxSize(150,90);

        Dialog dialog = new Dialog();
        dialog.setHeaderText("Wstaw liczbę:");

        for(int i=1;i<=9;i++){
            dialogPane.getChildren().add(chooseButtons[i-1]);
        }

        dialog.getDialogPane().setContent(dialogPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        dialog.showAndWait();
        return dialog;

    }
    public void changeButtonDisplayText(int buttonsTableIndex, String buttonText){
        this.gameButtons[buttonsTableIndex].setText(buttonText);
    }
    public void setButtonIndexToChange(int buttonsTableIndex){
        this.buttonIndexToChange = buttonsTableIndex;
    }
    public int getButtonIndexToChange(){
        return this.buttonIndexToChange;
    }
    public void placeHint(int[] hint){
        this.gameButtons[hint[0]].setText(Integer.toString(hint[1]));
        this.gameButtons[hint[0]].setDisable(true);
        this.gameButtons[hint[0]].setStyle("-fx-background-color: red");
    }

    public void setChooseButtons() {
        for(int i=1;i<=9;i++){
            this.chooseButtons[i-1] = new Button(i+"");
            this.chooseButtons[i-1].setMinSize(38,38);
        }
    }
    public void createLevelPane(){
        this.levelPane = new HBox();
        this.levelPane.setAlignment(Pos.CENTER);
        this.levelPane.setSpacing(5);

        ToggleGroup levelGroup = new ToggleGroup();
        this.levelEasy.setToggleGroup(levelGroup);
        this.levelEasy.setSelected(true);
        this.levelNormal.setToggleGroup(levelGroup);
        this.levelHard.setToggleGroup(levelGroup);

        this.levelPane.getChildren().add(this.levelEasy);
        this.levelPane.getChildren().add(this.levelNormal);
        this.levelPane.getChildren().add(this.levelHard);
    }
    public void setGameLevel(int selectedLevel){
        this.gameLevel = selectedLevel;
    }
    public int getGameLevel(){
        return this.gameLevel;
    }
    public void setLevelButtonsClickable(boolean clickable){
        this.levelEasy.setDisable(!clickable);
        this.levelNormal.setDisable(!clickable);
        this.levelHard.setDisable(!clickable);
    }

    public void createButtonsPane(){
        this.buttonsPane = new TilePane();
        this.buttonsPane.setAlignment(Pos.CENTER);
        this.buttonsPane.setPrefColumns(2);
        this.buttonsPane.setPrefRows(3);
        this.buttonsPane.setHgap(5);
        this.buttonsPane.setVgap(5);

        this.restartGameButton.setDisable(true);
        this.hintButton.setDisable(true);
        this.saveGameButton.setDisable(true);
        this.saveGameBoard.setDisable(true);
        this.solveButton.setDisable(true);

        this.buttonsPane.getChildren().add(this.newGameButton);
        this.buttonsPane.getChildren().add(this.restartGameButton);
        this.buttonsPane.getChildren().add(this.hintButton);
        this.buttonsPane.getChildren().add(this.solveButton);
        //this.buttonsPane.getChildren().add(this.saveGameButton);
        //this.buttonsPane.getChildren().add(this.loadGameButton);
        //this.buttonsPane.getChildren().add(this.saveGameBoard);
        //this.buttonsPane.getChildren().add(this.loadGameBoard);
    }
    public void setControlButtonsClickable(boolean clickable){
        this.restartGameButton.setDisable(!clickable);
        this.hintButton.setDisable(!clickable);
        this.saveGameButton.setDisable(!clickable);
        this.saveGameBoard.setDisable(!clickable);
        this.solveButton.setDisable(!clickable);
        this.loadGameButton.setDisable(clickable);
        this.loadGameBoard.setDisable(clickable);
    }

    public void initControllerAndModel(){
        Sudoku_Model model = new Sudoku_Model();
        Controller controller = new Controller(this,model);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
