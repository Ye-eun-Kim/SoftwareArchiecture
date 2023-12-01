import java.util.ArrayList;
import java.util.List;


public class Competitor {
    private int id;
    private int age;
    private Name name;
    private String email;
    private String category;
    private String scores;
    private double overallScore;

    public Competitor(int id, int age, Name name, String email, String category, String scores, double overallScore) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.email = email;
        this.category = category;
        this.scores = scores;
        this.overallScore = overallScore;
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

    public String getScores() {
        return scores;
    }

    public double getOverallScore(){
        return overallScore;
    }

    public String getFullDetails(){
        return "Competitor number: "+id+", Name: "+getFullName()+", Age: "+age+", Category: "+category
                +"\nScores: "+scores+", Overall score: "+overallScore+".";
    }

    public String getShortDetails(){
        return "CN "+id+" ("+name.getInitials()+") has overall score "+overallScore+".";
    }
}
