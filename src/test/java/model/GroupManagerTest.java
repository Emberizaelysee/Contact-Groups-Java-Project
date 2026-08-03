package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupManagerTest {

    private GroupManager manager;

    @BeforeEach
    void setUp() {
        manager = new GroupManager();
    }

    @Test
    void addGroupRejectsDuplicateNameCaseInsensitively() {
        manager.addGroup(new Group("Family", "desc"));

        assertThrows(IllegalArgumentException.class, () -> manager.addGroup(new Group("FAMILY", "other")));
    }

    @Test
    void updateGroupRejectsRenameToExistingName() {
        Group family = new Group("Family", "desc");
        Group friends = new Group("Friends", "desc");
        manager.addGroup(family);
        manager.addGroup(friends);

        friends.setName("Family");

        assertThrows(IllegalArgumentException.class, () -> manager.updateGroup(friends));
    }

    @Test
    void updateGroupReplacesEntryWithSameName() {
        Group family = new Group("Family", "desc");
        manager.addGroup(family);
        family.setDescription("Updated description");

        manager.updateGroup(family);

        assertEquals("Updated description", manager.getAllGroups().get(0).getDescription());
    }

    @Test
    void deleteGroupRemovesMatchingEntry() {
        Group family = new Group("Family", "desc");
        manager.addGroup(family);

        manager.deleteGroup(family);

        assertTrue(manager.getAllGroups().isEmpty());
    }

    @Test
    void getAllGroupsReturnsDefensiveCopy() {
        manager.addGroup(new Group("Family", "desc"));

        manager.getAllGroups().clear();

        assertEquals(1, manager.getAllGroups().size());
    }
}
