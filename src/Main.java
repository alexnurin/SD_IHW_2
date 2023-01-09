import java.io.File;
import java.io.IOException;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws MissingFormatArgumentException {
        System.out.println("Start. \nEnter path to the root directory: \n");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.nextLine();
        if (!inputPath.endsWith("\\")) {
            inputPath = inputPath + "\\";
        }
        if (!FileHandler.pathIsCorrect(inputPath)) {
            throw new MissingFormatArgumentException("Not found any txt files. Try another path.");
        }
        FileHandler fileHandler = new FileHandler(inputPath);
        var collectedTxt = fileHandler.txtCollector(fileHandler.rootFile);
        for (File f : collectedTxt) {
            System.out.println(f.getName() + " found.");
        }
        Graph graph = new Graph(collectedTxt, inputPath);
        graph.topologicalSortHandler();
        Stack<File> sortedFiles = graph.getSortedFiles();
        if (graph.Cycles()){
            System.out.println("Cycle detected in dependencies. Problem is between files " + graph.problemFiles() + ".\n");
            return;
        }
        try {
            fileHandler.concatenateFiles(sortedFiles);
            System.out.println("Check for results in file .out.txt!");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}