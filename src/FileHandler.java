import java.io.*;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;

/**
 * Класс, предназначенный для обработки файлов: поиска, конкатенации и вывода.
 */
public class FileHandler {
    public String rootPath;
    public File rootFile;

    /**
     * Constructor
     * @param path путь к корневой рабочей папке
     */
    public FileHandler(String path) {
        rootPath = path;
        rootFile = new File(rootPath);
    }

    /**
     *
     * @param path анализируемый путь
     * @return true если путь существует и содержит хотя бы один файл
     */
    public static boolean pathIsCorrect(String path) {
        try {
            File test = new File(path);
            if (test.listFiles() == null || Objects.requireNonNull(test.listFiles()).length == 0){
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Находит все файлы в папке и её подпапках.
     * @param filePath путь до исследуемой папки (вызывается рекурсивно от подпапок)
     * @return вектор найденных файлов
     */
    public Vector<File> txtCollector(File filePath) {
        File[] files = filePath.listFiles();
        Vector<File> txtFiles = new Vector<>();
        if (files == null) {
            return txtFiles;
        }
        for (File file : files) {
            if (file.isFile()) {
                txtFiles.add(file);
            }
            if (file.isDirectory()) {
                txtFiles.addAll(txtCollector(file));
            }
            if (txtFiles.size() > 500){
                throw new OutOfMemoryError("Directory is too large.");
            }
        }
        return txtFiles;
    }

    /**
     *
     * @param files Стек файлов, упорядоченных для вывода
     * @throws IOException Ошибка при чтении из файлов или записи в файлы.
     */
    public void concatenateFiles(Stack<File> files) throws IOException {
        PrintWriter out = null;
        try {
            PrintWriter clearer = new PrintWriter(".out.txt"); // очистить файл перед выводом
            clearer.close();
            FileWriter outStream = new FileWriter(".out.txt", true);
            out = new PrintWriter(outStream);
            while (!files.empty()) {
                File file = files.pop();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {    // выводить содержимое файолов в заданном порядке
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                reader.close();

                String content = stringBuilder.toString();
                out.write(content);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
