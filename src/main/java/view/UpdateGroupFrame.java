package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.Group;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Frame for updating existing groups in the Contact Management System.
 * This frame allows users to modify group details and manage contacts within the group.
 */
public class UpdateGroupFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private GroupController groupController;
    private ContactController contactController;
    private Group group;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JList<Contact> availableContactsList;
    private JList<Contact> selectedContactsList;
    private DefaultListModel<Contact> availableContactsModel;
    private DefaultListModel<Contact> selectedContactsModel;
    private boolean hasUnsavedChanges = false;

    /**
     * Constructs a new UpdateGroupFrame.
     * Initializes the UI components and loads the group data.
     *
     * @param groupController The controller for group operations
     * @param contactController The controller for contact operations
     * @param group The group to be updated
     */
    public UpdateGroupFrame(GroupController groupController, ContactController contactController, Group group) {
        this.groupController = groupController;
        this.contactController = contactController;
        this.group = group;
        
        initializeComponents();
        setupFrame();
        loadGroupData();
    }

    /**
     * Initializes all UI components.
     * Sets up the input fields, lists, and buttons for managing the group.
     */
    private void initializeComponents() {
        // Initialize components
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        availableContactsModel = new DefaultListModel<>();
        selectedContactsModel = new DefaultListModel<>();
        availableContactsList = new JList<>(availableContactsModel);
        selectedContactsList = new JList<>(selectedContactsModel);
        
        // Add scroll panes for the lists
        JScrollPane availableScrollPane = new JScrollPane(availableContactsList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedContactsList);
        
        // Create buttons
        JButton addButton = new JButton("Add >");
        JButton removeButton = new JButton("< Remove");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        // Add action listeners
        addButton.addActionListener(e -> addSelectedContact());
        removeButton.addActionListener(e -> removeSelectedContact());
        saveButton.addActionListener(e -> saveGroup());
        cancelButton.addActionListener(e -> confirmClose());
        
        // Add document listeners for change tracking
        nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
        });
        
        descriptionArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { hasUnsavedChanges = true; }
        });
        
        // Layout
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Contacts panel
        JPanel contactsPanel = new JPanel(new BorderLayout());
        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        listsPanel.add(availableScrollPane);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        listsPanel.add(buttonPanel);
        
        listsPanel.add(selectedScrollPane);
        contactsPanel.add(listsPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        
        // Add panels to frame
        add(formPanel, BorderLayout.NORTH);
        add(contactsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets up the frame properties and window listener.
     */
    private void setupFrame() {
        setTitle("Update Group");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmClose();
            }
        });
    }

    /**
     * Loads the group data into the form fields and contact lists.
     */
    private void loadGroupData() {
        nameField.setText(group.getName());
        descriptionArea.setText(group.getDescription());
        
        // Load all contacts
        List<Contact> allContacts = contactController.getAllContacts();
        List<Contact> groupContacts = group.getContacts();
        
        // Add contacts to appropriate lists
        for (Contact contact : allContacts) {
            if (groupContacts.contains(contact)) {
                selectedContactsModel.addElement(contact);
            } else {
                availableContactsModel.addElement(contact);
            }
        }
    }

    /**
     * Adds all selected contacts from the available list to the selected list.
     */
    private void addSelectedContact() {
        List<Contact> selectedContacts = availableContactsList.getSelectedValuesList();
        if (!selectedContacts.isEmpty()) {
            for (Contact contact : selectedContacts) {
                availableContactsModel.removeElement(contact);
                selectedContactsModel.addElement(contact);
            }
            hasUnsavedChanges = true;
        }
    }

    /**
     * Removes all selected contacts from the selected list and adds them back to the available list.
     */
    private void removeSelectedContact() {
        List<Contact> selectedContacts = selectedContactsList.getSelectedValuesList();
        if (!selectedContacts.isEmpty()) {
            for (Contact contact : selectedContacts) {
                selectedContactsModel.removeElement(contact);
                availableContactsModel.addElement(contact);
            }
            hasUnsavedChanges = true;
        }
    }

    /**
     * Saves the updated group information.
     * Updates the group with the new data and closes the frame if successful.
     */
    private void saveGroup() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Update group
        group.setName(name);
        group.setDescription(description);
        
        // Update contacts
        group.clearContacts();
        for (int i = 0; i < selectedContactsModel.size(); i++) {
            group.addContact(selectedContactsModel.getElementAt(i));
        }
        
        // Save changes
        if (groupController.updateGroup(group)) {
            hasUnsavedChanges = false;
            dispose();
        }
    }

    /**
     * Confirms closing the frame if there are unsaved changes.
     * Closes the frame if confirmed or if there are no unsaved changes.
     */
    private void confirmClose() {
        if (hasUnsavedChanges) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Do you want to save before closing?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION
            );
            
            if (result == JOptionPane.YES_OPTION) {
                saveGroup();
            } else if (result == JOptionPane.NO_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }
}