import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class CompetitorManagerGUI {
    private JFrame frame;
    private CompetitorList competitorList;
    private Manager manager;

    public CompetitorManagerGUI(Manager manager, CompetitorList competitorList) {
        this.manager = manager;
        this.competitorList = competitorList;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Competitor Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // add panel
        frame.add(createScorePanel(), BorderLayout.NORTH);
        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createDetailsPanel(), BorderLayout.SOUTH);

        // add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.generateReport("finalReport.txt");
                frame.dispose();
            }
        });
        frame.add(closeButton, BorderLayout.PAGE_END);

        // set window size
        frame.setSize(600, 400);
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

        viewButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor competitor = competitorList.findCompetitor(id);
            if (competitor != null) {
                scoresField.setText(Arrays.toString(competitor.getScoreArray()));
            } else {
                JOptionPane.showMessageDialog(frame, "Competitor not found.");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    // transfer text into score of scoresField
                    int[] updatedScores = parseScores(scoresField.getText());
                    competitor.setScoreArray(updatedScores);

                    // recalculate total score and represent it
                    double newOverallScore = competitor.getOverallScore(); // based on score array
                    JOptionPane.showMessageDialog(frame, "Scores updated. New overall score: " + newOverallScore);
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
        String[] columnNames = {"ID", "Name", "Category", "Overall Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        for (Competitor competitor : competitorList.getAllCompetitors()) {
            model.addRow(new Object[]{competitor.getId(), competitor.getFullName(), competitor.getCategory(), competitor.getOverallScore()});
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }


    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Competitor ID:");
        JTextField competitorIdField = new JTextField(10);
        JButton fullDetailsButton = new JButton("Full Details");
        JButton shortDetailsButton = new JButton("Short Details");
        JTextArea detailsArea = new JTextArea(5, 30);

        fullDetailsButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor competitor = competitorList.findCompetitor(id);
            if (competitor != null) {
                detailsArea.setText(competitor.getFullDetails());
            } else {
                JOptionPane.showMessageDialog(frame, "Competitor not found.");
            }
        });

        shortDetailsButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor competitor = competitorList.findCompetitor(id);
            if (competitor != null) {
                detailsArea.setText(competitor.getShortDetails());
            } else {
                JOptionPane.showMessageDialog(frame, "Competitor not found.");
            }
        });

        panel.add(label);
        panel.add(competitorIdField);
        panel.add(fullDetailsButton);
        panel.add(shortDetailsButton);
        panel.add(new JScrollPane(detailsArea));
        return panel;
    }

}
