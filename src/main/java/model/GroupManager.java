package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of groups in the contact management system.
 * This class implements Serializable to allow for persistent storage of the group collection.
 * Provides functionality for adding, updating, deleting, and retrieving groups.
 */
public class GroupManager implements Serializable {
	private static final long serialVersionUID = 1L;

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
        // Check if the new name conflicts with an existing group
        for (Group existingGroup : groups) {
            if (!existingGroup.equals(group) && existingGroup.getName().equalsIgnoreCase(group.getName())) {
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