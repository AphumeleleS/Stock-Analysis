import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

class ProgramInfo implements Comparable<ProgramInfo> {
    TreeNode tree;
    double fitness;
    double f1Score;

    public ProgramInfo(TreeNode tree, double f1Score) {
        this.tree = tree;
        this.f1Score = f1Score;
    }
    // sortable now with Collections,sort();
    @Override
    public int compareTo(ProgramInfo other) {
        return Double.compare(other.f1Score, this.f1Score);
    }
}
    
public class Utils {

    static long start ;
    static long end ;
    static long durationInMs;

    // Parameters
    static boolean terminate = false;
    static double desiredFitness = 0.8;
    static double highestFitness;
    static TreeNode bestSolution;
    
    static int populationSize = 40;
    static int generations = 20;
    static int minTreeDepth = 6;
    static int maxTreeDepth = 12;
    static int tournamentSize = 10;

    static double mutationProbability = 0.2;    
    static double crossoverProbability = 0.8;
    static double elitismPercent = 0.05;
    static double mutationPercent = 1 - elitismPercent - crossoverProbability;
    static double crossoverPercent = 0.70;
    
    static double functionProbability = 0.7;

    static double terminalProbability = 0.5; // defaulted to 50%
    static double constantProbability = 0.5; // defaulted to 50%

    static ArrayList<Long> runtimes = new ArrayList<>();

    // Random
    static long seed = System.currentTimeMillis();
    private static final Random random = new Random(seed);

    // counters
    static int TP = 0, TN = 0, FP = 0, FN = 0;

    // data storage
    static ArrayList<HashMap<String, Double>> data = new ArrayList<>(); // rows formatted
    static HashMap<String, Double> candlestickData;

    static ArrayList<TreeNode> population = new ArrayList<>(); 

    static ArrayList<ProgramInfo> resultingPopulation = new ArrayList<>();

    enum TreeGrowthType {
        FULL,   // represented by 0
        GROW    // represented by 1
    }

    public enum NodeType {
        FUNCTION("function"),
        TERMINAL("terminal"),
        CONSTANT("constant");

        private final String label;

        NodeType(String label) {
            this.label = label;
        }

        public String get() {
            return label;
        }
    }

    static String[] terminals = { "Open", "High", "Low", "Close", "Adj Close" };
    static String[] functions = { "+", "-", "*", "/", "max", "min" };
    static double[] constants = {
        -2.0, -1.5, -1.0, -0.75, -0.5, -0.25, 0.0,
        0.25, 0.5, 0.75, 1.0, 1.5, 2.0
    };


    public static void printData() {
        int index = 0;
        for (HashMap<String, Double> row : data) {
            System.out.println("Row " + index + ":");
            for (Map.Entry<String, Double> entry : row.entrySet()) {
                System.out.println(" " + entry.getKey() + " : " + entry.getValue());
            }
            index++;
        }
    }

    // Collect Data
    public static void readCSV(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            String[] headings = null;
            
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.contains("Open")) {
                    headings = line.split(",");
                    continue;
                }
                
                String[] tempData = line.split(",");
                HashMap<String, Double> rowData = new HashMap<>();
                
                for (int i = 0; i < tempData.length && i < headings.length; i++) {
                    rowData.put(headings[i], Double.parseDouble(tempData[i]));
                }
                
