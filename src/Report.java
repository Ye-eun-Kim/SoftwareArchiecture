import java.util.Map;

public class Report {
    private CompetitorList competitorList;

    public Report(CompetitorList competitorList) {
        this.competitorList = competitorList;
    }

    public String createReport() {
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append(createCompetitorTable());
        reportBuilder.append("\n----------------------------------\n");
        reportBuilder.append(createHighestScoringCompetitorDetails());
        reportBuilder.append("\n----------------------------------\n");
        reportBuilder.append(createSummaryStatistics());
        reportBuilder.append("\n----------------------------------\n");
        reportBuilder.append(createFrequencyReport());

        return reportBuilder.toString();
    }

    private String createCompetitorTable() {
        StringBuilder tableBuilder = new StringBuilder("Competitor Details Table:\n");
        for (Competitor competitor : competitorList.getAllCompetitors()) {
            tableBuilder.append(competitor.getFullDetails()).append("\n");
        }
        return tableBuilder.toString();
    }

    private String createHighestScoringCompetitorDetails() {
        Competitor highestScorer = competitorList.getHighestScoringCompetitor();
        return "Highest Scorer:\n" + (highestScorer != null ? highestScorer.getFullDetails() : "No competitors available") + "\n";
    }

    private String createSummaryStatistics() {
        // 예시: 평균, 최대, 최소 점수
        String summary = "Summary Statistics:\n";
        summary += "Average Score: " + competitorList.getAverageScore() + "\n";
        summary += "Maximum Score: " + competitorList.getMaxScore() + "\n";
        summary += "Minimum Score: " + competitorList.getMinScore() + "\n";
        return summary;
    }

    private String createFrequencyReport() {
        StringBuilder frequencyBuilder = new StringBuilder("Score Frequency Report:\n");
        Map<Integer, Integer> frequencyMap = competitorList.getScoreFrequency();
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            frequencyBuilder.append("Score ").append(entry.getKey())
                    .append(": ").append(entry.getValue()).append(" times\n");
        }
        return frequencyBuilder.toString();
    }
}
