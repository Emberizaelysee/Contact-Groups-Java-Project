package util;

import model.ContactManager;
import model.GroupManager;

import java.io.*;
import javax.swing.JOptionPane;

/**
 * Manages the persistence of contacts and groups data using file serialization.
 * This class handles saving and loading of ContactManager and GroupManager objects
 * to and from files.
 */
public class FileManager {
    /** File name for storing contacts data */
    private static final String CONTACTS_FILE = "contacts.dat";
    
    /** File name for storing groups data */
    private static final String GROUPS_FILE = "groups.dat";

    /**
     * Constructs a new FileManager.
     * This constructor is always public to allow instantiation from other packages.
     */
    public FileManager() {
        // Initialize file paths or any other necessary setup
    }

    /**
     * Saves the contacts data to a file using object serialization.
     * If an error occurs during saving, it will be displayed to the user.
     *
     * @param contactManager The ContactManager object to be saved
     */
    public void saveContacts(ContactManager contactManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONTACTS_FILE))) {
            oos.writeObject(contactManager);
        } catch (IOException e) {
            String message = "Error saving contacts: " + e.getMessage();
            System.err.println(message);
            JOptionPane.showMessageDialog(null, message, "Error Saving Contacts", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the groups data to a file using object serialization.
     * If an error occurs during saving, it will be displayed to the user.
     *
     * @param groupManager The GroupManager object to be saved
     */
    public void saveGroups(GroupManager groupManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GROUPS_FILE))) {
            oos.writeObject(groupManager);
        } catch (IOException e) {
            String message = "Error saving groups: " + e.getMessage();
            System.err.println(message);
            JOptionPane.showMessageDialog(null, message, "Error Saving Groups", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads contacts data from the file.
     * If the file doesn't exist or an error occurs during loading,
     * a new empty ContactManager will be created and the user will be notified.
     *
     * @return The loaded ContactManager or a new empty one if loading fails
     */
    public ContactManager loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONTACTS_FILE))) {
            return (ContactManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            String message = "Could not load contacts file. Creating new contacts file.";
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Loading Contacts", JOptionPane.INFORMATION_MESSAGE);
            return new ContactManager();
        }
    }

    /**
     * Loads groups data from the file.
     * If the file doesn't exist or an error occurs during loading,
     * a new empty GroupManager will be created and the user will be notified.
     *
     * @return The loaded GroupManager or a new empty one if loading fails
     */
    public GroupManager loadGroups() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GROUPS_FILE))) {
            return (GroupManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            String message = "Could not load groups file. Creating new groups file.";
            System.out.println(message);
            JOptionPane.showMessageDialog(null, message, "Loading Groups", JOptionPane.INFORMATION_MESSAGE);
            return new GroupManager();
        }
    }
}