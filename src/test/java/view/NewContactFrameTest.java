package view;

import controller.ContactController;
import controller.GroupController;
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

class NewContactFrameTest {

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
    void startsWithEmptyFieldsAndAvailableGroupsLoaded() {
        groupController.addGroup(new Group("Family", "desc"));

        NewContactFrame frame = new NewContactFrame(contactController, groupController);

        JTextField firstNameField = SwingTestUtils.getField(frame, "firstNameField");
        JTextField lastNameField = SwingTestUtils.getField(frame, "lastNameField");
        JTextField cityField = SwingTestUtils.getField(frame, "cityField");
        assertEquals("", firstNameField.getText());
        assertEquals("", lastNameField.getText());
        assertEquals("", cityField.getText());

        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        // Index 0 is a "No Group" sentinel (null) the frame adds ahead of the real groups.
        assertEquals(2, groupsListModel.getSize());
        assertNull(groupsListModel.getElementAt(0));
        assertEquals("Family", groupsListModel.getElementAt(1).getName());
    }
}
