import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private final String dirPath;
    private final Vector<File> files;
    private final Vector<Vector<Integer>> AdjList;
    private final HashMap<File, Integer> numberInGraph;

    private Vector<File> getParents(File file) {
        Vector<File> parents = new Vector<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("require")) {
                    parents.add(new File(dirPath + line.split("‘")[1].split("’")[0]));
                    System.out.println("please " + parents.get(parents.size() - 1));
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
        AdjList = new Vector<>();
        numberInGraph = new HashMap<>();
        for (File file : files) {
            Vector<File> parents = getParents(file);
            for (File parent : parents) {
                System.out.println(file.getName() + " depends on " + parent.getName());
            }
        }
    }

    public Stack<File> getSortedFiles(){
        buildGraph();
        return topologicalSortHandler();
    }

    public void buildGraph() {
        AdjList.setSize(files.size());
        for (int i = 0; i < files.size(); i++) {
            numberInGraph.put(files.elementAt(i), i);
            AdjList.set(i, new Vector<>());
        }
        System.out.println(numberInGraph.toString());

        for (File file : files) {
            Vector<File> parents = getParents(file);
            for (File parent : parents) {
                // System.out.print(parent + " $ " + numberInGraph.get(parent) + "\n");
                AdjList.get(numberInGraph.get(file)).add(numberInGraph.get(parent));
            }
        }
        for (int i = 0; i < AdjList.size(); i++) {
            System.out.println(i + ":");
            for (var el : AdjList.get(i)) {
                System.out.print(el + " ");
            }
        }
    }

    void topologicalSortIteration(int node, boolean[] visited, Stack<Integer> stack) {
        //  Помечаем текущий узел как посещенный
        visited[node] = true;

        // Рекурсивно вызываем функцию для всех смежных вершин
        for (Integer integer : AdjList.get(node)) {
            if (!visited[integer]) {
                topologicalSortIteration(integer, visited, stack);
            }
        }
        // Добавляем текущую вершину в стек с результатом
        stack.push(node);
    }

    Stack<File> integersToFiles(Stack<Integer> stack){
        Stack <File> sortedFiles = new Stack<>();
        while (!stack.empty()){
            sortedFiles.push(files.get(stack.pop()));
        }
        return sortedFiles;
    }

    Stack<File> topologicalSortHandler() {
        Stack<Integer> stack = new Stack<>();

        // Помечаем все вершины как непосещенные
        boolean[] colour = new boolean[AdjList.size()];
        for (int i = 0; i < AdjList.size(); i++) {
            colour[i] = false;
        }
        // Вызываем рекурсивную вспомогательную функцию
        // для поиска топологической сортировки для каждой вершины
        for (int i = 0; i < AdjList.size(); i++) {
            if (!colour[i]) {
                topologicalSortIteration(i, colour, stack);
            }
        }
        return integersToFiles(stack);
    }
}
