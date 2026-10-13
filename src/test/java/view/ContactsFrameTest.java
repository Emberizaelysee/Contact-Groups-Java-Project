package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.ContactManager;
import model.GroupManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Dialogs;
import util.FileManager;

import javax.swing.table.DefaultTableModel;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ContactsFrameTest {

    @TempDir
    Path tempDir;

    private ContactController contactController;
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        FileManager fileManager = new FileManager(tempDir);
        groupController = new GroupController(new GroupManager(), fileManager);
        contactController = new ContactController(new ContactManager(), new GroupManager(), fileManager);
    }

    @Test
    void buildsWithEmptyTableWhenNoContacts() {
        ContactsFrame frame = new ContactsFrame(contactController, groupController);

        assertEquals("Contacts", frame.getTitle());
        DefaultTableModel model = SwingTestUtils.getField(frame, "contactsTableModel");
        assertEquals(0, model.getRowCount());
    }

    @Test
    void populatesTableFromControllerOnConstruction() {
        contactController.addContact(new Contact("Jane", "Doe", "Paris"));
        contactController.addContact(new Contact("John", "Smith", "Lyon"));

        ContactsFrame frame = new ContactsFrame(contactController, groupController);

        DefaultTableModel model = SwingTestUtils.getField(frame, "contactsTableModel");
        assertEquals(2, model.getRowCount());
    }

    @Test
    void refreshAllDataReflectsNewlyAddedContact() {
        ContactsFrame frame = new ContactsFrame(contactController, groupController);
        contactController.addContact(new Contact("Jane", "Doe", "Paris"));

        frame.refreshAllData();

        DefaultTableModel model = SwingTestUtils.getField(frame, "contactsTableModel");
        assertEquals(1, model.getRowCount());
        assertEquals("Jane", model.getValueAt(0, 0));
        assertEquals("Doe", model.getValueAt(0, 1));
        assertEquals("Paris", model.getValueAt(0, 2));
    }
}
