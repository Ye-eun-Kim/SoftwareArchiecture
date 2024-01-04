import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.BorderLayout.NORTH;


public class CompetitorManagerGUI {
    private JFrame frame;
    private CompetitorList competitorList;
    private Manager manager;
    private JTable table;

    public CompetitorManagerGUI(Manager manager, CompetitorList competitorList) {
        this.manager = manager;
        this.competitorList = competitorList;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Competitor Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create main panel containing three primary panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical alignment
        mainPanel.add(createScorePanel());
        mainPanel.add(createTablePanel());
        mainPanel.add(createDetailsPanel());

        // Place main panel in the center of the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Create and add bottom panel containing the close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            manager.generateReport("finalReport.txt");
            frame.dispose();
        });
        bottomPanel.add(closeButton);

        // Place bottom panel at the bottom of the frame
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        // Set frame size and make it visible
        frame.pack();
        frame.setVisible(true);
    }


    private JPanel createScorePanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Competitor ID:");
        JTextField competitorIdField = new JTextField(10);
        JButton viewButton = new JButton("View Scores");
        JLabel scoresLabel = new JLabel("Scores:");
        JTextField scoresField = new JTextField(20);
        JButton updateButton = new JButton("Update Scores");

        // Display scores when 'View Scores' button is clicked
        viewButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor competitor = competitorList.findCompetitor(id);
            if (competitor != null) {
                scoresField.setText(competitor.getScoreString());
            } else {
                JOptionPane.showMessageDialog(frame, "Competitor not found.");
            }
        });

        // Update scores when 'Update Scores' button is clicked
        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    // Reflect new score array in Competitor object
                    int[] updatedScores = parseScores(scoresField.getText());
                    competitor.setScoreArray(updatedScores);

                    // Recalculate total score
                    double newOverallScore = competitor.getOverallScore();

                    // Notify user of new total score
                    JOptionPane.showMessageDialog(frame, "Scores updated. New overall score: " + newOverallScore);

                    // Update table information
                    updateTableModel(null);
                } else {
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number format.");
            }
        });

        panel.add(label);
        panel.add(competitorIdField);
        panel.add(viewButton);
        panel.add(scoresLabel);
        panel.add(scoresField);
        panel.add(updateButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Name", "Category", "Scores", "Overall Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        // Fill the table with competitor data
        for (Competitor competitor : competitorList.getAllCompetitors()) {
            model.addRow(new Object[]{competitor.getId(), competitor.getFullName(), competitor.getCategory(), competitor.getScoreString(), competitor.getOverallScore()});
        }

        // Create new panel for sorting options and button
        JPanel sortPanel = new JPanel(); // Default FlowLayout
        JComboBox<String> sortOptions = new JComboBox<>(new String[]{"ID", "Name", "Category", "Scores", "Overall Score"});
        JButton sortButton = new JButton("Sort");

        // Sort list based on selected criteria
        sortButton.addActionListener(e -> {
            String selectedOption = (String) sortOptions.getSelectedItem();
            sortCompetitorList(selectedOption, model);
        });

        sortPanel.add(sortOptions);
        sortPanel.add(sortButton);

        panel.add(sortPanel, NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }


    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top: View details and remove competitor
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Competitor ID:");
        JTextField competitorIdField = new JTextField(10);
        JButton viewButton = new JButton("View Details");
        JButton removeButton = new JButton("Remove Competitor");

        topPanel.add(label);
        topPanel.add(competitorIdField);
        topPanel.add(viewButton);
        topPanel.add(removeButton);

        // Center: View details
        JTextArea detailsArea = new JTextArea(5, 30);
        detailsArea.setEditable(false);

        // Bottom: Edit details
        JPanel bottomPanel = new JPanel();
        JButton editButton = new JButton("Edit Details");
        JButton saveButton = new JButton("Save Changes");
        saveButton.setEnabled(false);

        // Event listener
        viewButton.addActionListener(e -> {
            try{
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    detailsArea.setText(competitor.getFullDetails());
                } else {
                    detailsArea.setText("");
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch(NumberFormatException e2){
                JOptionPane.showMessageDialog(frame, "Invalid ID format. Please enter a numeric value.");
                detailsArea.setText("");
            }
        });

        editButton.addActionListener(e -> {
            detailsArea.setEditable(true);
            saveButton.setEnabled(true);
        });

        saveButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor originalCompetitor = competitorList.findCompetitor(id);
            try {
                if (originalCompetitor != null) {
                    String originalDetails = originalCompetitor.getFullDetails();
                    String updatedDetails = detailsArea.getText();

                    if (!originalDetails.equals(updatedDetails) && isValidDetails(originalDetails, updatedDetails)) {
                        updateCompetitorDetails(originalCompetitor, updatedDetails);
                        detailsArea.setEditable(false);
                        saveButton.setEnabled(false);
                        JOptionPane.showMessageDialog(frame, "Details updated");
                    } else {
                        detailsArea.setText(originalDetails); // Reset to original details
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Competitor ID.");
                detailsArea.setText(originalCompetitor.getFullDetails());
            }
        });

        removeButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                boolean isRemoved = competitorList.removeCompetitor(id);
                if (isRemoved) {
                    JOptionPane.showMessageDialog(frame, "Competitor removed successfully.");
                    updateTableModel(null); // Update table view
                } else {
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Competitor ID.");
            }
        });

        bottomPanel.add(editButton);
        bottomPanel.add(saveButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }


    // Helper methods to extract proper details
    private String extractID(String detailsLine) {
        String[] parts = detailsLine.split(", ");
        return parts[0].substring(parts[0].indexOf(": ") + 2);
    }
    private String extractCategory(String detailsLine) {
        String[] parts = detailsLine.split(", ");
        return parts[2].substring(parts[2].indexOf(": ") + 2);
    }
    private String extractAge(String detailsLine) throws NumberFormatException {
        String ageString = detailsLine.split(", ")[0].split(": ")[1];
        return ageString;
    }
    private String extractName(String detailsLine) {
        String[] parts = detailsLine.split(", ");
        return parts[1].substring(parts[1].indexOf(": ") + 2);
    }
    private String extractEmail(String detailsLine) {
        String[] parts = detailsLine.split(", ");
        return parts[1].split(": ")[1];
    }


    // Parse score text to an array of scores
    private int[] parseScores(String scoresText) {
        String[] scoreStrings = scoresText.trim().split("\\s*,\\s*");
        int[] scores = new int[scoreStrings.length];
        for (int i = 0; i < scoreStrings.length; i++) {
            scores[i] = Integer.parseInt(scoreStrings[i]);
        }
        return scores;
    }


    private void sortCompetitorList(String criteria, DefaultTableModel model) {
        Comparator<Competitor> comparator;

        switch (criteria) {
            case "ID":
                comparator = Comparator.comparing(Competitor::getId);
                break;
            case "Name":
                comparator = Comparator.comparing(Competitor::getFullName);
                break;
            case "Category":
                comparator = Comparator.comparing(Competitor::getCategory);
                break;
            case "Scores":
                comparator = Comparator.comparing(Competitor::getScoreString);
                break;
            case "Overall Score":
                comparator = Comparator.comparing(Competitor::getOverallScore);
                break;
            default:
                throw new IllegalArgumentException("Unknown sorting criteria: " + criteria);
        }

        List<Competitor> sortedCompetitors = new ArrayList<>(competitorList.getAllCompetitors());
        Collections.sort(sortedCompetitors, comparator);

        // Reflect updated sorted list in table model
        updateTableModel(sortedCompetitors);
    }


    private void updateTableModel(List<Competitor> sortedCompetitors) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the data of table

        // If a sorted list is provided, use that list, otherwise use the default list.
        List<Competitor> competitors = (sortedCompetitors != null) ? sortedCompetitors : competitorList.getAllCompetitors();

        for (Competitor competitor : competitors) {
            model.addRow(new Object[]{
                    competitor.getId(),
                    competitor.getFullName(),
                    competitor.getCategory(),
                    competitor.getScoreString(),
                    competitor.getOverallScore(),

            });
        }
    }


    private boolean isValidDetails(String originalDetails, String updatedDetails) {
        String[] originalLines = originalDetails.split("\n");
        String[] updatedLines = updatedDetails.split("\n");

        // Check if ID is changed
        String originalID = extractID(originalLines[0]);
        String updatedID = extractID(updatedLines[0]);
        if (!originalID.equals(updatedID)) {
            JOptionPane.showMessageDialog(frame, "ID cannot be edited here.");
            return false;
        }

        // Check if only the score is changed
        boolean isScoreChanged = !originalLines[2].equals(updatedLines[2]);
        if (isScoreChanged) {
            JOptionPane.showMessageDialog(frame, "Scores cannot be edited here. Please use the 'Update Scores' feature.");
            return false;
        }

        // Check for valid category
        String category = extractCategory(updatedLines[0]);
        List<String> validCategories = Arrays.asList("Freestyle", "Butterfly");
        if (!validCategories.contains(category)) {
            JOptionPane.showMessageDialog(frame, "Invalid category. Please enter a valid category.");
            return false;
        }

        // Check for valid age
        try {
            int age = Integer.parseInt(extractAge(updatedLines[1]));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid age format. Please enter a numeric value.");
            return false;
        }

        return true;
    }


    private void updateCompetitorDetails(Competitor competitor, String details) {
        String[] lines = details.split("\n");

        String name = extractName(lines[0]);
        String category = extractCategory(lines[0]);
        int age = Integer.parseInt(extractAge(lines[1]));
        String email = extractEmail(lines[1]);

        competitor.setName(name);
        competitor.setCategory(category);
        competitor.setAge(age);
        competitor.setEmail(email);

        updateTableModel(null);
    }
}
