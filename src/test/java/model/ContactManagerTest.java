package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactManagerTest {

    private ContactManager manager;

    @BeforeEach
    void setUp() {
        manager = new ContactManager();
    }

    @Test
    void addContactRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.addContact(null));
    }

    @Test
    void addContactAddsToCollection() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        manager.addContact(contact);

        assertEquals(List.of(contact), manager.getAllContacts());
    }

    @Test
    void updateContactThrowsWhenContactDoesNotExist() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        assertThrows(IllegalArgumentException.class, () -> manager.updateContact(contact));
    }

    @Test
    void updateContactReplacesExistingEntry() {
        Contact original = new Contact("Jane", "Doe", "Paris");
        manager.addContact(original);
        original.setCity("London");

        manager.updateContact(original);

        assertEquals("London", manager.getAllContacts().get(0).getCity());
    }

    @Test
    void deleteContactReturnsFalseWhenNotFound() {
        assertFalse(manager.deleteContact(new Contact("Jane", "Doe", "Paris")));
    }

    @Test
    void searchContactsMatchesFirstOrLastNamePrefixCaseInsensitively() {
        Contact jane = new Contact("Jane", "Doe", "Paris");
        Contact john = new Contact("John", "Smith", "London");
        manager.addContact(jane);
        manager.addContact(john);

        List<Contact> results = manager.searchContacts("ja");

        assertEquals(List.of(jane), results);
    }

    @Test
    void sortByFirstNameOrdersAscending() {
        Contact john = new Contact("John", "Smith", "London");
        Contact jane = new Contact("Jane", "Doe", "Paris");
        manager.addContact(john);
        manager.addContact(jane);

        assertEquals(List.of(jane, john), manager.sortByFirstName());
    }

    @Test
    void getAllContactsReturnsDefensiveCopy() {
        manager.addContact(new Contact("Jane", "Doe", "Paris"));

        manager.getAllContacts().clear();

        assertEquals(1, manager.getAllContacts().size());
    }
}
