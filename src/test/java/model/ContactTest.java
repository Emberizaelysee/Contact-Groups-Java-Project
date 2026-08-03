package model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void constructorTrimsNamesAndDefaultsCityToEmpty() {
        Contact contact = new Contact("  Jane  ", "  Doe  ", null);

        assertEquals("Jane", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertEquals("", contact.getCity());
        assertTrue(contact.getTelephones().isEmpty());
    }

    @Test
    void constructorRejectsBlankFirstName() {
        assertThrows(IllegalArgumentException.class, () -> new Contact(" ", "Doe", "Paris"));
    }

    @Test
    void constructorRejectsBlankLastName() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("Jane", "", "Paris"));
    }

    @Test
    void addTelephoneAcceptsValidNumberAndTrimsIt() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        contact.addTelephone(" 0612345678 ");

        assertEquals(List.of("0612345678"), contact.getTelephones());
    }

    @Test
    void addTelephoneRejectsNumberWithFewerThanEightDigits() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        assertThrows(IllegalArgumentException.class, () -> contact.addTelephone("123"));
    }

    @Test
    void addTelephoneRejectsBlankNumber() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        assertThrows(IllegalArgumentException.class, () -> contact.addTelephone("   "));
    }

    @Test
    void removeTelephoneReturnsFalseWhenNumberNotPresent() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        assertFalse(contact.removeTelephone("0612345678"));
    }

    @Test
    void getTelephonesReturnsDefensiveCopy() {
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contact.addTelephone("0612345678");

        contact.getTelephones().clear();

        assertEquals(1, contact.getTelephones().size());
    }

    @Test
    void equalsAndHashCodeAreBasedOnFirstAndLastNameOnly() {
        Contact a = new Contact("Jane", "Doe", "Paris");
        Contact b = new Contact("Jane", "Doe", "London");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentLastName() {
        Contact a = new Contact("Jane", "Doe", "Paris");
        Contact b = new Contact("Jane", "Smith", "Paris");

        assertNotEquals(a, b);
    }
}
