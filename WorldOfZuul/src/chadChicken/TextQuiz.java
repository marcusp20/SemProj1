package chadChicken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TextQuiz extends Quiz {
    Scanner reader;
    public TextQuiz(List<Question> preQuestions) {
        super(preQuestions);
        reader = new Scanner(System.in);
    }

    @Override
    protected String getAnswerFromUser(Question q) {
        System.out.println(q.Q);
        ArrayList<String> answers = new ArrayList<>();
        answers.add(q.A1);
        answers.add(q.A2);
        answers.add(q.A3);
        answers.add(q.A4);

        Collections.shuffle(answers);
        for(int i = 0; i < answers.size(); i++) {
            System.out.println(i + ": " + answers.get(i));
        }


        String answer = null;

        while (answer == null) {
            System.out.print("Type the number of you answer > ");
            String inputLine = reader.nextLine().strip();
            try {
                int index = Integer.parseInt(inputLine);
                answer = answers.get(index);

            } catch (NumberFormatException nfe) {
                System.out.println("That's not a number!");
                answer = null;
            } catch (IndexOutOfBoundsException eobe) {
                System.out.println("Please write a number between 0 & " + (answers.size() - 1));
                answer = null;
            }
        }
        return answer;
    }
}
