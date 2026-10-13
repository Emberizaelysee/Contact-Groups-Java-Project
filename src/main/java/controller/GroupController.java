package controller;

import model.*;
import util.Dialogs;
import util.FileManager;

import java.util.List;

/**
 * Controller class responsible for managing group-related operations.
 * Acts as an intermediary between the view layer and the model layer for group management.
 * Handles operations such as adding, updating, and deleting groups, as well as managing
 * contacts within groups.
 */
public class GroupController {
    private GroupManager groupManager;
    private FileManager fileManager;

    /**
     * Constructs a new GroupController with the specified manager.
     * Initializes the controller with the necessary dependencies for group management.
     *
     * @param groupManager The manager responsible for group operations
     * @param fileManager The manager responsible for file operations
     */
    public GroupController(GroupManager groupManager, FileManager fileManager) {
        this.groupManager = groupManager;
        this.fileManager = fileManager;
    }

    /**
     * Adds a new group to the system.
     * This method delegates the addition to the group manager and ensures
     * proper data persistence. Displays an error message if the operation fails.
     *
     * @param group The group to be added
     * @return true if the group was added successfully, false otherwise
     */
    public boolean addGroup(Group group) {
        try {
            groupManager.addGroup(group);
            fileManager.saveGroups(groupManager);
            return true;
        } catch (IllegalArgumentException e) {
            Dialogs.error(e.getMessage(), "Error Adding Group");
            return false;
        } catch (Exception e) {
            Dialogs.error("An unexpected error occurred while adding the group.", "Error");
            return false;
        }
    }

    /**
     * Updates an existing group's information.
     * This method ensures that all references to the group are updated
     * throughout the system. Displays an error message if the operation fails.
     *
     * @param group The group to be updated
     * @return true if the group was updated successfully, false otherwise
     */
    public boolean updateGroup(Group group) {
        try {
            groupManager.updateGroup(group);
            fileManager.saveGroups(groupManager);
            return true;
        } catch (IllegalArgumentException e) {
            Dialogs.error(e.getMessage(), "Error Updating Group");
            return false;
        } catch (Exception e) {
            Dialogs.error("An unexpected error occurred while updating the group.", "Error");
            return false;
        }
    }

    /**
     * Deletes a group from the system.
     * This method ensures that the group is properly removed from all
     * related data structures. Displays an error message if the operation fails.
     *
     * @param group The group to be deleted
     * @return true if the group was deleted successfully, false otherwise
     */
    public boolean deleteGroup(Group group) {
        try {
            groupManager.deleteGroup(group);
            fileManager.saveGroups(groupManager);
            return true;
        } catch (IllegalArgumentException e) {
            Dialogs.error(e.getMessage(), "Error Deleting Group");
            return false;
        } catch (Exception e) {
            Dialogs.error("An unexpected error occurred while deleting the group.", "Error");
            return false;
        }
    }

    /**
     * Retrieves all groups in the system.
     * Returns a new list to prevent external modification of the internal data.
     *
     * @return A list of all groups
     */
    public List<Group> getAllGroups() {
        return groupManager.getAllGroups();
    }
}