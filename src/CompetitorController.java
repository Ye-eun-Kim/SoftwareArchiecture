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
                String idStr = entry[0].trim();
                idStr = idStr.replaceAll("[^\\d]", "");
                int id = Integer.parseInt(idStr);

                String nameStr = entry[1];
                Name name = createNameObject(nameStr);

                Date dateOfBirth = new SimpleDateFormat("dd-MM-yyyy").parse(entry[2]);
                int age = calculateAge(dateOfBirth);

                String category = entry[3];

                String email = entry[4];

                int[] scoreValues = new int[4];
                for (int i = 5; i <= 8; i++) {
                    String score = entry[i];
                    int scoreValue = Integer.valueOf(score);
                    scoreValues[i-5] = scoreValue;
                }

                Competitor competitor;
                if (category.equals("Freestyle")) {
                    competitor = new FreestyleCompetitor(id, age, name, email, scoreValues);
                } else if (category.equals(("Butterfly"))) {
                    competitor = new ButterflyCompetitor(id, age, name, email, scoreValues);
                } else {
                    competitor = new Competitor(id, age, name, email, category, scoreValues);
                }
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
}
