package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    @Test
    void constructorRejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Group("  ", "desc"));
    }

    @Test
    void constructorDefaultsNullDescriptionToEmpty() {
        Group group = new Group("Family", null);

        assertEquals("", group.getDescription());
    }

    @Test
    void addContactIgnoresDuplicates() {
        Group group = new Group("Family", "Close relatives");
        Contact contact = new Contact("Jane", "Doe", "Paris");

        group.addContact(contact);
        group.addContact(contact);

        assertEquals(1, group.getContacts().size());
    }

    @Test
    void addContactRejectsNull() {
        Group group = new Group("Family", "Close relatives");

        assertThrows(IllegalArgumentException.class, () -> group.addContact(null));
    }

    @Test
    void removeContactReturnsFalseWhenNotPresent() {
        Group group = new Group("Family", "Close relatives");
        Contact contact = new Contact("Jane", "Doe", "Paris");

        assertFalse(group.removeContact(contact));
    }

    @Test
    void toStringIncludesNameAndContactCount() {
        Group group = new Group("Family", "Close relatives");
        group.addContact(new Contact("Jane", "Doe", "Paris"));

        assertEquals("Family (1 contacts)", group.toString());
    }

    @Test
    void equalsAndHashCodeAreBasedOnNameOnly() {
        Group a = new Group("Family", "Close relatives");
        Group b = new Group("Family", "Different description");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
