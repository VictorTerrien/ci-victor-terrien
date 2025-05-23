package fr.epsi.validator;

import fr.epsi.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonValidatorTest {

    private PersonValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PersonValidator();
    }

    @Test
    void testValidPerson() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();

        Person person = new Person(
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                birthDate,
                calculatedAge
        );

        List<String> errors = validator.validate(person);

        assertTrue(errors.isEmpty(), "Une personne valide ne devrait pas générer d'erreurs");
    }

    @Test
    void testMissingFirstName() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();

        Person person = new Person(
                null,
                "Dupont",
                "jean.dupont@example.com",
                birthDate,
                calculatedAge
        );

        List<String> errors = validator.validate(person);

        assertEquals(1, errors.size());
        assertTrue(errors.contains("Le prénom est obligatoire"));
    }

    @Test
    void testEmptyLastName() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();

        Person person = new Person(
                "Jean",
                "   ",
                "jean.dupont@example.com",
                birthDate,
                calculatedAge
        );

        List<String> errors = validator.validate(person);

        assertEquals(1, errors.size());
        assertTrue(errors.contains("Le nom est obligatoire"));
    }

    @Test
    void testInvalidEmail() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        int calculatedAge = Period.between(birthDate, LocalDate.now()).getYears();

        Person person = new Person(
                "Jean",
                "Dupont",
                "jean.dupontexample.com",
                birthDate,
                calculatedAge
        );

        List<String> errors = validator.validate(person);

        assertEquals(1, errors.size());
        assertTrue(errors.contains("Format d'email invalide"));
    }

    @Test
    void testFutureBirthDate() {
        Person person = new Person(
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                LocalDate.now().plusDays(1),
                0
        );

        List<String> errors = validator.validate(person);

        assertTrue(errors.contains("La date de naissance ne peut pas être dans le futur"));
    }

    @Test
    void testNegativeAge() {
        Person person = new Person(
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                LocalDate.of(1985, 5, 15),
                -5
        );

        List<String> errors = validator.validate(person);

        assertTrue(errors.contains("L'âge ne peut pas être négatif"));
    }

    @Test
    void testInconsistentAge() {
        Person person = new Person(
                "Jean",
                "Dupont",
                "jean.dupont@example.com",
                LocalDate.of(1985, 5, 15),
                25
        );

        List<String> errors = validator.validate(person);

        assertTrue(errors.contains("L'âge ne correspond pas à la date de naissance"));
    }

    @Test
    void testMultipleErrors() {
        Person person = new Person(
                "",
                null,
                "invalidemail",
                LocalDate.now().plusYears(1),
                -10
        );

        List<String> errors = validator.validate(person);

        assertEquals(5, errors.size());
    }
}