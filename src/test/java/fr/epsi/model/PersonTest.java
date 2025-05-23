package fr.epsi.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    void testPersonConstructorAndGetters() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        Person person = new Person("Jean", "Dupont", "jean.dupont@example.com", birthDate, 38);

        assertEquals("Jean", person.getFirstName());
        assertEquals("Dupont", person.getLastName());
        assertEquals("jean.dupont@example.com", person.getEmail());
        assertEquals(birthDate, person.getBirthDate());
        assertEquals(38, person.getAge());
    }

    @Test
    void testPersonSetters() {
        Person person = new Person(null, null, null, null, null);

        person.setFirstName("Marie");
        person.setLastName("Martin");
        person.setEmail("marie.martin@example.com");
        LocalDate birthDate = LocalDate.of(1990, 7, 22);
        person.setBirthDate(birthDate);
        person.setAge(33);

        assertEquals("Marie", person.getFirstName());
        assertEquals("Martin", person.getLastName());
        assertEquals("marie.martin@example.com", person.getEmail());
        assertEquals(birthDate, person.getBirthDate());
        assertEquals(33, person.getAge());
    }

    @Test
    void testToString() {
        LocalDate birthDate = LocalDate.of(1985, 5, 15);
        Person person = new Person("Jean", "Dupont", "jean.dupont@example.com", birthDate, 38);

        String toString = person.toString();

        assertTrue(toString.contains("firstName='Jean'"));
        assertTrue(toString.contains("lastName='Dupont'"));
        assertTrue(toString.contains("email='jean.dupont@example.com'"));
        assertTrue(toString.contains("birthDate=" + birthDate));
        assertTrue(toString.contains("age=38"));
    }
}