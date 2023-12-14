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

        // 패널 추가
        frame.add(createScorePanel(), BorderLayout.NORTH);
        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createDetailsPanel(), BorderLayout.SOUTH);

        // 닫기 버튼 추가
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.generateReport("finalReport.txt");
                frame.dispose();
            }
        });
        frame.add(closeButton, BorderLayout.PAGE_END);

        // 창 크기 설정 및 보이기
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
                    // scoresField의 텍스트를 분석하여 점수 배열로 변환
                    int[] updatedScores = parseScores(scoresField.getText());
                    competitor.setScoreArray(updatedScores);

                    // 전체 점수를 재계산하고 결과 표시
                    double newOverallScore = competitor.getOverallScore(); // 이 메서드는 점수 배열을 기반으로 전체 점수를 계산합니다.
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
        // 경쟁자 데이터로 테이블을 채우는 로직 필요
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
