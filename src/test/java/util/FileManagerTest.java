package util;

import model.Contact;
import model.ContactManager;
import model.Group;
import model.GroupManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @TempDir
    Path tempDir;

    private FileManager fileManager;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        fileManager = new FileManager(tempDir);
    }

    @Test
    void saveAndLoadContactsRoundTrips() {
        ContactManager contactManager = new ContactManager();
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contact.addTelephone("06 12 34 56 78");
        contactManager.addContact(contact);

        fileManager.saveContacts(contactManager);
        ContactManager reloaded = fileManager.loadContacts();

        assertEquals(1, reloaded.getAllContacts().size());
        Contact reloadedContact = reloaded.getAllContacts().get(0);
        assertEquals("Jane", reloadedContact.getFirstName());
        assertEquals("Doe", reloadedContact.getLastName());
        assertEquals("Paris", reloadedContact.getCity());
        assertEquals(1, reloadedContact.getTelephones().size());
    }

    @Test
    void saveAndLoadGroupsRoundTrips() {
        GroupManager groupManager = new GroupManager();
        Group group = new Group("Family", "Close relatives");
        group.addContact(new Contact("Jane", "Doe", "Paris"));
        groupManager.addGroup(group);

        fileManager.saveGroups(groupManager);
        GroupManager reloaded = fileManager.loadGroups();

        assertEquals(1, reloaded.getAllGroups().size());
        Group reloadedGroup = reloaded.getAllGroups().get(0);
        assertEquals("Family", reloadedGroup.getName());
        assertEquals(1, reloadedGroup.getContacts().size());
    }

    @Test
    void savesContactsAsPlainJsonFile() throws IOException {
        ContactManager contactManager = new ContactManager();
        contactManager.addContact(new Contact("Jane", "Doe", "Paris"));

        fileManager.saveContacts(contactManager);

        Path contactsFile = tempDir.resolve("contacts.json");
        assertTrue(Files.exists(contactsFile));
        String json = Files.readString(contactsFile, StandardCharsets.UTF_8);
        assertTrue(json.contains("\"firstName\""));
        assertTrue(json.contains("Jane"));
    }

    @Test
    void loadContactsReturnsEmptyManagerWhenFileMissing() {
        ContactManager result = fileManager.loadContacts();

        assertNotNull(result);
        assertTrue(result.getAllContacts().isEmpty());
    }

    @Test
    void loadGroupsReturnsEmptyManagerWhenFileMissing() {
        GroupManager result = fileManager.loadGroups();

        assertNotNull(result);
        assertTrue(result.getAllGroups().isEmpty());
    }

    @Test
    void loadContactsReturnsEmptyManagerWhenFileIsCorrupt() throws IOException {
        Path contactsFile = tempDir.resolve("contacts.json");
        Files.writeString(contactsFile, "{ this is not valid json", StandardCharsets.UTF_8);

        ContactManager result = fileManager.loadContacts();

        assertNotNull(result);
        assertTrue(result.getAllContacts().isEmpty());
    }

    @Test
    void loadGroupsReturnsEmptyManagerWhenFileIsCorrupt() throws IOException {
        Path groupsFile = tempDir.resolve("groups.json");
        Files.writeString(groupsFile, "not json at all", StandardCharsets.UTF_8);

        GroupManager result = fileManager.loadGroups();

        assertNotNull(result);
        assertTrue(result.getAllGroups().isEmpty());
    }
}
