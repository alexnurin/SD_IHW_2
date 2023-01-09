import java.io.File;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws MissingFormatArgumentException {
        System.out.println("Start. \nEnter path to the root directory: \n");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.nextLine();
        if (!FileHandler.pathIsCorrect(inputPath)) {
            throw new MissingFormatArgumentException("Wrong input path\n");
        }
        FileHandler fileHandler = new FileHandler(inputPath);
        var collectedTxt = fileHandler.txtCollector(fileHandler.rootFile);
        for (File f : collectedTxt) {
            System.out.println(f.getName());
        }
        Graph graph = new Graph(collectedTxt, inputPath);
        graph.topologicalSortHandler();
        Stack<Integer> sortedFiles = graph.getSortedFiles();
    }
}