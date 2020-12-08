package chadChicken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChadChicken {
    private List<Question> preQuestions;
    private List<Question> postQuestions;

    public ChadChicken() {
        initQuestionLists();
        initFireBase();

    }

    private void initFireBase() {
        //TODO implement firebase
        //Create unique hash as userID based of timestamp
        //init firebase object
    }

    private void initQuestionLists() {
        preQuestions = new ArrayList<>();
        preQuestions.add(Question.PRE_Q1);
        preQuestions.add(Question.PRE_Q2);

        postQuestions = new ArrayList<>();
        postQuestions.add(Question.POST_Q1);
        postQuestions.add(Question.POST_Q2);

    }

    public void uploadAnswers(HashMap<Question, String> answers) {
        //Upload answers to firebase
    }

    public List<Question> getPreQuestions() {
        return this.preQuestions;
    }

    public List<Question> getPostQuestions() {
        return postQuestions;
    }
}
