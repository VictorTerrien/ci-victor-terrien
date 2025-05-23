package fr.epsi.service;

import fr.epsi.model.Person;
import fr.epsi.validator.PersonValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessingService {
    private final PersonValidator validator;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DataProcessingService() {
        this.validator = new PersonValidator();
    }

    public Map<String, Object> processDataFile(String filePath) {
        List<Person> validPersons = new ArrayList<>();
        Map<Person, List<String>> invalidPersons = new HashMap<>();
        int totalCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                totalCount++;

                try {
                    String[] data = line.split(",");
                    if (data.length < 5) {
                        Person incompletePerson = new Person(
                                data.length > 0 ? data[0] : null,
                                data.length > 1 ? data[1] : null,
                                data.length > 2 ? data[2] : null,
                                null, null
                        );
                        List<String> errors = new ArrayList<>();
                        errors.add("Données incomplètes");
                        invalidPersons.put(incompletePerson, errors);
                        continue;
                    }

                    LocalDate birthDate = null;
                    Integer age = null;

                    try {
                        birthDate = LocalDate.parse(data[3], DATE_FORMATTER);
                    } catch (DateTimeParseException e) {
                    }

                    try {
                        age = Integer.parseInt(data[4]);
                    } catch (NumberFormatException e) {
                    }

                    Person person = new Person(data[0], data[1], data[2], birthDate, age);

                    List<String> validationErrors = validator.validate(person);
                    if (validationErrors.isEmpty()) {
                        validPersons.add(person);
                    } else {
                        invalidPersons.put(person, validationErrors);
                    }
                } catch (Exception e) {
                    System.err.println("Erreur lors du traitement de la ligne: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("validPersons", validPersons);
        result.put("invalidPersons", invalidPersons);
        result.put("totalCount", totalCount);
        result.put("validCount", validPersons.size());
        result.put("invalidCount", invalidPersons.size());

        return result;
    }
}