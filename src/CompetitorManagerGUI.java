import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;

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

    // Parse score text to an array of scores
    private int[] parseScores(String scoresText) {
        String[] scoreStrings = scoresText.trim().split("\\s*,\\s*");
        int[] scores = new int[scoreStrings.length];
        for (int i = 0; i < scoreStrings.length; i++) {
            scores[i] = Integer.parseInt(scoreStrings[i]);
        }
        return scores;
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
        model.setRowCount(0); // 테이블 데이터 클리어

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


    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        // Top: Look up competitor information
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Competitor ID:");
        JTextField competitorIdField = new JTextField(10);
        JButton viewButton = new JButton("View Details");
        topPanel.add(label);
        topPanel.add(competitorIdField);
        topPanel.add(viewButton);

        // center: show details
        JTextArea detailsArea = new JTextArea(5, 30);
        detailsArea.setEditable(false); // Editing is not possible by default

        // bottom: edit details
        JPanel bottomPanel = new JPanel();
        JButton editButton = new JButton("Edit Details");
        JButton saveButton = new JButton("Save Changes");
        saveButton.setEnabled(false); // initially disabled

        editButton.addActionListener(e -> {
            detailsArea.setEditable(true); // Make it editable
            saveButton.setEnabled(true); // Activate save button
        });

        viewButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    // Display details in text area
                    detailsArea.setText(competitor.getFullDetails());
                } else {
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Competitor ID.");
            }
        });

        saveButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    updateCompetitorDetails(competitor, detailsArea.getText());
                    detailsArea.setEditable(false);
                    saveButton.setEnabled(false);
                    JOptionPane.showMessageDialog(frame, "Details updated.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Competitor not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Competitor ID.");
            }
        });

        bottomPanel.add(editButton);
        bottomPanel.add(saveButton);

        panel.add(topPanel, NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(bottomPanel, SOUTH);

        return panel;
    }

    private void updateCompetitorDetails(Competitor competitor, String details) {
        // Detailed information parsing logic
        String[] lines = details.split("\n");

        // Process first line (competitor number, name, age, category)
        String[] firstLineDetails = lines[0].split(", ");
        String name = firstLineDetails[1].substring(6);  // String after "Name: "
        int age = 0;
        try {
            age = Integer.parseInt(firstLineDetails[2].substring(firstLineDetails[2].indexOf(": ") + 2));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid age format. Please enter a numeric value.");
            return;
        }
        String category = firstLineDetails[3].substring(firstLineDetails[3].indexOf(": ") + 2);
        // list of valid categories
        List<String> validCategories = Arrays.asList("Freestyle", "Butterfly");
        if (!validCategories.contains(category)) {
            JOptionPane.showMessageDialog(frame, "Invalid category. Please enter a valid category.");
            return;
        }

        // Process second line (score)
        String[] secondLineDetails = lines[1].split(", ");
        String[] scoreStrings = secondLineDetails[0].substring(8).split(","); // Separate the string after "Scores: " with commas
        int[] scoreValues = new int[scoreStrings.length];
        try {
            for (int i = 0; i < scoreStrings.length; i++) {
                scoreValues[i] = Integer.parseInt(scoreStrings[i].trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid score format.");
            return;
        }

        // update details
        competitor.setName(name);
        competitor.setAge(age);
        competitor.setCategory(category);
        competitor.setScoreArray(scoreValues);

        // update table
        updateTableModel(null);
    }


}
