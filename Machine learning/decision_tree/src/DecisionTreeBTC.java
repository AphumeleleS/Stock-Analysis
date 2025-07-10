import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Scanner;

public class DecisionTreeBTC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.print("Enter random seed: ");
            int seed = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Enter path to training ARFF file: ");
            String trainPath = scanner.nextLine();

            System.out.print("Enter path to test ARFF file: ");
            String testPath = scanner.nextLine();

           
            DataSource trainSource = new DataSource(trainPath);
            Instances train = trainSource.getDataSet();

            if (train == null) {
                System.err.println("❌ ERROR: Could not load training data. Check your file path.");
                return;
            }

            train.setClassIndex(train.numAttributes() - 1);

           
            DataSource testSource = new DataSource(testPath);
            Instances test = testSource.getDataSet();

            if (test == null) {
                System.err.println("❌ ERROR: Could not load test data. Check your file path.");
                return;
            }

            test.setClassIndex(test.numAttributes() - 1);

            
            J48 tree = new J48();
            tree.setReducedErrorPruning(true);  
            tree.setSeed(seed);              
            tree.buildClassifier(train);

            // Evaluate
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(tree, test);

            // Output results
            System.out.println("=== J48 Decision Tree Evaluation ===");
            System.out.printf("Accuracy: %.2f%%\n", eval.pctCorrect());
            System.out.printf("F1 Score: %.4f\n", eval.weightedFMeasure());
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
