package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.Group;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A frame for creating new groups in the Contact Management System.
 * This class provides a user interface for:
 * - Entering group details (name and description)
 * - Selecting contacts to add to the group
 * - Managing the list of available and selected contacts
 * 
 * The frame includes validation to ensure required fields are filled and
 * provides confirmation dialogs when closing with unsaved changes.
 */
public class NewGroupFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    /** Controller for managing group-related operations */
    private GroupController groupController;
    
    /** Controller for managing contact-related operations */
    private ContactController contactController;
    
    /** Text field for entering the group name */
    private JTextField nameField;
    
    /** Text field for entering the group description */
    private JTextField descriptionField;
    
    /** List displaying all available contacts that can be added to the group */
    private JList<Contact> allContactsList;
    
    /** Model for managing the list of available contacts */
    private DefaultListModel<Contact> allContactsListModel;
    
    /** List displaying contacts that have been selected for the group */
    private JList<Contact> selectedContactsList;
    
    /** Model for managing the list of selected contacts */
    private DefaultListModel<Contact> selectedContactsListModel;
    
    /** Button to add selected contacts to the group */
    private JButton addContactButton;
    
    /** Button to remove selected contacts from the group */
    private JButton removeContactButton;
    
    /** Button to save the new group */
    private JButton saveButton;
    
    /** Button to cancel group creation */
    private JButton cancelButton;

    /**
     * Constructs a new NewGroupFrame with the specified controllers.
     * Initializes the UI components and loads available contacts.
     *
     * @param groupController The controller for managing groups
     * @param contactController The controller for managing contacts
     */
    public NewGroupFrame(GroupController groupController, ContactController contactController) {
        this.groupController = groupController;
        this.contactController = contactController;
        initComponents();
        setupFrame();
        loadContacts();
    }

    /**
     * Initializes and arranges all UI components of the frame.
     * Creates the form layout, contact selection section, and action buttons.
     * Sets up the layout with three main sections:
     * - Form panel for group details
     * - Contact selection panel with available and selected contacts
     * - Action buttons panel
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel nameLabel = new JLabel("Group Name:");
        nameField = new JTextField();
        
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);

        // Create contacts selection panel
        JPanel contactsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        contactsPanel.setBorder(BorderFactory.createTitledBorder("Select Contacts"));
        
        // All contacts list
        JPanel allContactsPanel = new JPanel(new BorderLayout());
        allContactsPanel.setBorder(BorderFactory.createTitledBorder("Available Contacts"));
        allContactsListModel = new DefaultListModel<>();
        allContactsList = new JList<>(allContactsListModel);
        JScrollPane allContactsScrollPane = new JScrollPane(allContactsList);
        allContactsPanel.add(allContactsScrollPane);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(50, 5, 50, 5));
        addContactButton = new JButton(">");
        removeContactButton = new JButton("<");
        buttonsPanel.add(addContactButton);
        buttonsPanel.add(removeContactButton);
        
        // Selected contacts list
        JPanel selectedContactsPanel = new JPanel(new BorderLayout());
        selectedContactsPanel.setBorder(BorderFactory.createTitledBorder("Selected Contacts"));
        selectedContactsListModel = new DefaultListModel<>();
        selectedContactsList = new JList<>(selectedContactsListModel);
        JScrollPane selectedContactsScrollPane = new JScrollPane(selectedContactsList);
        selectedContactsPanel.add(selectedContactsScrollPane);
        
        contactsPanel.add(allContactsPanel);
        contactsPanel.add(buttonsPanel);
        contactsPanel.add(selectedContactsPanel);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Save Group");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components
        add(formPanel, BorderLayout.NORTH);
        add(contactsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addContactButton.addActionListener(e -> addSelectedContacts());
        removeContactButton.addActionListener(e -> removeSelectedContacts());
        saveButton.addActionListener(e -> saveGroup());
        cancelButton.addActionListener(e -> confirmClose());
    }

    /**
     * Configures the frame properties and window behavior.
     * Sets the title, size, and adds a window listener to handle closing events.
     * The frame is set to DO_NOTHING_ON_CLOSE to prevent accidental data loss.
     */
    private void setupFrame() {
        setTitle("New Group");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmClose();
            }
        });
    }

    /**
     * Loads all available contacts from the contact controller into the available contacts list.
     * This populates the initial list of contacts that can be added to the group.
     */
    private void loadContacts() {
        List<Contact> contacts = contactController.getAllContacts();
        for (Contact contact : contacts) {
            allContactsListModel.addElement(contact);
        }
    }

    /**
     * Adds selected contacts from the available list to the selected contacts list.
     * Moves contacts from the available list to the selected list when the add button is clicked.
     * Prevents duplicate contacts in the selected list.
     */
    private void addSelectedContacts() {
        List<Contact> selected = allContactsList.getSelectedValuesList();
        for (Contact contact : selected) {
            if (!selectedContactsListModel.contains(contact)) {
                selectedContactsListModel.addElement(contact);
                allContactsListModel.removeElement(contact);
            }
        }
    }

    /**
     * Removes selected contacts from the selected list back to the available list.
     * Moves contacts from the selected list to the available list when the remove button is clicked.
     * Prevents duplicate contacts in the available list.
     */
    private void removeSelectedContacts() {
        List<Contact> selected = selectedContactsList.getSelectedValuesList();
        for (Contact contact : selected) {
            if (!allContactsListModel.contains(contact)) {
                allContactsListModel.addElement(contact);
                selectedContactsListModel.removeElement(contact);
            }
        }
    }

    /**
     * Saves the new group with all entered information.
     * Performs validation checks for required fields.
     * Creates the group, adds selected contacts, and saves it to the controller.
     * Shows success message and clears the form upon successful save.
     */
    private void saveGroup() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        
        // Validate group name
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Group name is required.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Check for duplicate group names
            for (Group existingGroup : groupController.getAllGroups()) {
                if (existingGroup.getName().equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this,
                            "A group with this name already exists.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Create new group
            Group group = new Group(name, description);
            
            // Add selected contacts to group
            for (int i = 0; i < selectedContactsListModel.size(); i++) {
                Contact contact = selectedContactsListModel.get(i);
                group.addContact(contact);
            }
            
            // Add group to controller
            groupController.addGroup(group);
            
            // Only show success and close if everything worked
            JOptionPane.showMessageDialog(this, 
                    "Group saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            dispose();
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving group: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all input fields and selections in the form.
     * Resets both contact lists and reloads available contacts.
     * Called after successful group creation or when canceling.
     */
    private void clearForm() {
        nameField.setText("");
        descriptionField.setText("");
        
        // Reset contact lists
        allContactsListModel.clear();
        selectedContactsListModel.clear();
        loadContacts();
    }

    /**
     * Handles the window closing event.
     * If there are unsaved changes (non-empty fields or selected contacts),
     * shows a confirmation dialog.
     * Closes the frame if confirmed or if there are no changes.
     */
    private void confirmClose() {
        if (!nameField.getText().isEmpty() || 
            !descriptionField.getText().isEmpty() || 
            !selectedContactsListModel.isEmpty()) {
            
            int response = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to close? Any unsaved data will be lost.", 
                    "Confirm Close", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            
            if (response == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }
}