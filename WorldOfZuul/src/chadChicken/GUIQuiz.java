package chadChicken;

import GUI.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GUIQuiz extends Quiz {

    public GUIQuiz(List<Question> questions) {
        super(questions);
    }

    @Override
    protected String getAnswerFromUser(Question q) {
        return null;
    }

    /*private Iterator<Question> questionIterator;
    private Stage stage;
    private Main main;

    public GUIQuiz(List<Question> questions) {
        super(questions);
    }

    private void storeAnswer(Question question, String answer) {
        getAnswers().put(question, answer);

        if(questionIterator.hasNext()) {
            getAnswerFromUser(questionIterator.next());
        } else {
            returnToGame();
        }
    }

    private void returnToGame() {
        fadeOut(stage);
        stage.close();
        main.startGame(stage);
    }

    public void fadeOut(Stage stage) {
        for (double i = 1; i >= 0.01; i = i - 0.0001) {
            stage.setOpacity(i);
        }
    }

    @Override
    public void run() {
        Collections.shuffle(questions);
        questionIterator = questions.listIterator();
        getAnswerFromUser(questionIterator.next());
    }

    @Override
    protected String getAnswerFromUser(Question q) {
        main.questionField.setText(q.getQ());
        main.a1.setText(q.A1);
        main.a1.setOnAction(e -> storeAnswer(q, q.A1));
        main.a2.setText(q.A2);
        main.a2.setOnAction(e -> storeAnswer(q, q.A2));
        main.a3.setText(q.A3);
        main.a2.setOnAction(e -> storeAnswer(q, q.A3));
        main.a4.setText(q.A4);
        main.a2.setOnAction(e -> storeAnswer(q, q.A4));

        return null;
    }

    public void show(Main main, Stage stage) {
        this.main = main;
        Pane p = new Pane();
        try {
            AnchorPane fxmlAnchorPane = FXMLLoader.load(getClass().getResource("chickenChadGUI.fxml"));
            p.getChildren().add(fxmlAnchorPane);
            AnchorPane innerFxmlAnchorPane = (AnchorPane) fxmlAnchorPane.getChildren().get(0);
            VBox vBox = (VBox) innerFxmlAnchorPane.getChildren().get(0);
            main.questionField = (TextArea) vBox.getChildren().get(0);
            VBox innerVBox = (VBox) vBox.getChildren().get(1);
            main.a1 = (Button) innerVBox.getChildren().get(0);
            main.a2 = (Button) innerVBox.getChildren().get(1);
            main.a3 = (Button) innerVBox.getChildren().get(2);
            main.a4 = (Button) innerVBox.getChildren().get(3);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        p.setPrefSize(1280, 832);

        this.stage = stage;
        this.stage.setScene(new Scene(p));
        this.stage.show();
        this.stage.setTitle("FARMVILL 99 RETARDO EDITION");
        this.stage.setOpacity(0);



        main.a1.setOnAction(e -> run());
        main.a1.setText("Continue");
        main.a2.setText("");
        main.a3.setText("");
        main.a4.setText("");

    }

*/
}