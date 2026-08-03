package controller;

import model.Contact;
import model.ContactManager;
import model.GroupManager;
import util.FileManager;
import view.GroupsFrame;

import java.util.*;
import javax.swing.JOptionPane;

/**
 * Controller class responsible for managing contact-related operations.
 * Acts as an intermediary between the view layer and the model layer for contact management.
 * Handles operations such as adding, updating, deleting, searching, and sorting contacts.
 * 
 * This controller maintains the business logic for contact operations and ensures
 * proper synchronization between the contact data and the user interface.
 */
public class ContactController {
    /** The manager responsible for contact operations */
    private ContactManager contactManager;
    /** The manager responsible for group operations */
    private GroupManager groupManager;
    /** The manager responsible for file operations */
    private FileManager fileManager;
    /** Reference to the groups frame for updating contact lists */
    private GroupsFrame groupsFrame;

    /**
     * Constructs a new ContactController with the specified managers.
     * Initializes the controller with the necessary dependencies for contact and group management.
     *
     * @param contactManager The manager responsible for contact operations
     * @param groupManager The manager responsible for group operations
     * @param fileManager The manager responsible for file operations
     */
    public ContactController(ContactManager contactManager, GroupManager groupManager, FileManager fileManager) {
        this.contactManager = contactManager;
        this.groupManager = groupManager;
        this.fileManager = fileManager;
    }

    /**
     * Adds a new contact to the system.
     * This method delegates the addition to the contact manager and ensures
     * proper data persistence. Displays an error message if the operation fails.
     *
     * @param contact The contact to be added
     * @return true if the contact was added successfully, false otherwise
     */
    public boolean addContact(Contact contact) {
        try {
            if (contactManager.addContact(contact)) {
                fileManager.saveContacts(contactManager);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Adding Contact", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred while adding the contact.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Updates an existing contact's information.
     * This method ensures that all references to the contact are updated
     * throughout the system. Displays an error message if the operation fails.
     *
     * @param contact The contact to be updated
     * @return true if the contact was updated successfully, false otherwise
     */
    public boolean updateContact(Contact contact) {
        try {
            contactManager.updateContact(contact);
            fileManager.saveContacts(contactManager);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Updating Contact", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred while updating the contact.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Deletes a contact from the system.
     * This method ensures that the contact is properly removed from all
     * related data structures. Displays an error message if the operation fails.
     *
     * @param contact The contact to be deleted
     * @return true if the contact was deleted successfully, false otherwise
     */
    public boolean deleteContact(Contact contact) {
        try {
            if (contactManager.deleteContact(contact)) {
                // Remove contact from all groups
                for (model.Group group : groupManager.getAllGroups()) {
                    group.removeContact(contact);
                }
                fileManager.saveContacts(contactManager);
                fileManager.saveGroups(groupManager);
                // Notify the GroupsFrame to refresh the contact list
                if (groupsFrame != null) {
                    groupsFrame.refreshContactList();
                }
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error Deleting Contact", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An unexpected error occurred while deleting the contact.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Retrieves all contacts in the system.
     * Returns a new list to prevent external modification of the internal data.
     *
     * @return A list of all contacts
     */
    public List<Contact> getAllContacts() {
        return contactManager.getAllContacts();
    }

    /**
     * Searches for contacts based on the given criteria.
     * The search is case-insensitive and matches contacts whose first name or last name
     * starts with the search criteria.
     *
     * @param criteria The search string to match against contact names
     * @return A list of contacts matching the search criteria
     */
    public List<Contact> searchContacts(String criteria) {
        return contactManager.searchContacts(criteria);
    }

    /**
     * Sorts all contacts by their first name in ascending order.
     * Returns a new sorted list without modifying the original data.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortContactsByFirstName() {
        return contactManager.sortByFirstName();
    }

    /**
     * Sorts all contacts by their last name in ascending order.
     * Returns a new sorted list without modifying the original data.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortContactsByLastName() {
        return contactManager.sortByLastName();
    }

    /**
     * Sorts all contacts by their city in ascending order.
     * Returns a new sorted list without modifying the original data.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortContactsByCity() {
        return contactManager.sortByCity();
    }
}