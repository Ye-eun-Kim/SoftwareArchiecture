import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CompetitorController {

    private CompetitorList competitorList;

    public CompetitorController(CompetitorList list) {
        this.competitorList = list;
    }

    public void processData(List<String[]> data) {
        for (String[] entry : data) {
            try {
                int id = Integer.parseInt(entry[0]);
                String nameStr = entry[1];
                Name name = createNameObject(nameStr);
                Date dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(entry[2]);
                int age = calculateAge(dateOfBirth);
                String category = entry[3];
                String email = entry[4];
                String scores = "";
                int[] scoreValues = new int[4];
                for (int i = 5; i <= 8; i++) {
                    String score = entry[i];
                    int scoreValue = Integer.valueOf(score);
                    scoreValues[i-5] = scoreValue;
                    if (i == 5)
                        scores = score;
                    else
                        scores = scores + "," + score;
                }
                double overallScore = getOverallScore(scoreValues);

                Competitor competitor = new Competitor(id, age, name, email, category, scores, overallScore );

                competitorList.addCompetitor(competitor);
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private Name createNameObject(String name) {
        String[] nameParts = name.split("\\s+");
        if (nameParts.length == 2) {
            // Only first and last names are present
            return new Name(nameParts[0], nameParts[1]);
        } else if (nameParts.length >= 3) {
            // First, middle, and last names are present
            String middleName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length - 1));
            return new Name(nameParts[0], middleName, nameParts[nameParts.length - 1]);
        }
        return null;
    }

    private int calculateAge(Date dateOfBirth) {
        LocalDate birthDate = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    private double getOverallScore(int[] scores) {
        if (scores == null || scores.length == 0) {
            return 0.0;
        }
        // Calculate the harmonic sum. Harmonic mean is a type of average which is useful for numbers defined in a range.
        // It gives less weight to larger numbers and more weight to smaller numbers.
        double harmonicSum = 0.0;
        for (int score : scores) {
            if (score != 0) {
                harmonicSum += 1.0 / score;
            }
        }
        // Calculate harmonic mean. The harmonic mean is the total number of scores divided by the harmonic sum.
        double harmonicMean = scores.length / harmonicSum;
        // Normalize the harmonic mean to be within the range of 0 to 5. This step adjusts the score to a standard scale.
        double normalizedScore = normalizeScore(harmonicMean, 0, 10, 0, 5);
        DecimalFormat df = new DecimalFormat("#.#");
        return Double.parseDouble(df.format(normalizedScore));
    }

    private double normalizeScore(double score, double min, double max, double newMin, double newMax) {
        double normalized = (score - min) / (max - min) * (newMax - newMin) + newMin;
        if (normalized < newMin) {
            normalized = newMin;
        } else if (normalized > newMax) {
            normalized = newMax;
        }
        return normalized;
    }

}
