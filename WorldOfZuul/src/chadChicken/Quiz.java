package chadChicken;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class Quiz {
    private HashMap<Question, String> answers;
    protected List<Question> questions;

    public Quiz(List<Question> questions) {
        answers = new HashMap<>();
        this.questions = questions;
    }

    public void run() {
        Collections.shuffle(questions);
        for (Question q : questions) {
            String answer = getAnswerFromUser(q);
            answers.put(q, answer);
        }
    }

    protected abstract String getAnswerFromUser(Question q);

    public HashMap<Question, String> getAnswers(){
        return this.answers;
    }
}
