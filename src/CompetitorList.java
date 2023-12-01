import java.util.ArrayList;
import java.util.List;

public class CompetitorList {
    private List<Competitor> competitors;

    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    public void addCompetitor(Competitor competitor) {
        competitors.add(competitor);
    }

    public void removeCompetitor(int competitorId) {
        competitors.removeIf(competitor -> competitor.getId() == competitorId);
    }

    public Competitor findCompetitor(int competitorId) {
        for (Competitor competitor : competitors) {
            if (competitor.getId() == competitorId) {
                return competitor;
            }
        }
        return null; // or throw an exception if competitor not found
    }
}
