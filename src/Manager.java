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

    public void readFromFile(String filename){
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
        CompetitorController controller = new CompetitorController(new CompetitorList());
        Manager manager = new Manager(controller);
        manager.readFromFile("./Competitor.csv");
    }
}