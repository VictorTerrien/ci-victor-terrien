package fr.epsi.service;

import fr.epsi.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    private ReportService reportService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        reportService = new ReportService();
    }

    @Test
    void testGenerateReport() throws IOException {
        // Préparer les données de test
        Map<String, Object> testData = new HashMap<>();

        // Personnes valides
        List<Person> validPersons = new ArrayList<>();
        validPersons.add(new Person("Jean", "Dupont", "jean.dupont@example.com",
                LocalDate.of(1985, 5, 15), 38));
        validPersons.add(new Person("Marie", "Martin", "marie.martin@example.com",
                LocalDate.of(1990, 7, 22), 33));

        // Personnes invalides avec leurs erreurs
        Map<Person, List<String>> invalidPersons = new HashMap<>();

        Person invalidPerson1 = new Person("Pierre", "Bernard", "pierre.bernard",
                LocalDate.of(1977, 12, 10), 46);
        List<String> errors1 = List.of("Format d'email invalide");
        invalidPersons.put(invalidPerson1, errors1);

        Person invalidPerson2 = new Person("Luc", "Petit", "luc.petit@example.com",
                LocalDate.of(2000, 8, 5), -5);
        List<String> errors2 = List.of("L'âge ne peut pas être négatif");
        invalidPersons.put(invalidPerson2, errors2);

        // Construire le résultat complet
        testData.put("validPersons", validPersons);
        testData.put("invalidPersons", invalidPersons);
        testData.put("totalCount", 4);
        testData.put("validCount", 2);
        testData.put("invalidCount", 2);

        // Générer le rapport
        String reportPath = tempDir.resolve("test-report.txt").toString();
        reportService.generateReport(testData, reportPath);

        // Vérifier le contenu du rapport
        try (BufferedReader reader = new BufferedReader(new FileReader(reportPath))) {
            String content = reader.lines().reduce("", (a, b) -> a + b + "\n");

            // Vérifier les informations essentielles
            assertTrue(content.contains("=== RAPPORT DE VALIDATION ==="));
            assertTrue(content.contains("Total des entrées: 4"));
            assertTrue(content.contains("Entrées valides: 2"));
            assertTrue(content.contains("Entrées invalides: 2"));
            assertTrue(content.contains("Taux de validité: 50%"));

            // Vérifier que les entrées valides sont présentes
            assertTrue(content.contains("Jean"));
            assertTrue(content.contains("Marie"));

            // Vérifier que les erreurs sont listées
            assertTrue(content.contains("Format d'email invalide"));
            assertTrue(content.contains("L'âge ne peut pas être négatif"));
        }
    }

    @Test
    void testGenerateEmptyReport() throws IOException {
        // Préparer des données vides
        Map<String, Object> emptyData = new HashMap<>();
        emptyData.put("validPersons", new ArrayList<Person>());
        emptyData.put("invalidPersons", new HashMap<Person, List<String>>());
        emptyData.put("totalCount", 0);
        emptyData.put("validCount", 0);
        emptyData.put("invalidCount", 0);

        // Générer le rapport
        String reportPath = tempDir.resolve("empty-report.txt").toString();
        reportService.generateReport(emptyData, reportPath);

        // Vérifier le contenu
        try (BufferedReader reader = new BufferedReader(new FileReader(reportPath))) {
            String content = reader.lines().reduce("", (a, b) -> a + b + "\n");

            assertTrue(content.contains("Total des entrées: 0"));
            assertTrue(content.contains("Taux de validité: 0%"));
            assertTrue(content.contains("Aucune entrée invalide."));
            assertTrue(content.contains("Aucune entrée valide."));
        }
    }
}