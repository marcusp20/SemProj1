import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NPC {
    private String name;                 //NPC name
    private String description;         //NPC description
    private File dialog;                //File containing NPC dialog
    private ArrayList<String> lines = new ArrayList<>();        //Array list containing all dialog lines
    private boolean firstMeeting = true;        //Attribute is true if player has not met npc


    NPC(File dialog)   {
        this.dialog = dialog;

        try {
            Scanner dialogReader= new Scanner(dialog);

            while(dialogReader.hasNextLine())   {
                String line = dialogReader.nextLine();
                lines.add(line);
            }

        } catch (FileNotFoundException e)  {
            System.out.println("File not found");
        }

        this.name = findLine(".nn");
        this.description = findLine(".nn");
    }

    private String findLine(String pattern)   {
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

    private void writePatternLines(String symbol)    {
        int lineCount = 0;

        while(true) {
            String pattern = symbol + lineCount;
            lineCount++;
            if (!findLine(pattern).equals("Line not found"))    {
                write(findLine(pattern));
            } else  {
                break;
            }
        }
    }

    private ArrayList<String> getPatternLines(String symbol)    {
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

    public void firstMeeting()  {
        writePatternLines("*");
    }

    public void converse()  {
        if(firstMeeting)    {
            firstMeeting();
            System.out.println("Enter number to continue.");
            scan();
            firstMeeting = false;
        }
        while(true) {
            writePatternLines("q0");
            write("Enter 0 to exit...");
            ArrayList<String> lines = getPatternLines("q0");

            int input = scan();
            if(input == 0)  {
                break;
            }
            for (int i = 0; i < lines.size(); i++) {

                int q = Character.getNumericValue(lines.get(i).charAt(0));
                if (input == q) {
                    writePatternLines("a" + (q - 1));
                }
            }

        }
    }

    static int scan() {      //Get user input
        Scanner user = new Scanner(System.in);  // Create a Scanner object
        System.out.print("->");

        int userInput = -1;
        try {
            userInput = Integer.parseInt(user.nextLine());
        } catch (NumberFormatException e)   {
            System.out.println(("No question selected, please enter valid question number "));
            scan();
        }
        return userInput;  // Read user input & return
    }

    private void write(String text) {
        for (int x = 0; x < text.length(); x++) {
            System.out.print(text.charAt(x));
            textW(20);
        }
        System.out.println();
    }

    private void textW(int time) {
        try
        {
            Thread.sleep(time);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}

