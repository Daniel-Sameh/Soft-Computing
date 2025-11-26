package fcai.sclibrary.usecases.MADRSdepressionTest;

import fcai.sclibrary.fuzzyLogic.FuzzyLogicEvaluator;
import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepressionTest {
    public static void main(String[] args) {
        System.out.println("Initializing MADRS Depression Test...");

        // 1. Define Variables
        List<FuzzyVariable> variables = new ArrayList<>();

        // Inputs
        variables.add(createInput("Sadness"));
        variables.add(createInput("InabilityToFeel"));
        variables.add(createInput("InnerTension"));
        variables.add(createInput("Concentration"));
        variables.add(createInput("Pessimism"));
        variables.add(createInput("SuicidalThoughts"));

        // Outputs
        variables.add(createOutput("AffectiveScore", 10));
        variables.add(createOutput("CognitiveScore", 10));
        variables.add(createOutput("DepressionSeverity", 60));

        // Create Evaluator
        FuzzyLogicEvaluator evaluator = FuzzyLogicEvaluator.builder().build();
        evaluator.setVariables(variables);
//        printVariables(variables);
        System.out.println("Variables Configured.");

        // 3. Load Rules
        try {
            evaluator.createAllRules("src/main/java/fcai/sclibrary/usecases/MADRSdepressionTest/rules.txt");
            System.out.println("System Ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean ok = true;
        while (ok) {
            System.out.println("======================================");
            System.out.println(" Answer the following questions (0-6):");
            System.out.println("======================================");
            try {
                Map<FuzzyVariable, Double> inputs= new HashMap<>();

                for (int i = 0; i < 6; i++) {
                    FuzzyVariable fuzzyVariable = variables.get(i);
                    System.out.print((i + 1) + ". " + fuzzyVariable.getName() + ": ");
                    // input number from 0 to 6
                    String input = System.console().readLine();
                    double value = Double.parseDouble(input);
                    if (value < 0 || value > 6) {
                        throw new Exception("Invalid input. Please enter a number between 0 and 6.");
                    }
                    inputs.put(fuzzyVariable, value);
                }
                double result = evaluator.evaluate(inputs);
                System.out.println("======================================");
                System.out.println(" Depression Severity Score: " + result);
                System.out.println("======================================");

                boolean next = true;
                while (next) {


                    System.out.println("Actions: ");

                    System.out.println(" 1-To Create a new rule");
                    System.out.println(" 2-To Edit an existing rule");
                    System.out.println(" 3-To Disable a rule");
                    System.out.println(" 4-To Enable a rule");
                    System.out.println(" 5-Retake the test");
                    System.out.println(" [Other]-to exit");
                    System.out.print(" Choose an option (1-5): ");
                    String option = System.console().readLine();
                    switch (option) {
                        case "1" -> {
                            String ruleString=InputRule();
                            evaluator.create(ruleString);
                        }
                        case "2" -> {
                            System.out.println("  Enter the rule ID to edit: ");
                            String ruleId = System.console().readLine();
                            String ruleString=InputRule();
                            evaluator.edit(Integer.parseInt(ruleId), ruleString);
                        }
                        case "3" -> {
                            System.out.println("  Enter the rule ID to disable: ");
                            String ruleId = System.console().readLine();
                            evaluator.disable(Integer.parseInt(ruleId));
                        }
                        case "4" -> {
                            System.out.println("  Enter the rule ID to enable: ");
                            String ruleId = System.console().readLine();
                            evaluator.enable(Integer.parseInt(ruleId));
                        }
                        case "5" -> {
                            next= false;
                        }
                        default -> {
                            ok = false;
                            next = false;
                        }
                    }

                }

            }catch (Exception e){
                ok = false;
                e.printStackTrace();
            }
        }
    }

    private static String InputRule() {
        System.out.println("  Enter the new rule in the format: ");
        String s1 = System.console().readLine();
        String ruleString = s1;
        while (s1.length() >= 4 && !s1.substring(0, 4).equalsIgnoreCase("THEN")) {
            s1 = System.console().readLine();
            ruleString += "\n" + s1;
        }
        return ruleString;
    }

    private static FuzzyVariable createInput(String name) {
        List<FuzzySet> sets = new ArrayList<>();

        // Normal
        sets.add(createSet("Normal", "Trapezoid", List.of(0.0, 0.0, 1.0, 3.0)));

        // Mild
        sets.add(createSet("Mild", "Triangular", List.of(1.0, 3.0, 5.0)));

        // Severe
        sets.add(createSet("Severe", "Trapezoid", List.of(3.0, 5.0, 6.0, 6.0)));

        return new FuzzyVariable(sets, name);
    }

    private static FuzzyVariable createOutput(String name, int max) {
        List<FuzzySet> sets = new ArrayList<>();

        // Standard MADRS Cutoffs approximated
        sets.add(createSet("Normal", "Trapezoid", List.of(0.0, 0.0, 5.0, 10.0)));// Approx 0-6
        sets.add(createSet("Mild", "Triangular", List.of(7.0, 13.0, 20.0)));   // Approx 7-19
        sets.add(createSet("Moderate", "Triangular", List.of(15.0, 27.0, 40.0)));// Approx 20-34
        sets.add(createSet("Severe", "Trapezoid", List.of(35.0, 60.0, 60.0, 60.0)));// Approx 35-60

        return new FuzzyVariable(sets, name);
    }

    private static FuzzySet createSet(String name, String type, List<Double> indices) {
        Double centroidValue = 0.0;

        if (indices.size() == 4) { // Trapezoidal [a, b, c, d]
            // Centroid approximation: middle of the top flat line
            centroidValue = (indices.get(1).doubleValue() + indices.get(2).doubleValue()) / 2.0;
        } else if (indices.size() == 3) { // Triangular [a, b, c]
            // Centroid approximation: the peak point
            centroidValue = indices.get(1).doubleValue();
        }

        return new FuzzySet(type, name, indices, centroidValue);
    }

//    private static void printVariables(List<FuzzyVariable> variables) {
//        for (FuzzyVariable var : variables) {
//            System.out.println("\n========================================");
//            System.out.println("Variable: " + var.getName());
//            System.out.println("========================================");
//            visualizeVariable(var);
//        }
//    }
//
//    private static void visualizeVariable(FuzzyVariable variable) {
//        List<FuzzySet> sets = variable.getFuzzySets();
//        if (sets == null || sets.isEmpty()) {
//            System.out.println("  No fuzzy sets defined");
//            return;
//        }
//
//        // Find global min and max across all sets
//        int globalMin = Integer.MAX_VALUE;
//        int globalMax = Integer.MIN_VALUE;
//
//        for (FuzzySet set : sets) {
//            List<Integer> indices = set.getIndices();
//            if (indices != null && !indices.isEmpty()) {
//                globalMin = Math.min(globalMin, indices.get(0));
//                globalMax = Math.max(globalMax, indices.get(indices.size() - 1));
//            }
//        }
//
//        // Print set information
//        System.out.println("\nFuzzy Sets:");
//        char[] symbols = {'*', '#', '+', '@', 'o', 'x', '=', '~'};
//        for (int i = 0; i < sets.size(); i++) {
//            FuzzySet set = sets.get(i);
//            List<Integer> indices = set.getIndices();
//            String shapeInfo;
//            if (indices.size() == 4) {
//                shapeInfo = String.format("[%d, %d, %d, %d]",
//                    indices.get(0), indices.get(1), indices.get(2), indices.get(3));
//            } else if (indices.size() == 3) {
//                shapeInfo = String.format("[%d, %d, %d]",
//                    indices.get(0), indices.get(1), indices.get(2));
//            } else {
//                shapeInfo = "Unknown";
//            }
//            System.out.printf("  %c - %s (%s): %s, Centroid: %.1f%n",
//                symbols[i % symbols.length], set.getName(), set.getType(), shapeInfo, set.getValue());
//        }
//
//        // ASCII visualization
//        int width = 60;
//        int height = 12;
//        char[][] canvas = new char[height][width];
//
//        // Initialize canvas
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                canvas[i][j] = ' ';
//            }
//        }
//
//        // Draw all sets
//        for (int setIdx = 0; setIdx < sets.size(); setIdx++) {
//            FuzzySet set = sets.get(setIdx);
//            char symbol = symbols[setIdx % symbols.length];
//
//            for (int i = 0; i < width; i++) {
//                double x = globalMin + (globalMax - globalMin) * i / (double)(width - 1);
//                double membership = getMembership(x, set.getIndices());
//
//                if (membership > 0.05) { // Only draw if membership is significant
//                    int y = (int)((1.0 - membership) * (height - 1));
//                    if (y >= 0 && y < height) {
//                        // Overlay symbols - if there's already a symbol, keep both visible
//                        if (canvas[y][i] == ' ') {
//                            canvas[y][i] = symbol;
//                        } else if (canvas[y][i] != symbol) {
//                            canvas[y][i] = '█'; // Show overlap with a solid block
//                        }
//                    }
//                }
//            }
//        }
//
//        // Print canvas with axes
//        System.out.println("\nCombined Visualization (membership 0-1):");
//        System.out.println("  1.0 |" + new String(canvas[0]));
//        for (int i = 1; i < height - 1; i++) {
//            System.out.println("      |" + new String(canvas[i]));
//        }
//        System.out.println("  0.0 |" + new String(canvas[height - 1]));
//        System.out.println("      +" + "-".repeat(width));
//
//        // Print scale
//        int spacing = width - String.valueOf(globalMin).length() - String.valueOf(globalMax).length();
//        System.out.printf("      %d%s%d%n", globalMin, " ".repeat(Math.max(0, spacing)), globalMax);
//    }
//
//    private static double getMembership(double x, List<Integer> indices) {
//        if (indices.size() == 4) {
//            // Trapezoidal: [a, b, c, d]
//            int a = indices.get(0);
//            int b = indices.get(1);
//            int c = indices.get(2);
//            int d = indices.get(3);
//
//            if (x < a || x > d) return 0.0;
//            if (x >= b && x <= c) return 1.0;
//            if (x < b) return (x - a) / (double)(b - a);
//            return (d - x) / (double)(d - c);
//
//        } else if (indices.size() == 3) {
//            // Triangular: [a, b, c]
//            int a = indices.get(0);
//            int b = indices.get(1);
//            int c = indices.get(2);
//
//            if (x < a || x > c) return 0.0;
//            if (x == b) return 1.0;
//            if (x < b) return (x - a) / (double)(b - a);
//            return (c - x) / (double)(c - b);
//        }
//
//        return 0.0;
//    }
}