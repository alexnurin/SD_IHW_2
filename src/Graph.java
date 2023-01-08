import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Graph {
    private String dirPath;
    private Vector<File> files;
    private Vector<Vector<Integer>> AdjMatrix;
    // private Map<File, Integer> numberInGraph;

    private Vector<File> getParents(File file) {
        Vector<File> parents = new Vector<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("require")) {
                    // System.out.println(line.split("‘")[0]);
                    parents.add(new File(dirPath + line.split("‘")[1]));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parents;
    }

    public Graph(Vector<File> txtFiles, String path) {
        files = txtFiles;
        dirPath = path;
        for (File file : files) {
            Vector<File> parents = getParents(file);
            for (File parent : parents) {
                System.out.println(file.getName() + " depends on " + parent.getName());
            }
        }
    }

    public void buildGraph() {
        AdjMatrix.setSize(files.size());
        for (File file : files) {

        }
    }

}
