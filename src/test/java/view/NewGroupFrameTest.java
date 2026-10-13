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

import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NewGroupFrameTest {

    @TempDir
    Path tempDir;

    private GroupController groupController;
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        FileManager fileManager = new FileManager(tempDir);
        groupController = new GroupController(new GroupManager(), fileManager);
        contactController = new ContactController(new ContactManager(), new GroupManager(), fileManager);
    }

    @Test
    void startsWithEmptyFieldsAndAllContactsAvailable() {
        contactController.addContact(new Contact("Jane", "Doe", "Paris"));

        NewGroupFrame frame = new NewGroupFrame(groupController, contactController);

        JTextField nameField = SwingTestUtils.getField(frame, "nameField");
        JTextField descriptionField = SwingTestUtils.getField(frame, "descriptionField");
        assertEquals("", nameField.getText());
        assertEquals("", descriptionField.getText());

        DefaultListModel<Contact> allContactsListModel = SwingTestUtils.getField(frame, "allContactsListModel");
        assertEquals(1, allContactsListModel.getSize());

        DefaultListModel<Contact> selectedContactsListModel = SwingTestUtils.getField(frame, "selectedContactsListModel");
        assertEquals(0, selectedContactsListModel.getSize());
    }
}
