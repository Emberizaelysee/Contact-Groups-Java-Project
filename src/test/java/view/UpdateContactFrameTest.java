package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.ContactManager;
import model.Group;
import model.GroupManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Dialogs;
import util.FileManager;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UpdateContactFrameTest {

    @TempDir
    Path tempDir;

    private ContactController contactController;
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        FileManager fileManager = new FileManager(tempDir);
        GroupManager groupManager = new GroupManager();
        groupController = new GroupController(groupManager, fileManager);
        contactController = new ContactController(new ContactManager(), groupManager, fileManager);
    }

    @Test
    void prefillsFieldsFromExistingContact() {
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contact.addTelephone("06 12 34 56 78");
        contactController.addContact(contact);
        groupController.addGroup(new Group("Family", "desc"));

        UpdateContactFrame frame = new UpdateContactFrame(contactController, groupController, contact);

        JTextField firstNameField = SwingTestUtils.getField(frame, "firstNameField");
        JTextField lastNameField = SwingTestUtils.getField(frame, "lastNameField");
        JTextField cityField = SwingTestUtils.getField(frame, "cityField");
        assertEquals("Jane", firstNameField.getText());
        assertEquals("Doe", lastNameField.getText());
        assertEquals("Paris", cityField.getText());

        DefaultListModel<String> phonesListModel = SwingTestUtils.getField(frame, "phonesListModel");
        assertEquals(1, phonesListModel.getSize());
        assertEquals("06 12 34 56 78", phonesListModel.getElementAt(0));

        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        // Index 0 is a "No Group" sentinel (null) the frame adds ahead of the real groups.
        assertEquals(2, groupsListModel.getSize());
        assertNull(groupsListModel.getElementAt(0));
        assertEquals("Family", groupsListModel.getElementAt(1).getName());
    }
}
