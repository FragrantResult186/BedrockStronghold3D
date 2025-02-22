package fragrant.viewer.ui;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    private final JTextField seedField;
    private final JTextField chunkXField;
    private final JTextField chunkZField;

    public InputPanel(Runnable onGenerate) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        seedField = new JTextField("0", 20);
        chunkXField = new JTextField("0", 10);
        chunkZField = new JTextField("0", 10);

        add(new JLabel("Seed:"));
        add(seedField);
        add(new JLabel("Chunk X:"));
        add(chunkXField);
        add(new JLabel("Chunk Z:"));
        add(chunkZField);

        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> onGenerate.run());
        add(generateButton);

        add(Box.createHorizontalStrut(20));
        add(new JLabel(
                "<html>Controls:<br>" +
                        "Left Mouse: Rotate camera<br>" +
                        "Right Mouse: Pan camera<br>" +
                        "Middle Mouse/Wheel: Zoom<br>" +
                        "WASD: Move camera<br>" +
                        "Space/Shift: Up/Down</html>"
        ));
    }

    public String getSeed() {
        return seedField.getText();
    }

    public String getChunkX() {
        return chunkXField.getText();
    }

    public String getChunkZ() {
        return chunkZField.getText();
    }
}
