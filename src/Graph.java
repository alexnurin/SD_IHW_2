import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс, предназначенный для упорядочивания файлов в зависимости от require-строк путём топологической сортировки.
 *
 */
public class Graph {
    private final String dirPath;
    private final Vector<File> files;
    private final Vector<Vector<Integer>> AdjList;
    private final HashMap<File, Integer> numberInGraph;
    private File problemFile1;
    private File problemFile2;

    /**
     * Инкапсуляция доступа к файлам, конфликтующих в require
     * @return Строку с названиями файлов
     */
    public String problemFiles() {
        return problemFile1.getName() + " and " + problemFile2.getName();
    }

    private boolean cycles = false;

    /**
     * Инкапсуляция доступа к cycles
     * @return cycles
     */
    public boolean Cycles() {
        return cycles;
    }

    /**
     * Поиск "предков" файла - других файлов, указанных в его require. Некорректные require игнорируются.
     * @param file Исследуемый файл.
     * @return Вектор файлов - предки исследуемого.
     */
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

    /**
     * Конструктор.
     * @param txtFiles Вектор файлов.
     * @param path Путь к корневой папке.
     */
    public Graph(Vector<File> txtFiles, String path) {
        files = txtFiles;
        dirPath = path;
        AdjList = new Vector<>();
        numberInGraph = new HashMap<>();
    }

    /**
     * Запрос на вывод файлов в корректном порядке.
     * @return Осортированные топологически файлы.
     */
    public Stack<File> getSortedFiles() {
        buildGraph();
        return topologicalSort();
    }

    /**
     * Построение графа путем перевода файлов в целые числа (вершины) и сохранение списка смежности между ними.
     */
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

    /**
     * Рекурсивная итерация топологической сортировки.
     * @param node Текущая вершина графа.
     * @param visited Массив посещений.
     * @param stack Ссылка на стек обработанных вершин.
     */
    void topologicalSortIteration(int node, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;

        for (Integer integer : AdjList.get(node)) {
            if (!visited[integer]) {
                topologicalSortIteration(integer, visited, stack);
            }
        }
        stack.push(node);
    }

    /**
     * Конвертация + ревёрс целочисленного стека в стек файлов.
     * @param stack Целочисленный стек.
     * @return Стек файлов.
     */
    Stack<File> integersToFiles(Stack<Integer> stack) {
        Stack<File> sortedFiles = new Stack<>();
        while (!stack.empty()) {
            sortedFiles.push(files.get(stack.pop()));
        }
        return sortedFiles;
    }

    /**
     * Топологическая сортировка файлов в зависимости от require-строчек.
     * @return Стек файлов в корректном порядке для конкатенации.
     */
    Stack<File> topologicalSort() {
        Stack<Integer> stack = new Stack<>();

        boolean[] colour = new boolean[AdjList.size()];
        for (int i = 0; i < AdjList.size(); i++) {
            colour[i] = false;
        }

        for (int i = 0; i < AdjList.size(); i++) {
            if (!colour[i]) {
                topologicalSortIteration(i, colour, stack);
            }
        }
        if (check_cycle(stack)) {
            return new Stack<>();
        }
        return integersToFiles(stack);
    }

    /**
     * Проверка на циклы путём поиска противоречий в псевдо-топологически отсортированном списке вершин.
     * @param numbers Стек чисел - упорядоченные вершины графа.
     * @return true если цикл найден.
     */
    boolean check_cycle(Stack<Integer> numbers) {
        for (int startNode = 0; startNode < AdjList.size(); startNode++) {
            for (Integer targetNode : AdjList.get(startNode)) {
                if (numbers.indexOf(targetNode) > numbers.indexOf(startNode)) {
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
