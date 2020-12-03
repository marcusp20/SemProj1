package interactable;

import GUI.NpcButtonCell;
import game.Command;
import game.CommandWord;
import game.CommandWords;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class NPC extends Interactable{
    private String name;                 //NPC name
    private String description;         //NPC description
    private File dialog;                //File containing NPC dialog
    private ArrayList<String> lines = new ArrayList<>();        //Array list containing all dialog lines
    private boolean firstMeeting = true;        //Attribute is true if player has not met npc

    //GUI
    private VBox npcWindow = new VBox();
    Text dialogText = new Text();

    public NPC(File dialog)   {
        super();
        this.dialog = dialog;

        try {
            Scanner dialogReader= new Scanner(dialog);

            while(dialogReader.hasNextLine())   {
                String line = dialogReader.nextLine();      //Gets line from file
                lines.add(line);                            //Adds lines to lines arraylist
            }

        } catch (FileNotFoundException e)  {
            System.out.println("File not found");
        }

        this.name = findLine(".nn");
        this.description = findLine(".dd");

        //GUI
        createNpcWindow();
    }


    public String getName() {
        return name;
    }

    private String findLine(String pattern)   {             //Find line with given patten
        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).contains(pattern))  {
                String line = lines.get(i);

                for (int j = 0; j < pattern.length(); j++) {
                    line = line.substring(1);
                }
                line = line.substring(1);

                return line;
            }
        }
        return "Line not found";
    }

    private void writePatternLines(String oPattern)    {              //Write lines containing pattern in order (top down)
        int lineCount = 0;

        while(true) {
            String pattern = oPattern + lineCount;
            lineCount++;
            if (!findLine(pattern).equals("Line not found"))    {
                write(findLine(pattern));
            } else  {
                break;
            }
        }
    }

    private ArrayList<String> getPatternLines(String symbol)    {       //Return lies containing pattern, similar to writePatternLines
        ArrayList<String> lines = new ArrayList<>();
        int lineCount = 0;

        while(true) {
            String pattern = symbol + lineCount;
            lineCount++;
            if (!findLine(pattern).equals("Line not found"))    {
                lines.add(findLine(pattern));
            } else  {
                break;
            }
        }
        return lines;
    }

    public void firstMeeting()  {           //Write first meeting dialog
        writePatternLines("*");
    }

    public void converse()  {
        if(firstMeeting)    {       //If it's players first meeting then give them introduction
            firstMeeting();
            waitFor(1000);
            System.out.println();
            firstMeeting = false;
        }else {
            writePatternLines("$$");
            waitFor(1500);
        }

        while(true) {
            writePatternLines("q0");
            write("Enter 'leave' to exit...");
            ArrayList<String> q0Lines = getPatternLines("q0");      //q0Lines is given lines containing q0 ie. the row of questions.

            int input = scan();
            if(input == -1)  {
                writePatternLines("##");            //Write farewell dialog
                break;
            }
            for (int i = 0; i < q0Lines.size(); i++) {                                        //For loop through all q0 questions
                int questionNum = Character.getNumericValue(q0Lines.get(i).charAt(0));        //Get the number at start of question, converted form char to int

                if (input == questionNum) {                                                 //If input equals question number
                    writePatternLines("a" + (questionNum - 1));
                }
            }
        }
    }

    static int scan() {      //Get user input
        Scanner user = new Scanner(System.in);  // Create a Scanner object
        System.out.print("->");

        int userInput = -1;
        try {
            String input = user.nextLine();              //Get user input
            if(input.equals("leave"))    {
                return -1;
            } else  {
                userInput = Integer.parseInt(input);
            }
        } catch (NumberFormatException e)   {                           //If no int given then try again
            System.out.println(("No question selected, please enter valid question number"));
            scan();
        }
        return userInput;  // Read user input & return
    }

    private void write(String text) {               //Used to write test one char at a time
        for (int x = 0; x < text.length(); x++) {
            System.out.print(text.charAt(x));         //Write one char
            waitFor(20);
        }
        System.out.println();
    }

    private void waitFor(int time)  {       //Program sleeps for x time millis
        try {
            Thread.sleep(time);               //Wait for x time(millis)
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();     //Interrupts thread if there is an exception
        }
    }

    @Override
    public String interact()  {
        System.out.println("You can interact with " + this.name);
        return "npc";
    }

    private void createNpcWindow()   {
        //Create start text
        String text = "";
        for(String line : getPatternLines("*"))    {
            text = text.concat(line + "\n");
        }
        dialogText = new Text(text);
        dialogText.setStyle("-fx-font: 20 arial;");

        npcWindow.getChildren().add(dialogText);

        //Create hashMap
        ArrayList<String> q0Lines = getPatternLines("q0");      //q0Lines is given lines containing q0 ie. the row of questions.
        HashMap<String, Integer> questionMap = new HashMap<>();

        for (int i = 0; i < q0Lines.size(); i++) {                                        //For loop through all q0 questions
            questionMap.put(q0Lines.get(i), i);
        }

        //Create answer menu
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(questionMap.keySet());
        Collections.reverse(list);
        ListView<String> questionList = new ListView<>(list);

        NPC npc = this;
        Callback<ListView<String>, ListCell<String>> commandCellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new NpcButtonCell(npc, questionMap);
            }
        };
        questionList.setCellFactory(commandCellFactory);

        questionList.setPrefHeight(35 * questionMap.size());
        if (questionList.getPrefHeight() > 300) {
            questionList.setPrefHeight(300);
        }
        questionList.setPrefWidth(350);
        questionList.setStyle("-fx-font: 14 arial;");

        npcWindow.getChildren().add(questionList);
    }

    public void updateAnswer(int q) {
        String text = "";
        for(String line : getPatternLines("a" + q))    {
            text = text.concat(line + "\n");
        }
        dialogText.setText(text);
    }

    public void resetNpcWindow()    {
        dialogText.setText(getPatternLines("$$").get(0));
    }

    public VBox getNpcWindow()  {
        npcWindow.setLayoutX(this.getImageView().getX());
        npcWindow.setLayoutY(this.getImageView().getY());

        npcWindow.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        return npcWindow;
    }

}

