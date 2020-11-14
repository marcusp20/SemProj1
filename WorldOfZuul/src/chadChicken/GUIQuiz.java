package chadChicken;

import java.util.List;

public class GUIQuiz extends Quiz{

    public GUIQuiz(List<Question> questions) {
        super(questions);
    }

    @Override
    protected String getAnswerFromUser(Question q) {
        //TODO implement gui
        return null;
    }
}
