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
import javax.swing.table.DefaultTableModel;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GroupsFrameTest {

    @TempDir
    Path tempDir;

    private GroupManager groupManager;
    private GroupController groupController;
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        FileManager fileManager = new FileManager(tempDir);
        groupManager = new GroupManager();
        groupController = new GroupController(groupManager, fileManager);
        contactController = new ContactController(new ContactManager(), groupManager, fileManager);
    }

    @Test
    void buildsWithEmptyListsWhenNoGroups() {
        GroupsFrame frame = new GroupsFrame(groupController, contactController);

        assertEquals("Groups", frame.getTitle());
        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        assertEquals(0, groupsListModel.getSize());
    }

    @Test
    void populatesGroupsListAndSelectsFirstGroupOnConstruction() {
        Group family = new Group("Family", "Close relatives");
        family.addContact(new Contact("Jane", "Doe", "Paris"));
        groupController.addGroup(family);
        groupController.addGroup(new Group("Friends", "desc"));

        GroupsFrame frame = new GroupsFrame(groupController, contactController);

        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        assertEquals(2, groupsListModel.getSize());

        DefaultTableModel contactsTableModel = SwingTestUtils.getField(frame, "contactsTableModel");
        assertEquals(1, contactsTableModel.getRowCount());
        assertEquals("Jane", contactsTableModel.getValueAt(0, 0));
    }

    @Test
    void refreshGroupsListReflectsNewlyAddedGroup() {
        GroupsFrame frame = new GroupsFrame(groupController, contactController);
        groupController.addGroup(new Group("Family", "desc"));

        frame.refreshGroupsList();

        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        assertEquals(1, groupsListModel.getSize());
        assertEquals("Family", groupsListModel.getElementAt(0).getName());
    }
}
