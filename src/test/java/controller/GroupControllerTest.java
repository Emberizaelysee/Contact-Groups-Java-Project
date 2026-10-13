package controller;

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

class GroupControllerTest {

    @TempDir
    Path tempDir;

    private GroupManager groupManager;
    private FileManager fileManager;
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        Dialogs.setEnabled(false);
        groupManager = new GroupManager();
        fileManager = new FileManager(tempDir);
        groupController = new GroupController(groupManager, fileManager);
    }

    @Test
    void addGroupAddsToManagerAndPersists() {
        Group group = new Group("Family", "Close relatives");

        boolean result = groupController.addGroup(group);

        assertTrue(result);
        assertEquals(List.of(group), groupController.getAllGroups());
        assertEquals(List.of(group), fileManager.loadGroups().getAllGroups());
    }

    @Test
    void addGroupReturnsFalseOnDuplicateName() {
        groupController.addGroup(new Group("Family", "desc"));

        boolean result = groupController.addGroup(new Group("FAMILY", "other"));

        assertFalse(result);
        assertEquals(1, groupController.getAllGroups().size());
    }

    @Test
    void updateGroupPersistsChanges() {
        Group group = new Group("Family", "desc");
        groupController.addGroup(group);
        group.setDescription("Updated");

        boolean result = groupController.updateGroup(group);

        assertTrue(result);
        assertEquals("Updated", fileManager.loadGroups().getAllGroups().get(0).getDescription());
    }

    @Test
    void updateGroupReturnsFalseOnNameConflict() {
        Group family = new Group("Family", "desc");
        Group friends = new Group("Friends", "desc");
        groupController.addGroup(family);
        groupController.addGroup(friends);
        friends.setName("Family");

        boolean result = groupController.updateGroup(friends);

        assertFalse(result);
    }

    @Test
    void deleteGroupRemovesFromManagerAndPersists() {
        Group group = new Group("Family", "desc");
        groupController.addGroup(group);

        boolean result = groupController.deleteGroup(group);

        assertTrue(result);
        assertTrue(groupController.getAllGroups().isEmpty());
        assertTrue(fileManager.loadGroups().getAllGroups().isEmpty());
    }

    @Test
    void getAllGroupsReturnsDefensiveCopy() {
        groupController.addGroup(new Group("Family", "desc"));

        groupController.getAllGroups().clear();

        assertEquals(1, groupController.getAllGroups().size());
    }
}
