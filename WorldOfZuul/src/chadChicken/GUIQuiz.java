package chadChicken;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.util.List;

public class GUIQuiz extends Quiz {

    @FXML
    public Button a1, a2, a3, a4;
    @FXML
    public TextArea questionField;
    private String answer;

    public GUIQuiz(List<Question> preQuestions) {
        super(preQuestions);
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    protected String getAnswerFromUser(Question q) {

        questionField.setText(q.getQ());
        a1.setText(q.A1);
        a2.setText(q.A2);
        a3.setText(q.A3);
        a4.setText(q.A4);


        return null;
    }



}