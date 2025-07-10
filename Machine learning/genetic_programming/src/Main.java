
public class Main {
    static long start ;
    static long end ;
    static long durationInMs;
    public static void main(String[] args) throws Exception {
        start = System.nanoTime();

        trainData();
        Utils.buildPrograms();
        Utils.evaluateFitness();
        Utils.getResults();

        testData();
        end = System.nanoTime();

        durationInMs = (end - start) / 1_000_000;

        System.out.println("Program Execution time: " + durationInMs + " ms");

    }

    public static void trainData() {
        Utils.readCSV("Euro_USD_Stock/BTC_train.csv");
    }

    public static void testData() {
        Utils.readCSV("Euro_USD_Stock/BTC_test.csv");
        Utils.evaluateTree(Utils.bestSolution,true);
        System.out.println("Function: " +Utils.bestSolution.toFunctionString());
    }

}
