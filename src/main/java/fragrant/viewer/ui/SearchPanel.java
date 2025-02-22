package fragrant.viewer.ui;

import fragrant.finder.BE_Stronghold;
import fragrant.helper.Position;
import nl.jellejurre.biomesampler.BiomeSampler;

import javax.swing.*;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final JTextField seedInputField;
    private final JTextArea resultArea;
    private final JButton searchButton;

    public SearchPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Bedrock Stronghold Search"));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seedInputField = new JTextField(20);
        searchButton = new JButton("Search");
        inputPanel.add(new JLabel("Seed:"));
        inputPanel.add(seedInputField);
        inputPanel.add(searchButton);

        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setupListeners();
    }

    private void setupListeners() {
        searchButton.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        try {
            long structureSeed = Long.parseLong(seedInputField.getText());
            searchButton.setEnabled(false);
            resultArea.setText("Searching...\n");

            SwingWorker<Position.BlockPos[], Void> worker = new SwingWorker<>() {
                @Override
                protected Position.BlockPos[] doInBackground() {
                    long worldSeed = (0) | structureSeed;
                    BiomeSampler biomeSampler = new BiomeSampler(worldSeed);
                    return findValidStrongholds(worldSeed, biomeSampler);
                }

                @Override
                protected void done() {
                    try {
                        Position.BlockPos[] positions = get();
                        if (positions != null) {
                            displayResult(positions, structureSeed);
                        } else {
                            resultArea.setText("No valid seed found for the given structure seed.");
                        }
                    } catch (Exception ex) {
                        resultArea.setText("Error during search: " + ex.getMessage());
                    } finally {
                        searchButton.setEnabled(true);
                    }
                }
            };

            worker.execute();

        } catch (NumberFormatException ex) {
            resultArea.setText("Please enter a valid number for the structure seed.");
        }
    }

    private Position.BlockPos[] findValidStrongholds(long worldSeed, BiomeSampler biomeSampler) {
        return BE_Stronghold.getFirstThreeStrongholds(worldSeed, biomeSampler);
    }

    private void displayResult(Position.BlockPos[] positions, long structureSeed) {
        StringBuilder sb = new StringBuilder();
        sb.append("Search Results:\n\n");
        sb.append(String.format("Structure Seed: %d\n\n", structureSeed));
        sb.append("Stronghold Positions (Stairway):\n");

        for (int i = 0; i < positions.length; i++) {
            Position.BlockPos pos = positions[i];
            Position.ChunkPos chunkPos = pos.toChunkPos();
            sb.append(String.format("Stronghold %d:\n", i + 1));
            sb.append("─".repeat(30)).append("\n");
            sb.append(String.format("  Chunk: (%d, %d)\n", chunkPos.x(), chunkPos.z()));
            sb.append(String.format("  Block: (%d, %d)\n", pos.x(), pos.z()));
            sb.append("─".repeat(30)).append("\n");
        }

        resultArea.setText(sb.toString());
    }
}