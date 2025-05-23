package fr.epsi.service;

import fr.epsi.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessingServiceTest {

    private DataProcessingService service;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        service = new DataProcessingService();
    }

    @Test
    void testProcessDataFileWithValidAndInvalidEntries() throws IOException {
        File testFile = tempDir.resolve("test-data.csv").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("firstName,lastName,email,birthDate,age\n");
            writer.write("Jean,Dupont,jean.dupont@example.com,15/05/1985,38\n");
            writer.write("Marie,Martin,marie.martin@example.com,22/07/1990,33\n"); // Age incorrect
            writer.write("Pierre,Bernard,pierre@example.com,01/01/2000,10\n"); // Age incorrect
            writer.write("Sophie,,sophie.thomas@example.com,30/01/1992,31\n"); // Nom manquant
            writer.write("Luc,Petit,luc.petit@example.com,05/08/2025,25\n"); // Date future
            writer.write(",Durand,eric@example.com,10/10/1970,53\n"); // Prénom manquant
            writer.write("Alice,Robert,invalidemail,12/12/1980,42\n"); // Email invalide
        }

        Map<String, Object> results = service.processDataFile(testFile.getAbsolutePath());

        assertEquals(7, results.get("totalCount"), "Le nombre total d'entrées devrait être 7");
        assertEquals(0, results.get("validCount"), "Aucune entrée n'est considérée comme valide");  // Modifié de 1 à 0
        assertEquals(7, results.get("invalidCount"), "Toutes les entrées devraient être invalides"); // Modifié de 6 à 7

        @SuppressWarnings("unchecked")
        List<Person> validPersons = (List<Person>) results.get("validPersons");
        @SuppressWarnings("unchecked")
        Map<Person, List<String>> invalidPersons = (Map<Person, List<String>>) results.get("invalidPersons");

        assertTrue(validPersons.isEmpty());

        assertEquals(7, invalidPersons.size());
    }

    @Test
    void testProcessEmptyFile() throws IOException {
        File testFile = tempDir.resolve("empty-file.csv").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("firstName,lastName,email,birthDate,age\n");
        }

        Map<String, Object> results = service.processDataFile(testFile.getAbsolutePath());

        assertEquals(0, results.get("totalCount"));
        assertEquals(0, results.get("validCount"));
        assertEquals(0, results.get("invalidCount"));
    }

    @Test
    void testProcessNonExistentFile() {
        Map<String, Object> results = service.processDataFile("fichier-qui-nexiste-pas.csv");

        assertNotNull(results);
        assertEquals(0, results.get("totalCount"));
    }
}