import java.text.DecimalFormat;
import java.util.Arrays;

public class Competitor {
    private int id;
    private int age;
    private Name name;
    private String email;
    private String category;
    private int[] scoreArray;
    private double overallScore;

    public Competitor(int id, int age, Name name, String email, String category, int[] scoreArray) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.email = email;
        this.category = category;
        this.scoreArray = scoreArray;
        this.overallScore = setOverallScore(scoreArray);
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getFullName() {
        return name.getFullName();
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public String getFullDetails(){
        return "Competitor number: "+id+", Name: "+getFullName()+", Age: "+age+", Category: "+category
                +"\nScores: "+ getScoreString() +", Overall score: "+overallScore+".";
    }

    public String getShortDetails(){
        return "CN "+id+" ("+name.getInitials()+") has overall score "+overallScore+"."; }


    public int[] getScoreArray() {
        return scoreArray;
    }

    public String getScoreString(){
        if (scoreArray == null || scoreArray.length == 0) {
            return "";
        }
        StringBuilder scores = new StringBuilder();
        for (int score : scoreArray) {
            if (scores.length() > 0) {
                scores.append(",");
            }
            scores.append(score);
        }

        return scores.toString();
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setScoreArray(int[] scoreArray) {
        this.scoreArray = scoreArray;
    }

    public double setOverallScore(int[] scores) {
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
