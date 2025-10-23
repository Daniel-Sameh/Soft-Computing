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
import fcai.sclibrary.ga.operators.replacement.GenerationalReplacement;
import fcai.sclibrary.ga.operators.replacement.functions.CutoffFunction;
import fcai.sclibrary.ga.operators.replacement.functions.MixingFunction;
import fcai.sclibrary.ga.operators.selection.RankSelection;
import fcai.sclibrary.ga.operators.selection.RouletteWheelSelection;
import fcai.sclibrary.ga.operators.selection.TournamentSelection;
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
    private JLabel targetImageLabel;
    private JLabel generatedImageLabel;
    private JLabel generationLabel;
    private JLabel fitnessLabel;
    private JProgressBar progressBar;

    private BufferedImage targetImage;
    private GeneticAlgorithmEngine gaEngine;
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
        startButton = new JButton("Start Genetic Algorithm");
        stopButton = new JButton("Stop");

        startButton.setEnabled(false);
        stopButton.setEnabled(false);

        panel.add(loadImageButton);
        panel.add(startButton);
        panel.add(stopButton);

        loadImageButton.addActionListener(e -> loadImageAction());
        startButton.addActionListener(e -> startGaAction());
        stopButton.addActionListener(e -> stopGaAction());

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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image File");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                targetImage = ImageIO.read(selectedFile);

                Image scaledImage = targetImage.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
                targetImageLabel.setIcon(new ImageIcon(scaledImage));
                targetImageLabel.setText(null);

                startButton.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error loading image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startGaAction() {
        if (targetImage == null || isRunning) return;

        startButton.setEnabled(false);
        loadImageButton.setEnabled(false);
        stopButton.setEnabled(true);
        isRunning = true;

        GeneticAlgorithmWorker worker = new GeneticAlgorithmWorker();
        worker.execute();
    }

    private void stopGaAction() {
        if (gaEngine != null) {
            gaEngine.stop();
        }
        stopButton.setEnabled(false);
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

            int numShapes = 1200;

            // Create fitness function
//            MeanSquareError fitnessFunction = new MeanSquareError(targetGenes);
            ShapeMSE fitnessFunction = new ShapeMSE(targetGenes, width, height);

//            ChromosomeFactory<Integer> imageFactory = new ImageChromosomeFactory(targetGenes, 0.1, true);
//            ChromosomeFactory<Integer> imageFactory = new IntegerChromosomeFactory();
            ChromosomeFactory<Integer> imageFactory = new ShapeFactory(width, height, numShapes, targetGenes);

            // GA configuration
            GAConfig<Chromosome<Integer>, Integer> config = GAConfig.<Chromosome<Integer>, Integer>builder()
                    .chromosomeSize(numShapes * 7) // 7 genes per shape
                    .range(new Range<>(0, Math.max(width, 255)))
                    .chromosomeFactory(imageFactory)
                    .fitnessFunction(fitnessFunction)
                    .selectionStrategy(new TournamentSelection<>(6))
                    .mutation(new ShapeMutation(width, height))
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
            SwingUtilities.invokeLater(() -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Save Final Image");
                chooser.setSelectedFile(new File("approximation.png"));
                chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
                chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));
                chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("BMP Image", "bmp"));
                int res = chooser.showSaveDialog(ArtisticApproximationUI.this);
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
                        ImageIO.write(rendered, ext.equals("jpg") ? "jpg" : ext, file);
                        JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                                "Saved final image to: " + file.getAbsolutePath(),
                                "Saved", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                                "Failed to save image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
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
            stopButton.setEnabled(false);

            try {
                Chromosome<?> result = get();
                JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                        String.format("GA completed!\nFinal MSE: %.2f",
                                result != null ? result.getFitness() : 0),
                        "Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                        "An error occurred: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
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
