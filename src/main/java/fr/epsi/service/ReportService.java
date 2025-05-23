package fr.epsi.service;

import fr.epsi.model.Person;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ReportService {

    public void generateReport(Map<String, Object> processResult, String outputPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("=== RAPPORT DE VALIDATION ===");
            writer.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println();

            int totalCount = (int) processResult.get("totalCount");
            int validCount = (int) processResult.get("validCount");
            int invalidCount = (int) processResult.get("invalidCount");

            writer.println("=== RÉSUMÉ ===");
            writer.println("Total des entrées: " + totalCount);
            writer.println("Entrées valides: " + validCount);
            writer.println("Entrées invalides: " + invalidCount);
            writer.println("Taux de validité: " + (totalCount > 0 ? (validCount * 100 / totalCount) : 0) + "%");
            writer.println();

            writer.println("=== ENTRÉES INVALIDES ===");
            @SuppressWarnings("unchecked")
            Map<Person, List<String>> invalidPersons = (Map<Person, List<String>>) processResult.get("invalidPersons");

            if (invalidPersons.isEmpty()) {
                writer.println("Aucune entrée invalide.");
            } else {
                invalidPersons.forEach((person, errors) -> {
                    writer.println("Entrée: " + person);
                    writer.println("Erreurs:");
                    errors.forEach(error -> writer.println("  - " + error));
                    writer.println();
                });
            }

            writer.println("=== ENTRÉES VALIDES ===");
            @SuppressWarnings("unchecked")
            List<Person> validPersons = (List<Person>) processResult.get("validPersons");

            if (validPersons.isEmpty()) {
                writer.println("Aucune entrée valide.");
            } else {
                validPersons.forEach(person -> writer.println(person));
            }

            System.out.println("Rapport généré avec succès: " + outputPath);

        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du rapport: " + e.getMessage());
        }
    }
}