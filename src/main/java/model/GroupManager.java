package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of groups in the contact management system.
 * Provides functionality for adding, updating, deleting, and retrieving groups.
 */
public class GroupManager {

    /** List of groups managed by this GroupManager */
    private List<Group> groups;

    /**
     * Constructs a new GroupManager with an empty list of groups.
     */
    public GroupManager() {
        groups = new ArrayList<>();
    }

    /**
     * Adds a new group to the collection.
     * Throws an IllegalArgumentException if a group with the same name already exists.
     *
     * @param group The group to be added
     * @throws IllegalArgumentException if a group with the same name already exists
     */
    public void addGroup(Group group) {
        // Check if a group with the same name already exists
        for (Group existingGroup : groups) {
            if (existingGroup.getName().equalsIgnoreCase(group.getName())) {
                throw new IllegalArgumentException("A group with the name '" + group.getName() + "' already exists.");
            }
        }
        groups.add(group);
    }

    /**
     * Updates an existing group in the collection.
     * If the group is found, it will be replaced with the updated version.
     * Groups are identified by their name for the purpose of updating.
     * Throws an IllegalArgumentException if the new name conflicts with an existing group.
     *
     * @param group The group to be updated
     * @throws IllegalArgumentException if the new name conflicts with an existing group
     */
    public void updateGroup(Group group) {
        // Compare by reference (not equals()) so that renaming a group in place doesn't let it
        // slip past its own conflict check once its name matches what we're comparing against.
        for (Group existingGroup : groups) {
            if (existingGroup != group && existingGroup.getName().equalsIgnoreCase(group.getName())) {
                throw new IllegalArgumentException("A group with the name '" + group.getName() + "' already exists.");
            }
        }
        int index = groups.indexOf(group);
        if (index != -1) {
            groups.set(index, group);
        }
    }

    /**
     * Removes a group from the collection.
     *
     * @param group The group to be removed
     */
    public void deleteGroup(Group group) {
        groups.remove(group);
    }

    /**
     * Retrieves all groups in the collection.
     * Returns a new ArrayList to prevent external modification of the internal list.
     *
     * @return A new list containing all groups
     */
    public List<Group> getAllGroups() {
        return new ArrayList<>(groups);
    }
}