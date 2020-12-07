package chadChicken;

import GUI.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GUIQuiz extends Quiz {

    @FXML
    public Button a1, a2, a3, a4;
    @FXML
    public TextArea questionField;
    private Iterator<Question> questionIterator;
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
        questionField.setText(q.getQ());
        a1.setText(q.A1);
        a1.setOnAction(e -> storeAnswer(q, q.A1));
        a2.setText(q.A2);
        a2.setOnAction(e -> storeAnswer(q, q.A2));
        a3.setText(q.A3);
        a2.setOnAction(e -> storeAnswer(q, q.A3));
        a4.setText(q.A4);
        a2.setOnAction(e -> storeAnswer(q, q.A4));

        return null;
    }

    public void show(Main main, Stage stage) {
        this.main = main;
        Pane p = new Pane();
        try {
            p.getChildren().add(FXMLLoader.load(getClass().getResource("chickenChadGUI.fxml")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        p.setPrefSize(1280, 832);

        this.stage = stage;
        this.stage.setScene(new Scene(p));
        this.stage.show();
        this.stage.setTitle("FARMVILL 99 RETARDO EDITION");
        this.stage.setOpacity(0);



        /*a1.setOnAction(e -> run());
        a1.setText("Continue");
        a2.setText("");
        a3.setText("");
        a4.setText("");*/

    }


}