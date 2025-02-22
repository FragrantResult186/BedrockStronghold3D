package fragrant.viewer.main;

import fragrant.viewer.graphics.Camera;
import fragrant.viewer.graphics.GLPanel;
import fragrant.viewer.graphics.StrongholdRenderer;
import fragrant.viewer.ui.InputPanel;
import fragrant.viewer.ui.SearchPanel;

import javax.swing.*;
import java.awt.*;

public class StrongholdViewer extends JFrame {
    private final Camera camera;
    private final GLPanel glPanel;
    private final InputPanel inputPanel;
    private final StrongholdRenderer renderer;

    public StrongholdViewer() {
        super("Bedrock Stronghold 3D");

        camera = new Camera();
        renderer = new StrongholdRenderer();

        glPanel = new GLPanel(camera, renderer);
        inputPanel = new InputPanel(this::generateStronghold);
        SearchPanel searchPanel = new SearchPanel();

        setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(searchPanel, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(400, 0));

        add(inputPanel, BorderLayout.NORTH);
        add(glPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        setSize(1424, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void generateStronghold() {
        try {
            long seed = Long.parseLong(inputPanel.getSeed());
            int chunkX = Integer.parseInt(inputPanel.getChunkX());
            int chunkZ = Integer.parseInt(inputPanel.getChunkZ());

            renderer.generateStronghold(seed, chunkX, chunkZ);
            camera.centerOn(chunkX * 16, 0, chunkZ * 16);
            glPanel.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for seed and coordinates.");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            StrongholdViewer viewer = new StrongholdViewer();
            viewer.setVisible(true);
            viewer.glPanel.requestFocusInWindow();
        });
    }
}