import java.io.*;
import java.util.Stack;
import java.util.Vector;

public class FileHandler {
    public String rootPath;
    public File rootFile;

    public FileHandler(String path) {
        rootPath = path;
        rootFile = new File(rootPath);
    }

    public static boolean pathIsCorrect(String path) {
        return true;
    }

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
        }
        return txtFiles;
    }

    public void concatenateFiles(Stack<File> files) throws IOException {
        PrintWriter out = null;
        try {
            FileWriter outStream = new FileWriter("out.txt", true);
            out = new PrintWriter(outStream);
            while (!files.empty()) {
                File file = files.pop();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                // delete the last new line separator
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
