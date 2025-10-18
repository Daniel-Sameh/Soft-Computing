package fcai.sclibrary.usecases.artisticImageApproximation;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple and effective Java Swing UI for the Artistic Image Approximation project.
 * This UI uses a SwingWorker to run the genetic algorithm in the background,
 * ensuring the UI remains responsive and updates with the latest generated image.
 */
public class ArtisticApproximationUI extends JFrame {

    // --- UI Components ---
    private JButton loadImageButton;
    private JButton startButton;
    private JLabel targetImageLabel;
    private JLabel generatedImageLabel;
    private JLabel generationLabel;
    private JLabel fitnessLabel;
    private JProgressBar progressBar;

    // --- Image Data ---
    private BufferedImage targetImage;

    // --- GA Parameters (for demonstration) ---
    private static final int MAX_GENERATIONS = 10000;

    public ArtisticApproximationUI() {
        super("Artistic Image Approximation GA");

        // --- Basic Window Setup ---
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null); // Center the window

        // --- UI Initialization ---
        initUI();

        // Use the system's look and feel for a modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not set system look and feel.");
        }
    }

    private void initUI() {
        // --- Main Panels ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = createControlPanel();
        JPanel imagePanel = createImagePanel();
        JPanel statusPanel = createStatusPanel();

        // --- Add Panels to Frame ---
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(imagePanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadImageButton = new JButton("Load Target Image...");
        startButton = new JButton("Start Genetic Algorithm");
        startButton.setEnabled(false); // Disabled until an image is loaded

        panel.add(loadImageButton);
        panel.add(startButton);

        // --- Action Listeners ---
        loadImageButton.addActionListener(this::loadImageAction);
        startButton.addActionListener(this::startGaAction);

        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Target Image Panel
        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.setBorder(new TitledBorder("Target Image"));
        targetImageLabel = new JLabel("Please load an image.", SwingConstants.CENTER);
        targetPanel.add(targetImageLabel, BorderLayout.CENTER);

        // Generated Image Panel
        JPanel generatedPanel = new JPanel(new BorderLayout());
        generatedPanel.setBorder(new TitledBorder("Generated Approximation"));
        generatedImageLabel = new JLabel("Waiting to start...", SwingConstants.CENTER);
        generatedPanel.add(generatedImageLabel, BorderLayout.CENTER);

        panel.add(targetPanel);
        panel.add(generatedPanel);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        generationLabel = new JLabel("Generation: 0");
        fitnessLabel = new JLabel("Best Error: N/A");
        progressBar = new JProgressBar(0, MAX_GENERATIONS);

        panel.add(generationLabel);
        panel.add(fitnessLabel);
        panel.add(progressBar);

        return panel;
    }

    // --- Action Handler Methods ---

    private void loadImageAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image File");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                targetImage = ImageIO.read(selectedFile);

                // Display the loaded image
                Image scaledImage = targetImage.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
                targetImageLabel.setIcon(new ImageIcon(scaledImage));
                targetImageLabel.setText(null);

                // Enable the start button
                startButton.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startGaAction(ActionEvent e) {
        if (targetImage == null) return;

        // Disable buttons to prevent multiple runs
        startButton.setEnabled(false);
        loadImageButton.setEnabled(false);

        // Start the GA on a background thread
        GeneticAlgorithmWorker worker = new GeneticAlgorithmWorker();
        worker.execute();
    }


    /**
     * A helper class to pass results from the GA worker thread to the UI thread.
     */
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

    /**
     * The SwingWorker class that runs the genetic algorithm in the background.
     */
    private class GeneticAlgorithmWorker extends SwingWorker<Void, GenerationResult> {

        @Override
        protected Void doInBackground() throws Exception {
            // =================================================================
            // == YOUR GENETIC ALGORITHM INITIALIZATION LOGIC GOES HERE ==
            //
            // Example:
            // Population population = new Population(POPULATION_SIZE, targetImage);
            // population.initialize();
            // =================================================================

            for (int gen = 1; gen <= MAX_GENERATIONS; gen++) {
                // =================================================================
                // == YOUR GA'S GENERATION LOGIC GOES HERE ==
                //
                // 1. Evaluate fitness of the population
                //    population.evaluateFitness();
                //
                // 2. Select parents and create the next generation
                //    population.createNewGeneration();
                //
                // 3. Get the best individual from the current population
                //    Individual bestIndividual = population.getFittest();
                //    double bestFitness = bestIndividual.getFitness();
                //    BufferedImage bestImage = bestIndividual.render();
                // =================================================================


                // --- For Demonstration: Create a fake result ---
                // Replace this with your actual GA logic.
                double bestFitness = 1000.0 / gen; // Fake fitness that improves over time
                BufferedImage bestImage = createPlaceholderImage(gen); // Fake image


                // Publish the intermediate result to the UI thread
                publish(new GenerationResult(gen, bestFitness, bestImage));

                // Allow the event thread to process updates
                Thread.sleep(10);
            }
            return null;
        }

        @Override
        protected void process(List<GenerationResult> chunks) {
            // This method runs on the UI thread and receives results from publish()
            GenerationResult latestResult = chunks.get(chunks.size() - 1);

            // Update UI components
            generationLabel.setText("Generation: " + latestResult.generation);
            fitnessLabel.setText(String.format("Best Error: %.4f", latestResult.fitness));
            progressBar.setValue(latestResult.generation);

            Image scaledImage = latestResult.image.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
            generatedImageLabel.setIcon(new ImageIcon(scaledImage));
            generatedImageLabel.setText(null);
        }

        @Override
        protected void done() {
            // This method runs on the UI thread after the GA finishes
            try {
                get(); // To catch any exceptions from doInBackground
                JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                        "Genetic Algorithm finished!", "Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ArtisticApproximationUI.this,
                        "An error occurred during execution: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Re-enable buttons
                startButton.setEnabled(true);
                loadImageButton.setEnabled(true);
            }
        }

        // Helper method to create a placeholder image for demonstration
        private BufferedImage createPlaceholderImage(int generation) {
            if (targetImage == null) return null;
            BufferedImage img = new BufferedImage(targetImage.getWidth(), targetImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0,0, img.getWidth(), img.getHeight());

            // Draw some random "progress"
            g.setColor(Color.BLUE);
            g.setFont(new Font("Serif", Font.BOLD, 40));
            g.drawString("Gen: " + generation, 50, 100);
            g.dispose();
            return img;
        }
    }


    public static void main(String[] args) {
        // Run the UI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            ArtisticApproximationUI ui = new ArtisticApproximationUI();
            ui.setVisible(true);
        });
    }
}
