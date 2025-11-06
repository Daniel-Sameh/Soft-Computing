package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.core.GAConfig;
import fcai.sclibrary.ga.core.GAProgressListener;
import fcai.sclibrary.ga.core.GeneticAlgorithmEngine;
import fcai.sclibrary.ga.core.Optimization;
import fcai.sclibrary.ga.operators.crossover.NPointCrossover;
import fcai.sclibrary.ga.operators.replacement.ElitistReplacement;
import fcai.sclibrary.ga.operators.replacement.functions.CutoffFunction;
import fcai.sclibrary.ga.operators.selection.TournamentSelection;
import fcai.sclibrary.usecases.artisticImageApproximation.factory.ShapeFactory;
import fcai.sclibrary.usecases.artisticImageApproximation.mutation.ShapeMutation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArtisticApproximationUI extends JFrame {
    private JButton loadImageButton;
    private JButton startButton;
    private JButton stopButton;
    private JButton saveButton;
    private JComboBox<String> shapeTypeComboBox;
    private JLabel targetImageLabel;
    private JLabel generatedImageLabel;
    private JLabel generationLabel;
    private JLabel fitnessLabel;
    private JProgressBar progressBar;

    private BufferedImage targetImage;
    private BufferedImage lastGeneratedImage;
    private GeneticAlgorithmEngine gaEngine;
    private GeneticAlgorithmWorker currentWorker;
    private volatile boolean isRunning = false;

    private static final int MAX_GENERATIONS = 5000;
    private static final int POPULATION_SIZE = 60;
    private static final double MUTATION_RATE = 0.02;
    private static final double CROSSOVER_RATE = 0.8;

    public ArtisticApproximationUI() {
        super("Artistic Image Approximation GA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        initUI();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not set system look and feel.");
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createControlPanel(), BorderLayout.NORTH);
        mainPanel.add(createImagePanel(), BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        loadImageButton = new JButton("Load Target Image...");

        // Shape type selection
        JLabel shapeLabel = new JLabel("Shape Type:");
        shapeTypeComboBox = new JComboBox<>(new String[]{
            "Circles", "Squares", "Rectangles", "Triangles", "Mixed Shapes"
        });
        shapeTypeComboBox.setSelectedIndex(0);

        startButton = new JButton("Start Genetic Algorithm");
        stopButton = new JButton("Stop");
        saveButton = new JButton("Save Image");

        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);

        panel.add(loadImageButton);
        panel.add(shapeLabel);
        panel.add(shapeTypeComboBox);
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(saveButton);

        loadImageButton.addActionListener(e -> loadImageAction());
        startButton.addActionListener(e -> startGaAction());
        stopButton.addActionListener(e -> stopGaAction());
        saveButton.addActionListener(e -> saveImageAction());

        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.setBorder(new TitledBorder("Target Image"));
        targetImageLabel = new JLabel("Please load an image.", SwingConstants.CENTER);
        targetPanel.add(new JScrollPane(targetImageLabel), BorderLayout.CENTER);

        JPanel generatedPanel = new JPanel(new BorderLayout());
        generatedPanel.setBorder(new TitledBorder("Generated Approximation"));
        generatedImageLabel = new JLabel("Waiting to start...", SwingConstants.CENTER);
        generatedPanel.add(new JScrollPane(generatedImageLabel), BorderLayout.CENTER);

        panel.add(targetPanel);
        panel.add(generatedPanel);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel metricsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        generationLabel = new JLabel("Generation: 0");
        fitnessLabel = new JLabel("Best MSE: N/A");
        metricsPanel.add(generationLabel);
        metricsPanel.add(fitnessLabel);

        progressBar = new JProgressBar(0, MAX_GENERATIONS);
        progressBar.setStringPainted(true);

        panel.add(metricsPanel);
        panel.add(progressBar);

        return panel;
    }

    public void loadImageAction() {
        // Stop any running GA first
        if (isRunning && gaEngine != null) {
            gaEngine.stop();
            isRunning = false;
            if (currentWorker != null && !currentWorker.isDone()) {
                currentWorker.cancel(true);
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image File");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                BufferedImage originalImage = ImageIO.read(selectedFile);

                // Resize to 200*200 for faster processing
                BufferedImage resized = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = resized.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(originalImage, 0, 0, 200, 200, null);
                g2d.dispose();

                targetImage = resized; // Use resized image instead

                // Display scaled version (500px wide for UI)
                Image scaledImage = targetImage.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
                targetImageLabel.setIcon(new ImageIcon(scaledImage));
                targetImageLabel.setText(null);

                // Reset UI state
                resetUIState();
                startButton.setEnabled(true);
                shapeTypeComboBox.setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error loading image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetUIState() {
        // Reset GA engine and worker
        gaEngine = null;
        currentWorker = null;
        lastGeneratedImage = null;

        // Reset UI components
        generatedImageLabel.setIcon(null);
        generatedImageLabel.setText("Waiting to start...");
        generationLabel.setText("Generation: 0");
        fitnessLabel.setText("Best MSE: N/A");
        progressBar.setValue(0);

        // Reset button states
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);
    }


    private void startGaAction() {
        if (targetImage == null || isRunning) return;

        startButton.setEnabled(false);
        loadImageButton.setEnabled(false);
        shapeTypeComboBox.setEnabled(false);
        stopButton.setEnabled(true);
        saveButton.setEnabled(false);
        isRunning = true;

        currentWorker = new GeneticAlgorithmWorker();
        currentWorker.execute();
    }

    private void stopGaAction() {
        if (gaEngine != null) {
            gaEngine.stop();
        }
        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
        }
        isRunning = false;
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        loadImageButton.setEnabled(true);
        shapeTypeComboBox.setEnabled(true);

        // Enable save button if we have a generated image
        if (lastGeneratedImage != null) {
            saveButton.setEnabled(true);
        }
    }

    private void saveImageAction() {
        if (lastGeneratedImage == null) {
            JOptionPane.showMessageDialog(this,
                    "No image to save. Please run the GA first.",
                    "No Image", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Generated Image");
        chooser.setSelectedFile(new File("approximation.png"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));
        chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("BMP Image", "bmp"));

        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String name = file.getName();
            String ext = "png";
            int idx = name.lastIndexOf('.');
            if (idx > 0 && idx < name.length() - 1) {
                ext = name.substring(idx + 1).toLowerCase();
            } else {
                String desc = chooser.getFileFilter().getDescription();
                if (desc.contains("JPEG")) ext = "jpg";
                else if (desc.contains("BMP")) ext = "bmp";
                file = new File(file.getAbsolutePath() + "." + ext);
            }
            try {
                ImageIO.write(lastGeneratedImage, ext.equals("jpg") ? "jpg" : ext, file);
                JOptionPane.showMessageDialog(this,
                        "Saved image to: " + file.getAbsolutePath(),
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class GenerationResult {
        final int generation;
        final double fitness;
        final BufferedImage image;

        GenerationResult(int generation, double fitness, BufferedImage image) {
            this.generation = generation;
            this.fitness = fitness;
            this.image = image;
        }
    }

    private class GeneticAlgorithmWorker extends SwingWorker<Chromosome<?>, GenerationResult>
            implements GAProgressListener {

        private final int width;
        private final int height;

        public GeneticAlgorithmWorker() {
            this.width = targetImage.getWidth();
            this.height = targetImage.getHeight();
        }

        @Override
        protected Chromosome<?> doInBackground() throws Exception {
            // Convert target image to genes
            List<Integer> targetGenes = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = targetImage.getRGB(x, y);
                    targetGenes.add(Utils.pixelToGene(pixel));
                }
            }

            int numShapes = 1500;

            // Create fitness function
//            MeanSquareError fitnessFunction = new MeanSquareError(targetGenes);
            ShapeMSE fitnessFunction = new ShapeMSE(targetGenes, width, height);

            // Get selected shape type from combo box
            int shapeType = shapeTypeComboBox.getSelectedIndex(); // 0=circle, 1=square, 2=rectangle, 3=triangle, 4=mixed

//            ChromosomeFactory<Integer> imageFactory = new ImageChromosomeFactory(targetGenes, 0.1, true);
//            ChromosomeFactory<Integer> imageFactory = new IntegerChromosomeFactory();
            ShapeFactory imageFactory = new ShapeFactory(width, height, numShapes, targetGenes);

            // Set the shape type (for mixed shapes index 4, set to 4 or higher to trigger randomization)
            imageFactory.setType(shapeType);

            // Allow type mutation only for mixed shapes mode
            boolean allowTypeMutation = (shapeType == 4);

            // GA configuration
            GAConfig<Chromosome<Integer>, Integer> config = GAConfig.<Chromosome<Integer>, Integer>builder()
                    .chromosomeSize(numShapes * 9) // 9 genes per shape: [type, x, y, param1, param2, r, g, b, a]
                    .range(new Range<>(0, Math.max(width, 255)))
                    .chromosomeFactory(imageFactory)
                    .fitnessFunction(fitnessFunction)
                    .infeasibleCheck(new ShapeInfeasibleCheck(width, height))
                    .selectionStrategy(new TournamentSelection<>(6))
                    .mutation(new ShapeMutation(width, height, allowTypeMutation))
                    .crossover(new NPointCrossover<>(numShapes/3))
                    .populationSize(POPULATION_SIZE)
                    .generations(MAX_GENERATIONS)
                    .mutationRate(MUTATION_RATE)
                    .crossoverRate(CROSSOVER_RATE)
                    .optimization(Optimization.MAXIMIZE)
                    .replacement(new ElitistReplacement<>(new CutoffFunction<>()))
                    .build();

            // Create and configure engine
            gaEngine = new GeneticAlgorithmEngine<Integer>(config);
            gaEngine.addListener(this);

            return gaEngine.run();
        }

        @Override
        public void onGenerationComplete(int generation,
                                         Chromosome<?> bestChromosome) {
            if (bestChromosome == null) return;

            // Render best chromosome
            BufferedImage rendered = ImageRenderer.renderChromosome(
                    (Chromosome<Integer>) bestChromosome, width, height);
//            System.out.println("Generation " + generation +
//                    " - Best MSE: " + bestChromosome.getFitness());

            // Publish to UI thread
            publish(new GenerationResult(generation, bestChromosome.getFitness(), rendered));
        }

        @Override
        public void onComplete(Chromosome<?> finalBest) {
            if (finalBest == null) return;
            BufferedImage rendered = ImageRenderer.renderChromosome((Chromosome<Integer>) finalBest, width, height);
            lastGeneratedImage = rendered; // Store the final image
            System.out.println("GA completed with final MSE: " + finalBest.getFitness());
        }

        @Override
        protected void process(List<GenerationResult> chunks) {
            // Get latest result
            GenerationResult latest = chunks.get(chunks.size() - 1);

            // Update UI
            generationLabel.setText("Generation: " + latest.generation);
            fitnessLabel.setText(String.format("Best MSE: %.2f", latest.fitness));
            progressBar.setValue(latest.generation);

            if (latest.image != null) {
                lastGeneratedImage = latest.image; // Store for saving later
                Image scaled = latest.image.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
                generatedImageLabel.setIcon(new ImageIcon(scaled));
                generatedImageLabel.setText(null);
            }
        }

        @Override
        protected void done() {
            isRunning = false;
            startButton.setEnabled(true);
            loadImageButton.setEnabled(true);
            shapeTypeComboBox.setEnabled(true);
            stopButton.setEnabled(false);

            // Enable save button if we have a generated image
            if (lastGeneratedImage != null) {
                saveButton.setEnabled(true);
            }

            try {
                Chromosome<?> result = get();
                if (result != null) {
                    JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                            String.format("GA completed!\nFinal MSE: %.2f\nUse 'Save Image' button to save the result.",
                                    result.getFitness()),
                            "Complete", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                if (!isCancelled()) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                            "An error occurred: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArtisticApproximationUI ui = new ArtisticApproximationUI();
            ui.setVisible(true);
        });
    }
}
