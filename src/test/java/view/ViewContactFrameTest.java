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
import javax.swing.JLabel;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ViewContactFrameTest {

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
    void displaysContactNameCityPhonesAndGroups() {
        Contact contact = new Contact("Jane", "Doe", "Paris");
        contact.addTelephone("06 12 34 56 78");
        contactController.addContact(contact);
        Group family = new Group("Family", "desc");
        family.addContact(contact);
        groupController.addGroup(family);

        ViewContactFrame frame = new ViewContactFrame(contactController, groupController, contact);

        JLabel nameLabel = SwingTestUtils.getField(frame, "nameLabel");
        JLabel cityLabel = SwingTestUtils.getField(frame, "cityLabel");
        assertEquals("Jane Doe", nameLabel.getText());
        assertEquals("Paris", cityLabel.getText());

        DefaultListModel<String> phonesListModel = SwingTestUtils.getField(frame, "phonesListModel");
        assertEquals(1, phonesListModel.getSize());
        assertEquals("06 12 34 56 78", phonesListModel.getElementAt(0));

        DefaultListModel<Group> groupsListModel = SwingTestUtils.getField(frame, "groupsListModel");
        assertEquals(1, groupsListModel.getSize());
        assertEquals("Family", groupsListModel.getElementAt(0).getName());
    }
}
