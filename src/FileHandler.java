import java.io.File;

public class FileHandler {
    public String rootPath;
    public File filePointer;

    public FileHandler() {
        rootPath = "/";
        filePointer = new File(rootPath);
    }

    public FileHandler(String path) {
        rootPath = path;
        filePointer = new File(rootPath);
    }

    public static boolean pathIsCorrect(String path) {
        return true;
    }

    public File[] listFiles() {
        return filePointer.listFiles();
    }
}
