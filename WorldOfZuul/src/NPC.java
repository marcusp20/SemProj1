import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NPC {
    private String name;             //NPC name
    private String description;      //NPC description
    private File dialog;             //File containing NPC dialog
    private ArrayList<String> lines = new ArrayList<>();

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

        this.name = findLine("nn");
        this.description = findLine("dd");
    }

    private String findLine(String pattern)   {
        for (int i = 0; i < lines.size(); i++) {
            if(lines.get(i).contains(pattern))  {
                String line = lines.get(i).substring(1);
                line = line.substring(1);
                line = line.substring(1);
                line = line.substring(1);
                return line;
            }
        }
        return "Line not found";
    }

    public void firstMeeting()  {
        int lineCount = 0;

        while(true) {
            String pattern = "*" + lineCount;
            lineCount++;
            if (!findLine(pattern).equals("Line not found"))    {
                System.out.println(findLine(pattern));
            } else  {
                break;
            }
        }
    }

    public void test()  {
        System.out.println(this.name);
        System.out.println(this.description);
        System.out.println();

        for(String line : lines)    {
            System.out.println(line);
        }
    }
}

