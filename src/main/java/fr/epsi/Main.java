package fr.epsi;

import fr.epsi.service.DataProcessingService;
import fr.epsi.service.ReportService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Validateur de Données Personnelles ===");

        String inputFilePath;
        Path inputPath;
        do {
            System.out.print("Entrez le chemin du fichier à valider: ");
            inputFilePath = scanner.nextLine().trim();
            inputPath = Paths.get(inputFilePath);

            if (!Files.exists(inputPath)) {
                System.out.println("Le fichier n'existe pas. Veuillez réessayer.");
            }
        } while (!Files.exists(inputPath));

        System.out.print("Entrez le chemin pour le rapport de validation (laissez vide pour 'rapport.txt'): ");
        String outputFilePath = scanner.nextLine().trim();
        if (outputFilePath.isEmpty()) {
            outputFilePath = "rapport.txt";
        }

        DataProcessingService processingService = new DataProcessingService();
        Map<String, Object> results = processingService.processDataFile(inputFilePath);

        ReportService reportService = new ReportService();
        reportService.generateReport(results, outputFilePath);

        System.out.println("\nTraitement terminé !");
        System.out.println("Total des entrées: " + results.get("totalCount"));
        System.out.println("Entrées valides: " + results.get("validCount"));
        System.out.println("Entrées invalides: " + results.get("invalidCount"));
        System.out.println("Rapport généré: " + outputFilePath);

        scanner.close();
    }
}