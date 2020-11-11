package interactibles;

import game.CommandWords;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class NPC extends Interactable {
    private String name;                 //iInteractibles.NPC name
    private String description;         //iInteractibles.NPC description
    private File dialog;                //File containing iInteractibles.NPC dialog
    private ArrayList<String> lines = new ArrayList<>();        //Array list containing all dialog lines
    private boolean firstMeeting = true;        //Attribute is true if player has not met npc
    private CommandWords commandWords;  //iInteractibles.NPC commands,, not used

    public NPC(File dialog, CommandWords commandWords)   {
        super(commandWords);
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
}

