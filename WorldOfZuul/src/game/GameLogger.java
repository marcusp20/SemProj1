package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class GameLogger {
    private ArrayList<Command> inputs;

    public GameLogger() {
        inputs = new ArrayList<>();
    }

    public void log(Command command) {
        inputs.add(command);
    }

    public boolean save() {
        try {
            PrintWriter myWriter = new PrintWriter("saveFile.txt");
            myWriter.write(this.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder transcript = new StringBuilder();
        Iterator<Command> transcriptIterator = this.inputs.iterator();
        while (transcriptIterator.hasNext()) {
            Command line = transcriptIterator.next();
            transcript.append(line.toString());
            if(transcriptIterator.hasNext()) {
                transcript.append("\n");
            }
        }
        return transcript.toString();
    }

    public static Game loadGameFrom(File file) {
        Game game = new Game();
        CommandWords commands = new CommandWords();
        commands.addAllCommandWords();
        try {
            Scanner log = new Scanner(file);
            String inputLine;
            String word1 = null;
            String word2 = null;

            while(log.hasNextLine())   {
                inputLine = log.nextLine();

                Scanner tokenizer = new Scanner(inputLine);

                if(tokenizer.hasNext()) {
                    word1 = tokenizer.next();
                    if(tokenizer.hasNext()) {
                        word2 = tokenizer.next();
                    }
                }
                game.processCommand(new Command(commands.getCommandWord(word1), word2));
            }

        } catch (FileNotFoundException e)  {
            System.out.println("File not found");
        }
        game.setCreatedFromSaveFile(true);
        return game;
    }
}
