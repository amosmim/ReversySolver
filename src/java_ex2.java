import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;

public class java_ex2 {

    public static void main(String[] args) {
        String content = "";
        try {
            content = new Scanner(new File("input.txt")).useDelimiter("\\Z").next();
        } catch (IOException e) {
            System.out.println("can't read from input.txt");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        // split the file to lines
        String[] splited = content.split("\\r?\\n");
        ArrayList<String> map = new ArrayList<>(Arrays.asList(splited));
        GridState start = new GridState(map);

        // print start state
        System.out.println("start state:");
        System.out.println(start);
        System.out.print("Heuristic = " + start.getHeuristicNumber());
        System.out.println("\n------------------------\n");

        // solve the game in loop
        MinMax minMax = new MinMax(3);
        GridState result = start;
        do {
            result = minMax.IdealStep(result);
            // print the new state
            System.out.println("minmax returns:");
            System.out.print(result);
            System.out.print("Heuristic = " + result.getHeuristicNumber());
            System.out.println("\n------------------------\n");
        }while(!result.isDone());
        // check who wins
        char wins = result.whoWin();
        // print result
        System.out.println(wins + " player wins!");
        // write result to output file
        try {
            PrintWriter out = new PrintWriter("output.txt", "UTF-8");
            out.write(wins);
            out.close();
        } catch (Exception exp) {
            System.out.println("ERROR: failed to write solution to output file");
            System.out.println(exp.getMessage());
        }
    }
}
