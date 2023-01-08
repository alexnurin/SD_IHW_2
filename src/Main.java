import java.io.File;
import java.util.MissingFormatArgumentException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws MissingFormatArgumentException {
        System.out.println("Start. \nEnter path to the root directory: \n");
        Scanner scanner = new Scanner(System.in);
        String inputPath = scanner.nextLine();
        if (!FileHandler.pathIsCorrect(inputPath)) {
            throw new MissingFormatArgumentException("Wrong input path\n");
        }
        FileHandler fileHandler = new FileHandler(inputPath);
        var res = fileHandler.listFiles();
        for (File f : res) {
            System.out.println(f.getName());
        }
    }
}