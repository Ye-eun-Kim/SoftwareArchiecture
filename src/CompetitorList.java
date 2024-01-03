import java.util.*;

public class CompetitorList {
    private List<Competitor> competitors;

    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
        System.out.println(competitor.getFullDetails());

    }

    public boolean removeCompetitor(int competitorId) {
        // If the size of list is changed, return true, which means a competitor deleted
        int originalSize = competitors.size();
        competitors.removeIf(competitor -> competitor.getId() == competitorId);
        return competitors.size() < originalSize;
    }


    public Competitor findCompetitor(int competitorNumber) {
        for (Competitor competitor : competitors) {
            if (competitor.getId() == competitorNumber) {
                return competitor;
            }
        }
        return null;
    }

    public List<Competitor> getAllCompetitors(){
        return this.competitors;
    }

    public Competitor getHighestScoringCompetitor() {
        return Collections.max(competitors, Comparator.comparing(Competitor::getOverallScore));
    }

    public double getAverageScore() {
        if (competitors.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Competitor competitor : competitors) {
            total += competitor.getOverallScore();
        }
        return total / competitors.size();
    }

    public double getMaxScore() {
        return competitors.stream()
                .mapToDouble(Competitor::getOverallScore)
                .max()
                .orElse(0.0);
    }

    public double getMinScore() {
        return competitors.stream()
                .mapToDouble(Competitor::getOverallScore)
                .min()
                .orElse(0.0);
    }

    public Map<Integer, Integer> getScoreFrequency() {
        Map<Integer, Integer> frequency = new HashMap<>();
        for (Competitor competitor : competitors) {
            for (int score : competitor.getScoreArray()) {
                frequency.put(score, frequency.getOrDefault(score, 0) + 1);
            }
        }
        return frequency;
    }
}
