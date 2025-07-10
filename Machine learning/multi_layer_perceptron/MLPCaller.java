import java.io.*;
import java.util.Scanner;

public class MLPCaller {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter random seed:");
        String seed = scanner.nextLine();

        System.out.println("Enter full path to btc_train.csv:");
        String trainPath = scanner.nextLine();

        System.out.println("Enter full path to btc_tests.csv:");
        String testPath = scanner.nextLine();

        try {
            ProcessBuilder pb = new ProcessBuilder("python", "mlp_classifier.py", seed, trainPath, testPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

            BufferedReader csvReader = new BufferedReader(new FileReader("mlp_results.csv"));
            System.out.println("\n--- Results from mlp_results.csv ---");
            while ((line = csvReader.readLine()) != null) {
                System.out.println(line);
            }
            csvReader.close();

        } catch (Exception e) {
            System.out.println("Error running Python script: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();
    }
}
