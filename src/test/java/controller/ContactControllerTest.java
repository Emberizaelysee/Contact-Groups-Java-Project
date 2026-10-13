package controller;

import model.Contact;
import model.ContactManager;
import model.Group;
import model.GroupManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Dialogs;
import util.FileManager;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactControllerTest {

    @TempDir
    Path tempDir;

    private ContactManager contactManager;
    private GroupManager groupManager;
    private FileManager fileManager;
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        contactManager = new ContactManager();
        groupManager = new GroupManager();
        fileManager = new FileManager(tempDir);
        contactController = new ContactController(contactManager, groupManager, fileManager);
    }

    @Test
    void addContactAddsToManagerAndPersists() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        boolean result = contactController.addContact(contact);

        assertTrue(result);
        assertEquals(List.of(contact), contactController.getAllContacts());
        assertEquals(List.of(contact), fileManager.loadContacts().getAllContacts());
    }

    @Test
    void addContactReturnsFalseAndDoesNotPersistOnNull() {
        boolean result = contactController.addContact(null);

        assertFalse(result);
        assertTrue(fileManager.loadContacts().getAllContacts().isEmpty());
    }

    @Test
    void updateContactPersistsChanges() {
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contactController.addContact(contact);
        contact.setCity("Lyon");

        boolean result = contactController.updateContact(contact);

        assertTrue(result);
        assertEquals("Lyon", fileManager.loadContacts().getAllContacts().get(0).getCity());
    }

    @Test
    void updateContactReturnsFalseWhenContactDoesNotExist() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        boolean result = contactController.updateContact(contact);

        assertFalse(result);
    }

    @Test
    void deleteContactRemovesContactAndCascadesToGroups() {
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contactController.addContact(contact);
        Group group = new Group("Family", "desc");
        group.addContact(contact);
        groupManager.addGroup(group);

        boolean result = contactController.deleteContact(contact);

        assertTrue(result);
        assertTrue(contactController.getAllContacts().isEmpty());
        assertTrue(groupManager.getAllGroups().get(0).getContacts().isEmpty());
        assertTrue(fileManager.loadGroups().getAllGroups().get(0).getContacts().isEmpty());
    }

    @Test
    void deleteContactReturnsFalseWhenNotFound() {
        Contact contact = new Contact("Jane", "Doe", "Paris");

        boolean result = contactController.deleteContact(contact);

        assertFalse(result);
    }

    @Test
    void searchContactsDelegatesToManager() {
        contactController.addContact(new Contact("Jane", "Doe", "Paris"));
        contactController.addContact(new Contact("John", "Smith", "Lyon"));

        List<Contact> results = contactController.searchContacts("ja");

        assertEquals(1, results.size());
        assertEquals("Jane", results.get(0).getFirstName());
    }

    @Test
    void sortContactsByFirstNameDelegatesToManager() {
        contactController.addContact(new Contact("Zed", "Last", "City"));
        contactController.addContact(new Contact("Amy", "First", "City"));

        List<Contact> sorted = contactController.sortContactsByFirstName();

        assertEquals("Amy", sorted.get(0).getFirstName());
        assertEquals("Zed", sorted.get(1).getFirstName());
    }
}
