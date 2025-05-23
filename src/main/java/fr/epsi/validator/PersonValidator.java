package fr.epsi.validator;

import fr.epsi.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PersonValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public List<String> validate(Person person) {
        List<String> errors = new ArrayList<>();

        if (person.getFirstName() == null || person.getFirstName().trim().isEmpty()) {
            errors.add("Le prénom est obligatoire");
        }
        if (person.getLastName() == null || person.getLastName().trim().isEmpty()) {
            errors.add("Le nom est obligatoire");
        }

        if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
            errors.add("L'email est obligatoire");
        } else if (!EMAIL_PATTERN.matcher(person.getEmail()).matches()) {
            errors.add("Format d'email invalide");
        }

        if (person.getBirthDate() == null) {
            errors.add("La date de naissance est obligatoire");
        } else {
            if (person.getBirthDate().isAfter(LocalDate.now())) {
                errors.add("La date de naissance ne peut pas être dans le futur");
            }

            int calculatedAge = Period.between(person.getBirthDate(), LocalDate.now()).getYears();

            if (person.getAge() == null) {
                errors.add("L'âge est obligatoire");
            } else if (person.getAge() < 0) {
                errors.add("L'âge ne peut pas être négatif");
            } else if (calculatedAge != person.getAge()) {
                errors.add("L'âge ne correspond pas à la date de naissance");
            }
        }

        return errors;
    }
}