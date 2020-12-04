package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class GameLogger {
    private ArrayList<Command> inputs;
    private long seed;

    public GameLogger(long seed) {
        this.seed = seed;
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
        transcript.append(seed);
        transcript.append("\n");
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
        return loadGameFrom(file, false);
    }

    public static Game loadGameFrom(File file, boolean isGUI) {
        Game game = null;
        CommandWords commands = new CommandWords();
        commands.addAllCommandWords();
        try {
            Scanner log = new Scanner(file);
            String inputLine;


            long seed = log.nextLong(); //This only reads the first line
            game = new Game(seed, isGUI);
            game.setCreatedFromSaveFile(true);

            //This will skip the first line, containing the seed
            System.out.println(log.nextLine());

            while(log.hasNextLine())   {
                inputLine = log.nextLine();

                Scanner tokenizer = new Scanner(inputLine);
                String word1 = null;
                String word2 = "";
                if(tokenizer.hasNext()) {
                    word1 = tokenizer.next();
                    while(tokenizer.hasNext()) {
                        word2 += tokenizer.next();
                        if (tokenizer.hasNext()) {
                            word2 += " ";
                        }
                    }
                }
                Command command = new Command(commands.getCommandWord(word1), word2.toString());
                System.err.println(command.toString());
                game.processCommand(command);
            }

        } catch (FileNotFoundException e)  {
            System.out.println("File not found");
        }
        return game;
    }
}
