import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Manager {

    private CompetitorController competitorController;
    private CompetitorList competitorList;
    private Scanner scanner;


    public Manager(CompetitorController controller, CompetitorList competitorList) {
        this.competitorController = controller;
        this.competitorList = competitorList;
        this.scanner = new Scanner(System.in);

    }

    public void readFromFile(String freestyleFilename, String butterflyFilename){
        readCompetitors(freestyleFilename);
        readCompetitors(butterflyFilename);
    }

    private void readCompetitors(String filename) {
        List<String[]> parsedData = new ArrayList<>();

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                String line = br.readLine();
                String[] data = line.split(",");
                parsedData.add(data);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        competitorController.processData(parsedData);
    }

    public void generateReport(String filePath) {
        Report report = new Report(this.competitorList);
        String reportContent = report.createReport();

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(reportContent);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    public void displayCompetitorDetails() {
        System.out.println("Enter competitor number: ");
        int competitorNumber = scanner.nextInt();

        Competitor competitor = competitorList.findCompetitor(competitorNumber);
        if (competitor != null) {
            System.out.println(competitor.getShortDetails());
        } else {
            System.out.println("No competitor found with number: " + competitorNumber);
        }
    }
//
//    public static void main(String[] args){
//        CompetitorList competitorList = new CompetitorList();
//        CompetitorController controller = new CompetitorController(competitorList);
//
//        Manager manager = new Manager(controller, competitorList);
//        manager.readFromFile("./FreestyleCompetitor.csv", "./ButterflyCompetitor.csv");
//        manager.generateReport("./report.txt");
//        manager.displayCompetitorDetails();
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    CompetitorList competitorList = new CompetitorList();
                    CompetitorController controller = new CompetitorController(competitorList);
                    Manager manager = new Manager(controller, competitorList);
                    manager.readFromFile("./FreestyleCompetitor.csv", "./ButterflyCompetitor.csv");
                    new CompetitorManagerGUI(manager, competitorList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}