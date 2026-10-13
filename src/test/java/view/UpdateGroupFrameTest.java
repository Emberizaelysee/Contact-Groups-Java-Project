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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class UpdateGroupFrameTest {

    @TempDir
    Path tempDir;

    private GroupController groupController;
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        FileManager fileManager = new FileManager(tempDir);
        GroupManager groupManager = new GroupManager();
        groupController = new GroupController(groupManager, fileManager);
        contactController = new ContactController(new ContactManager(), groupManager, fileManager);
    }

    @Test
    void prefillsFieldsFromExistingGroup() {
        Contact jane = new Contact("Jane", "Doe", "Paris");
        Contact john = new Contact("John", "Smith", "Lyon");
        contactController.addContact(jane);
        contactController.addContact(john);
        Group family = new Group("Family", "Close relatives");
        family.addContact(jane);
        groupController.addGroup(family);

        UpdateGroupFrame frame = new UpdateGroupFrame(groupController, contactController, family);

        JTextField nameField = SwingTestUtils.getField(frame, "nameField");
        JTextArea descriptionArea = SwingTestUtils.getField(frame, "descriptionArea");
        assertEquals("Family", nameField.getText());
        assertEquals("Close relatives", descriptionArea.getText());

        DefaultListModel<Contact> selectedContactsModel = SwingTestUtils.getField(frame, "selectedContactsModel");
        assertEquals(1, selectedContactsModel.getSize());
        assertEquals("Jane", selectedContactsModel.getElementAt(0).getFirstName());

        DefaultListModel<Contact> availableContactsModel = SwingTestUtils.getField(frame, "availableContactsModel");
        assertEquals(1, availableContactsModel.getSize());
        assertEquals("John", availableContactsModel.getElementAt(0).getFirstName());
    }
}
