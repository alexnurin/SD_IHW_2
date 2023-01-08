import java.io.File;
import java.util.Vector;

public class FileHandler {
    public String rootPath;
    public File rootFile;

    public FileHandler() {
        rootPath = "/";
        rootFile = new File(rootPath);
    }

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

    public File[] listFiles() {
        return rootFile.listFiles();
    }
}
