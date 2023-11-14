import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearchApp extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private Path filePath;

    public FileSearchApp() {
        setTitle("File Search App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3));

        originalTextArea = new JTextArea();
        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        add(originalScrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        searchField = new JTextField();
        buttonPanel.add(searchField);

        loadButton = new JButton("Load File");
        loadButton.addActionListener(e -> loadFile());
        buttonPanel.add(loadButton);

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchFile());
        searchButton.setEnabled(false);
        buttonPanel.add(searchButton);

        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(quitButton);

        add(buttonPanel);

        filteredTextArea = new JTextArea();
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);
        add(filteredScrollPane);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            try {
                List<String> lines = Files.readAllLines(filePath);
                originalTextArea.setText(String.join("\n", lines));
                searchButton.setEnabled(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        String searchTerm = searchField.getText();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Stream<String> lines = Files.lines(filePath)) {
            List<String> filteredLines = lines.filter(line -> line.contains(searchTerm)).collect(Collectors.toList());
            filteredTextArea.setText(String.join("\n", filteredLines));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error searching the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileSearchApp app = new FileSearchApp();
            app.setVisible(true);
        });
    }
}
