import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        // 세 개의 주요 패널을 포함하는 메인 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // 수직 정렬
        mainPanel.add(createScorePanel());
        mainPanel.add(createTablePanel());
        mainPanel.add(createDetailsPanel());

        // 메인 패널을 프레임 중앙에 배치
        frame.add(mainPanel, BorderLayout.CENTER);

        // 닫기 버튼을 포함하는 하단 패널 생성 및 추가
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            manager.generateReport("finalReport.txt");
            frame.dispose();
        });
        bottomPanel.add(closeButton);

        // 하단 패널을 프레임 하단에 배치
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        // 프레임 크기 설정 및 표시
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

        viewButton.addActionListener(e -> {
            int id = Integer.parseInt(competitorIdField.getText());
            Competitor competitor = competitorList.findCompetitor(id);
            if (competitor != null) {
                scoresField.setText(competitor.getScoreString());
            } else {
                JOptionPane.showMessageDialog(frame, "Competitor not found.");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    // 새로운 점수 배열을 Competitor 객체에 반영
                    int[] updatedScores = parseScores(scoresField.getText());
                    competitor.setScoreArray(updatedScores);

                    // 전체 점수를 재계산
                    double newOverallScore = competitor.getOverallScore();

                    // 사용자에게 새로운 전체 점수 알림
                    JOptionPane.showMessageDialog(frame, "Scores updated. New overall score: " + newOverallScore);

                    // 테이블 정보 업데이트
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

        // 경쟁자 데이터로 테이블 채우기
        for (Competitor competitor : competitorList.getAllCompetitors()) {
            model.addRow(new Object[]{competitor.getId(), competitor.getFullName(), competitor.getCategory(), competitor.getScoreString(), competitor.getOverallScore()});
        }

        // 정렬 옵션 및 버튼을 담을 새로운 패널 생성
        JPanel sortPanel = new JPanel(); // FlowLayout이 기본값
        JComboBox<String> sortOptions = new JComboBox<>(new String[]{"ID", "Name", "Category", "Scores", "Overall Score"});
        JButton sortButton = new JButton("Sort");

        sortButton.addActionListener(e -> {
            String selectedOption = (String) sortOptions.getSelectedItem();
            sortCompetitorList(selectedOption, model);
        });

        // 정렬 옵션 및 버튼을 패널에 추가
        sortPanel.add(sortOptions);
        sortPanel.add(sortButton);

        // 패널을 BorderLayout.NORTH에 추가
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

        // 업데이트된 정렬된 목록을 테이블 모델에 반영
        updateTableModel(sortedCompetitors);
    }

    private void updateTableModel(List<Competitor> sortedCompetitors) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // 테이블 데이터 클리어

        // 정렬된 목록이 제공되면 해당 목록 사용, 아니면 기본 목록 사용
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

        // 상단: 경쟁자 정보 조회
        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Competitor ID:");
        JTextField competitorIdField = new JTextField(10);
        JButton viewButton = new JButton("View Details");
        topPanel.add(label);
        topPanel.add(competitorIdField);
        topPanel.add(viewButton);

        // 중앙: 세부 정보 표시
        JTextArea detailsArea = new JTextArea(5, 30);
        detailsArea.setEditable(false); // 기본적으로 편집 불가

        // 하단: 세부 정보 수정
        JPanel bottomPanel = new JPanel();
        JButton editButton = new JButton("Edit Details");
        JButton saveButton = new JButton("Save Changes");
        saveButton.setEnabled(false); // 처음에는 비활성화

        editButton.addActionListener(e -> {
            detailsArea.setEditable(true); // 편집 가능하도록 설정
            saveButton.setEnabled(true); // 저장 버튼 활성화
        });

        viewButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(competitorIdField.getText());
                Competitor competitor = competitorList.findCompetitor(id);
                if (competitor != null) {
                    // 세부 정보를 텍스트 영역에 표시
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
                    // detailsArea의 텍스트로 경쟁자 정보 업데이트 로직 구현 필요
                    updateCompetitorDetails(competitor, detailsArea.getText());
                    detailsArea.setEditable(false); // 편집 불가능하도록 설정
                    saveButton.setEnabled(false); // 저장 버튼 비활성화
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

        // 패널 구성
        panel.add(topPanel, NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(bottomPanel, SOUTH);

        return panel;
    }

    private void updateCompetitorDetails(Competitor competitor, String details) {
        // 세부 정보 파싱 로직
        String[] lines = details.split("\n");

        // 첫 번째 줄 처리 (경쟁자 번호, 이름, 나이, 카테고리)
        String[] firstLineDetails = lines[0].split(", ");
        String name = firstLineDetails[1].substring(6); // "Name: " 이후의 문자열
        int age = 0;
        try {
            age = Integer.parseInt(firstLineDetails[2].substring(firstLineDetails[2].indexOf(": ") + 2));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid age format. Please enter a numeric value.");
            return;
        }
        String category = firstLineDetails[3].substring(firstLineDetails[3].indexOf(": ") + 2);
        // 유효한 카테고리 목록
        List<String> validCategories = Arrays.asList("Freestyle", "Butterfly");
        if (!validCategories.contains(category)) {
            JOptionPane.showMessageDialog(frame, "Invalid category. Please enter a valid category.");
            return;
        }

        // 두 번째 줄 처리 (점수)
        String[] secondLineDetails = lines[1].split(", ");
        String[] scoreStrings = secondLineDetails[0].substring(8).split(","); // "Scores: " 이후의 문자열을 쉼표로 분리
        int[] scoreValues = new int[scoreStrings.length];
        try {
            for (int i = 0; i < scoreStrings.length; i++) {
                scoreValues[i] = Integer.parseInt(scoreStrings[i].trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid score format.");
            return;
        }

        // 세부 정보 업데이트
        competitor.setName(name);
        competitor.setAge(age);
        competitor.setCategory(category);
        competitor.setScoreArray(scoreValues);

        // 테이블 업데이트
        updateTableModel(null);
    }


}
