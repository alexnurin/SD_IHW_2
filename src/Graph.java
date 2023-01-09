import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private final String dirPath;
    private final Vector<File> files;
    private final Vector<Vector<Integer>> AdjList;
    private final HashMap<File, Integer> numberInGraph;
    private File problemFile1;
    private File problemFile2;

    public String problemFiles(){
        return problemFile1.getName() + " and " + problemFile2.getName();
    }
    private boolean cycles = false;
    public boolean Cycles (){
        return cycles;
    }

    private Vector<File> getParents(File file) {
        Vector<File> parents = new Vector<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("require")) {
                    File parent = new File(dirPath + line.split("‘")[1].split("’")[0]);
                    if (numberInGraph.containsKey(parent)) {
                        parents.add(parent);
                    }
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
    }

    public Stack<File> getSortedFiles() {
        buildGraph();
        return topologicalSortHandler();
    }

    public void buildGraph() {
        AdjList.setSize(files.size());
        for (int i = 0; i < files.size(); i++) {
            numberInGraph.put(files.elementAt(i), i);
            AdjList.set(i, new Vector<>());
        }

        for (File file : files) {
            Vector<File> parents = getParents(file);
            for (File parent : parents) {
                AdjList.get(numberInGraph.get(file)).add(numberInGraph.get(parent));
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

    Stack<File> integersToFiles(Stack<Integer> stack) {
        Stack<File> sortedFiles = new Stack<>();
        while (!stack.empty()) {
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
        if (check_cycle(stack)){
            return new Stack<>();
        }
        return integersToFiles(stack);
    }

    boolean check_cycle(Stack<Integer> numbers) {
        for (int startNode = 0; startNode < AdjList.size(); startNode++) {
            for (Integer targetNode : AdjList.get(startNode)){
                if (numbers.indexOf(targetNode) > numbers.indexOf(startNode)){
                    cycles = true;
                    problemFile1 = files.get(startNode);
                    problemFile2 = files.get(targetNode);
                    return true;
                }
            }
        }
        return false;
    }
}
