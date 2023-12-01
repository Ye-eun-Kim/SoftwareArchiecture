import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private CompetitorController competitorController;

    public Manager(CompetitorController controller) {
        this.competitorController = controller;
    }

    public static void readFromFile(String filename){
        List<String[]> parsedData = new ArrayList<>();

        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            while(br.ready()){
                String line = br.readLine();
                String[] data = line.split(",");
                parsedData.add(data);
            }
            br.close();
        }
        catch(IOException e){
            System.out.println("Not read");
        }
        competitorController.processData(parsedData);
    }

    public static void main(String[] args){
        Name name = new Name("Yeeun", "Kim");
        String fn = name.getFirstName();
        String ini = name.getInitials();
        System.out.println(fn);
        System.out.println(ini);

        Competitor yeeun = new Competitor(100,23,name,"yk23","free","2,4,5,4,2",3.4);
        System.out.println(yeeun.getFullDetails());
        System.out.println(yeeun.getShortDetails());
    }
}