                data.add(rowData);
            }
            
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("readCSV(): File not found.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("readCSV(): Unknown error.\n" + e.getMessage());
        }
    }
    
    // Randomly build programs (trees) from function and terminal sets.
    public static void buildPrograms(){
        System.out.println("Building Programs...");
        int depth;
        TreeGrowthType treeType;
        for (int i = 0; i < populationSize; i++) {
            depth = random.nextInt(minTreeDepth,maxTreeDepth);
            treeType = TreeGrowthType.values()[random.nextInt(0, 2)];
            population.add(buildTree(depth, treeType.name()));            
        }
    }

    // Evaluate the fitness of each program against our data.
    public static void evaluateFitness() {
        for (int index = 0; index < generations; index++) {
            System.out.println("\nEvaluating Fitness...");
            start = System.nanoTime();
            if (!terminate) {
                resultingPopulation.clear();
                for (TreeNode tree : population) {
                    evaluateTree(tree);
                }
                Collections.sort(resultingPopulation);
                
                if (!resultingPopulation.isEmpty()) {
                    highestFitness = resultingPopulation.get(0).f1Score;
                    bestSolution = resultingPopulation.get(0).tree;
                    System.out.println("Generation: " + index + " Highest Fitness: " + highestFitness);
                    
                    // termination condition
                    if (highestFitness >= desiredFitness) {
                        terminate = true;
                        System.out.println("Desired fitness reached. Terminating.");
                        break;
                    }
                }

                if (index < generations - 1 && !terminate) {
                    createNextGeneration();
                }
            } else {
                System.out.println("evaluateFitness(): Termination met.");
                break;
            }
            end = System.nanoTime();
            durationInMs = (end - start) / 1_000_000;
            runtimes.add(durationInMs);
        }
    }

    // Return Data
    public static ArrayList<ProgramInfo> getResults() {
        System.out.println("\n===== GENETIC PROGRAMMING RESULTS =====");
        
        // Print best program
        if (!resultingPopulation.isEmpty()) {
            ProgramInfo best = resultingPopulation.get(0);
            System.out.println("\nBEST PROGRAM:");
            System.out.println("F1 Score: " + best.f1Score);
            System.out.println("Tree Structure:");
            best.tree.print();
            
        }
        
        System.out.println("\nRUNTIME STATISTICS:");
        System.out.println("Total Generations: " + generations);
        long totalRuntime = 0;
        System.out.println("Generation Runtimes (ms):");
        for (int i = 0; i < runtimes.size(); i++) {
            long runtime = runtimes.get(i);
            totalRuntime += runtime;
            System.out.println("  Generation " + i + ": " + runtime + " ms");
        }
        System.out.println("Total Runtime: " + totalRuntime + " ms");
        System.out.println("Average Generation Runtime: " + (runtimes.isEmpty() ? 0 : totalRuntime / runtimes.size()) + " ms");
        
        System.out.println("\nPARAMETERS:");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Generations: " + generations);
        System.out.println("Tree Depth Range: " + minTreeDepth + "-" + maxTreeDepth);
        System.out.println("Tournament Size: " + tournamentSize);
        System.out.println("Mutation Probability: " + mutationProbability);
        System.out.println("Crossover Probability: " + crossoverProbability);
        System.out.println("Elitism Percentage: " + elitismPercent);
        System.out.println("Random Seed: " + seed);
        
        System.out.println("\n======================================");
        
        return resultingPopulation;
    }

    public static void evaluateTree(TreeNode tree) {
        // evaluate fitness of tree against data
        TP = 0; TN = 0; FP = 0; FN = 0;
        double threshold = 0.5;

        for ( HashMap<String,Double> row : data ) {
            candlestickData = row;
            double prediction = evaluateNode(tree);

            int predicatedClass = prediction >= threshold ? 1 : 0;
            int actualClass = row.getOrDefault("Output", 0.0).intValue();

            if (predicatedClass == 1 && actualClass == 1) {
                TP++;
            } else if (predicatedClass == 0 && actualClass == 0) {
                TN++;
            } else if (predicatedClass == 1 && actualClass == 0) {
                FP++;
            } else if (predicatedClass == 0 && actualClass == 1) {
                FN++;
            }
        }

        // Calculate precision, recall, and F1 score
        double precision = (TP + FP == 0) ? 0 : (double) TP / (TP + FP);
        double recall = (TP + FN == 0) ? 0 : (double) TP / (TP + FN);
        double f1Score = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);
        System.out.println("F1 Score: " + f1Score);

        ProgramInfo programInfo = new ProgramInfo(tree, f1Score);
        resultingPopulation.add(programInfo);
    }

    public static void evaluateTree(TreeNode tree, boolean verbose) {
        System.out.println("\n===== TESTING BEST SOLUTION ON TEST DATA =====");
        TP = 0; TN = 0; FP = 0; FN = 0;
        double threshold = 0.5;

        for ( HashMap<String,Double> row : data ) {
            candlestickData = row;
            double prediction = evaluateNode(tree);

            int predicatedClass = prediction >= threshold ? 1 : 0;
            int actualClass = row.getOrDefault("Output", 0.0).intValue();

            if (predicatedClass == 1 && actualClass == 1) {
                TP++;
            } else if (predicatedClass == 0 && actualClass == 0) {
                TN++;
            } else if (predicatedClass == 1 && actualClass == 0) {
                FP++;
            } else if (predicatedClass == 0 && actualClass == 1) {
                FN++;
            }
        }

        // Calculate precision, recall, and F1 score
        double precision = (TP + FP == 0) ? 0 : (double) TP / (TP + FP);
        double recall = (TP + FN == 0) ? 0 : (double) TP / (TP + FN);
        double f1Score = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);


        System.out.println("Test Data Results:");
        System.out.println("F1 Score: " + f1Score);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("\nConfusion Matrix:");
        System.out.println("TP: " + Utils.TP + " (Predicted Buy, Actual Buy)");
        System.out.println("FP: " + Utils.FP + " (Predicted Buy, Actual Don't Buy)");
        System.out.println("FN: " + Utils.FN + " (Predicted Don't Buy, Actual Buy)");
        System.out.println("TN: " + Utils.TN + " (Predicted Don't Buy, Actual Don't Buy)");
        System.out.println("Accuracy: " + ((double)(Utils.TP + Utils.TN) / (Utils.TP + Utils.TN + Utils.FP + Utils.FN)));
        System.out.println("===========================================");
    }


    // generate new programs from parents using crossover and mutation.
    public static void createNextGeneration(){
        System.out.println("Creating next generation...");
        ArrayList<TreeNode> newPopulation = new ArrayList<>();
        int elitismCount = (int) Math.round(populationSize * elitismPercent);
        int crossoverCount = (int) Math.round(populationSize * crossoverPercent);
        int mutationCount = (int) Math.round(populationSize * mutationPercent);

        // Elitism
        for (int i = 0; i < elitismCount; i++) {
            newPopulation.add(resultingPopulation.get(i).tree);
        }

        // Crossover
        for (int i = 0; i < crossoverCount; i++) {
            TreeNode parent1 = tournamentSelection();
            TreeNode parent2 = tournamentSelection();

            TreeNode child = crossover(parent1, parent2);
            newPopulation.add(child);
        }

        // Mutation
        for (int i = 0; i < mutationCount; i++) {
            TreeNode individual = tournamentSelection();
            TreeNode mutated = mutate(individual);
            newPopulation.add(mutated);
        }

        population.clear();
        population.addAll(newPopulation);

        System.out.println("Next generation created with " + population.size() + " individuals");
        System.out.println("Elitism: " + elitismCount + " Crossover: " + crossoverCount + " Mutation: " + mutationCount);
    }

    // Select best individuals, already sorted.
    public static TreeNode tournamentSelection() {
        ArrayList<ProgramInfo> tournament = new ArrayList<>();
        for (int i = 0; i < tournamentSize && i < resultingPopulation.size(); i++ ){
            int randomIndex = random.nextInt(resultingPopulation.size());
            tournament.add(resultingPopulation.get(randomIndex));
        }

        Collections.sort(tournament);
        return cloneTree(tournament.get(0).tree);
    }

    private static TreeNode crossover(TreeNode parent1, TreeNode parent2) {
        System.out.println("Crossover...");
        TreeNode child = cloneTree(parent1);
        if (random.nextDouble() < crossoverProbability) {

            ArrayList<TreeNode> nodes1 = getAllNodes(child);
            if (nodes1.isEmpty()) return child;
            TreeNode crossoverPoint1 = nodes1.get(random.nextInt(nodes1.size()));

            ArrayList<TreeNode> nodes2 = getAllNodes(child);
            if (nodes2.isEmpty()) return child;
            TreeNode crossoverPoint2 = nodes2.get(random.nextInt(nodes2.size()));

            TreeNode subtree = cloneTree(crossoverPoint2);

            // Replace the selected node in the first parent with the subtree
            replaceNode(child, crossoverPoint1, subtree);

        }
        return child;
    }

    private static TreeNode mutate(TreeNode individual) {
        System.out.println("Mutating...");
        TreeNode mutated = cloneTree(individual);

        if (random.nextDouble() < mutationProbability) {
            ArrayList<TreeNode> nodes = getAllNodes(mutated);
            if (nodes.isEmpty()) return mutated;

            TreeNode mutationPoint = nodes.get(random.nextInt(nodes.size()));
            if (mutationPoint.getType().equals("function")) {
                mutationPoint.value = getRandomFunction();
            } else if (mutationPoint.getType().equals("terminal")) {
                mutationPoint.value = getRandomTerminal();
            } else if (mutationPoint.getType().equals("constant")) {
                mutationPoint.value = getRandomConstant();
            }
            
        }
        return mutated;
    }

    private static ArrayList<TreeNode> getAllNodes(TreeNode root){
        ArrayList<TreeNode> nodes = new ArrayList<>();
        if (root != null) {
            collectNodes(root,nodes);
        }
        return nodes;
    }
    
    private static void collectNodes(TreeNode node, ArrayList<TreeNode> nodes){
        if (node == null) return;
        nodes.add(node);
        collectNodes(node.left, nodes);
        collectNodes(node.right, nodes);
    }

    private static void replaceNode(TreeNode root, TreeNode target, TreeNode replacement) {
        if (root == null) return;

        if (root.left == target) {
            root.left = replacement;

        } else if (root.right == target) {
            root.right = replacement;

        } else {
            // dives deeper to find the target recursively
            replaceNode(root.left, target, replacement);
            replaceNode(root.right, target, replacement);
        }
    }

    // avoid damaging a node.
    private static TreeNode cloneTree(TreeNode node) {
        if (node == null) return null;

        TreeNode clone = new TreeNode(node.type, node.value);
        clone.left = cloneTree(node.left);
        clone.right = cloneTree(node.right);
        return clone;
    }

    public static double evaluateNode(TreeNode node) {
        if (node == null) return 0.0;

        String type = node.getType();
        String value = node.value;

        if (type.equals("terminal")) {
            return candlestickData.getOrDefault(value,0.0);
        } else if (type.equals("constant")) {
            return Double.parseDouble(value);
        } else {
            double leftValue = evaluateNode(node.left);
            double rightValue = evaluateNode(node.right);

            switch (value) {
                case "+": return leftValue + rightValue;
                case "-": return leftValue - rightValue;
                case "*": return leftValue * rightValue;
                case "/": return rightValue != 0 ? leftValue / rightValue : 0.0;
                case "max": return Math.max(leftValue, rightValue);
                case "min": return Math.min(leftValue, rightValue);
                default: return 0.0;
            }
        }
    }

    public static TreeNode buildTree(int depth, String treeType) {
        TreeNode root = buildRoot();
        
        // For FULL method, all internal nodes are functions until max depth
        // For GROW method, nodes can be functions or terminals at any depth
        
        if (depth <= 1) {
            closeNode(root);
            return root;
        }

        if (treeType.equals("FULL")) {
            root.left = buildNode(NodeType.FUNCTION);
            root.right = buildNode(NodeType.FUNCTION);

            buildTree(root.left, depth - 1, treeType);
            buildTree(root.right, depth - 1, treeType);
        } else {

            // GROW
            // Build left
            if (random.nextDouble(0,1) < functionProbability ) { // 70% chance to add function node
                root.left = buildNode(NodeType.FUNCTION);
                buildTree(root.left, depth - 1, treeType);
            } else {
                // 50|50 chance for terminal or constant
                root.left = (random.nextInt(2) == 0) ? 
                    buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            }

            // Build right
            if (random.nextDouble(0,1) < functionProbability ) { // 70% chance to add function node
                root.right = buildNode(NodeType.FUNCTION);
                buildTree(root.right, depth - 1, treeType);
            } else {
                // 50|50 chance for terminal or constant
                root.right = (random.nextInt(2) == 0) ? 
                    buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            }
        }
        
        return root;
    }

    private static void buildTree(TreeNode node, int depth, String treeType) {
        
        if (depth <= 1 || node == null || !node.getType().equals("function")) {
            // At max depth or if node is not a function, close it
            closeNode(node);
            return;
        }

        if (treeType.equals("FULL")) {
            // For FULL, always use function nodes at internal levels
            node.left = buildNode(NodeType.FUNCTION);
            node.right = buildNode(NodeType.FUNCTION);
            
            // Recursively build subtrees
            buildTree(node.left, depth - 1, treeType);
            buildTree(node.right, depth - 1, treeType);
        } else {
            if (random.nextDouble() < functionProbability ) { // 70% chance for function nodes
                node.left = buildNode(NodeType.FUNCTION);
                buildTree(node.left, depth - 1, treeType);
            } else {
                // 50% chance for terminal, 50% for constant
                node.left = (random.nextInt(2) == 0) ? 
                    buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            }
            
            if (random.nextDouble() < functionProbability ) { // 70% chance for function nodes
                node.right = buildNode(NodeType.FUNCTION);
                buildTree(node.right, depth - 1, treeType);
            } else {
                // 50% chance for terminal, 50% for constant
                node.right = (random.nextInt(2) == 0) ? 
                    buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            }
        }
    }

    // Helper Functions
    public static TreeNode buildRoot(){
        TreeNode root = new TreeNode("function", getRandomFunction());
        return root;
    }

    public static TreeNode buildNode(NodeType type){
        TreeNode root = null;
        switch (type) {
            case FUNCTION: 
                root = new TreeNode("function", getRandomFunction());  
                break;
            case TERMINAL: 
                root = new TreeNode("terminal", getRandomTerminal());
                break;
            case CONSTANT:
                root = new TreeNode("constant", getRandomConstant());
                break; 
        }

        return root;
    }

    public static void closeNode(TreeNode node) {
        // 50% chance of terminal, 50% chance of constant
        if (node == null) return;

        // constants and terminals dont need children NB.
        if (node.getType().equals("function")) {
            // Close left child
            if (node.left == null) {
                node.left = (random.nextInt(2) == 0 ) ? buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            } else {
                closeNode(node.left);
            }

            // Close right child
            if (node.right == null) {
                node.right = (random.nextInt(2) == 0 ) ? buildNode(NodeType.TERMINAL) : buildNode(NodeType.CONSTANT);
            } else {
                closeNode(node.right); 
            }
        }
    }


    public static String getRandomFunction() {
        return functions[random.nextInt(functions.length)];
    }

    public static String getRandomTerminal() {
        return terminals[random.nextInt(terminals.length)];
    }

    public static String getRandomConstant() {
        return String.valueOf(constants[random.nextInt(constants.length)]);
    }

}
