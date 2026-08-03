package controller;

import model.ContactManager;
import model.GroupManager;
import util.FileManager;
import view.MainFrame;

/**
 * Main controller class that coordinates between different controllers and views.
 * This class is responsible for initializing the application and managing the
 * relationships between different components.
 */
public class MainController {
    /** Manages the collection of contacts */
    private ContactManager contactManager;
    /** Manages the collection of contact groups */
    private GroupManager groupManager;
    /** Handles file operations for saving and loading data */
    private FileManager fileManager;
    /** Controller responsible for contact-related operations */
    private ContactController contactController;
    /** Controller responsible for group-related operations */
    private GroupController groupController;
    private MainFrame mainFrame;

    /**
     * Constructs a new MainController and initializes all necessary components.
     * This includes creating the managers, controllers, and the main view.
     */
    public MainController() {
        // Initialize file manager
        fileManager = new FileManager();

        // Load data from files
        contactManager = fileManager.loadContacts();
        groupManager = fileManager.loadGroups();

        // Initialize controllers
        groupController = new GroupController(groupManager, fileManager);
        contactController = new ContactController(contactManager, groupManager, fileManager);

        // Initialize main view
        mainFrame = new MainFrame(this);
        mainFrame.setVisible(true);
    }

    /**
     * Gets the contact controller instance.
     *
     * @return The contact controller
     */
    public ContactController getContactController() {
        return contactController;
    }

    /**
     * Gets the group controller instance.
     *
     * @return The group controller
     */
    public GroupController getGroupController() {
        return groupController;
    }

    /**
     * Saves the current state of contacts and groups to persistent storage.
     * This method delegates the saving operation to the FileManager.
     * It ensures that all data is properly persisted before the application closes.
     */
    public void saveData() {
        fileManager.saveContacts(contactManager);
        fileManager.saveGroups(groupManager);
    }
}