package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.Group;

import javax.swing.*;
import java.awt.*;

import java.util.List;

/**
 * Frame for creating new contacts in the Contact Management System.
 * This frame provides a user interface for entering contact details including
 * name, city, phone numbers, and group assignments.
 */
public class NewContactFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    /** Controller for managing contact-related operations */
    private ContactController contactController;
    
    /** Controller for managing group-related operations */
    private GroupController groupController;
    
    /** Text field for entering the contact's first name */
    private JTextField firstNameField;
    
    /** Text field for entering the contact's last name */
    private JTextField lastNameField;
    
    /** Text field for entering the contact's city */
    private JTextField cityField;
    
    /** Text field for entering new phone numbers */
    private JTextField phoneField;
    
    /** List displaying all phone numbers for the contact */
    private JList<String> phonesList;
    
    /** Model for managing the list of phone numbers */
    private DefaultListModel<String> phonesListModel;
    
    /** Button to add a new phone number */
    private JButton addPhoneButton;
    
    /** Button to remove selected phone numbers */
    private JButton removePhoneButton;
    
    /** List displaying available groups for assignment */
    private JList<Group> groupsList;
    
    /** Model for managing the list of available groups */
    private DefaultListModel<Group> groupsListModel;
    
    /** Button to save the new contact */
    private JButton saveButton;
    
    /** Button to cancel contact creation */
    private JButton cancelButton;

    /**
     * Constructs a new NewContactFrame.
     * Initializes the UI components and sets up the frame.
     *
     * @param contactController The controller for contact operations
     * @param groupController The controller for group operations
     */
    public NewContactFrame(ContactController contactController, GroupController groupController) {
        this.contactController = contactController;
        this.groupController = groupController;
        initComponents();
        setupFrame();
        loadGroups();
    }

    /**
     * Initializes all UI components and sets up the layout.
     * Creates input fields, lists, and buttons for managing the contact.
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(cityLabel);
        formPanel.add(cityField);

        // Create phones panel
        JPanel phonesPanel = new JPanel(new BorderLayout());
        phonesPanel.setBorder(BorderFactory.createTitledBorder("Phone Numbers"));
        
        JPanel phoneInputPanel = new JPanel(new BorderLayout());
        phoneField = new JTextField();
        addPhoneButton = new JButton("Add");
        phoneInputPanel.add(phoneField, BorderLayout.CENTER);
        phoneInputPanel.add(addPhoneButton, BorderLayout.EAST);
        
        phonesListModel = new DefaultListModel<>();
        phonesList = new JList<>(phonesListModel);
        JScrollPane phonesScrollPane = new JScrollPane(phonesList);
        
        removePhoneButton = new JButton("Remove Selected");
        
        phonesPanel.add(phoneInputPanel, BorderLayout.NORTH);
        phonesPanel.add(phonesScrollPane, BorderLayout.CENTER);
        phonesPanel.add(removePhoneButton, BorderLayout.SOUTH);

        // Create groups panel
        JPanel groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.setBorder(BorderFactory.createTitledBorder("Groups"));
        groupsListModel = new DefaultListModel<>();
        groupsList = new JList<>(groupsListModel);
        groupsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        groupsList.setCellRenderer(new GroupListRenderer());
        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        groupsPanel.add(groupsScrollPane);

        // Create split panel
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        splitPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        splitPanel.add(phonesPanel);
        splitPanel.add(groupsPanel);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("SAVE");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components
        add(formPanel, BorderLayout.NORTH);
        add(splitPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addPhoneButton.addActionListener(e -> addPhoneNumber());
        removePhoneButton.addActionListener(e -> removePhoneNumber());
        saveButton.addActionListener(e -> saveContact());
        cancelButton.addActionListener(e -> confirmClose());
    }

    /**
     * Sets up the frame properties and window listener.
     * Configures the frame size, title, and close operation.
     */
    private void setupFrame() {
        setTitle("New Contact");
        setSize(600, 500);
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
     * Loads all available groups into the groups list.
     * Adds a "No group" option at the beginning of the list.
     */
    private void loadGroups() {
        // Add "No Group" option at the beginning
        groupsListModel.addElement(null);
        
        // Add all available groups
        List<Group> groups = groupController.getAllGroups();
        for (Group group : groups) {
            groupsListModel.addElement(group);
        }
    }

    /**
     * Custom renderer for the groups list.
     * Displays "No group" for null values and group names for Group objects.
     */
    private static class GroupListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                setText("No group");
            } else if (value instanceof Group) {
                setText(value.toString());
            }
            return this;
        }
    }

    /**
     * Adds a new phone number to the contact.
     * Validates that the phone number contains only digits and is at least 8 digits long.
     */
    private void addPhoneNumber() {
        String phone = phoneField.getText().trim();
        
        // Validate that the phone number contains only digits
        if (!phone.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "Phone number must contain only digits (0-9).",
                    "Invalid Phone Number",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate minimum length
        if (phone.length() < 8) {
            JOptionPane.showMessageDialog(this,
                    "Phone number must be at least 8 digits long.",
                    "Invalid Phone Number",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.isEmpty()) {
            phonesListModel.addElement(phone);
            phoneField.setText("");
        }
    }

    /**
     * Removes the selected phone number from the contact.
     * Removes all selected phone numbers if multiple are selected.
     */
    private void removePhoneNumber() {
        int selectedIndex = phonesList.getSelectedIndex();
        if (selectedIndex != -1) {
            phonesListModel.remove(selectedIndex);
        }
    }

    /**
     * Saves the new contact with all entered information.
     * Validates required fields and phone numbers before saving.
     * Adds the contact to selected groups if any are chosen.
     */
    private void saveContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String city = cityField.getText().trim();
        
        // Validate required fields
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "First name and last name are required.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate at least one phone number
        if (phonesListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "At least one phone number is required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create new contact
            Contact contact = new Contact(firstName, lastName, city);
            
            // Add phone numbers
            for (int i = 0; i < phonesListModel.size(); i++) {
                contact.addTelephone(phonesListModel.get(i));
            }
            
            // Add contact to controller
            contactController.addContact(contact);
            
            // Add contact to selected groups
            List<Group> selectedGroups = groupsList.getSelectedValuesList();
            for (Group group : selectedGroups) {
                if (group != null) { // Skip the "No group" option
                    group.addContact(contact);
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                    "Contact saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving contact: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears all input fields and resets the form to its initial state.
     * Removes all phone numbers and group selections.
     */
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        cityField.setText("");
        phoneField.setText("");
        phonesListModel.clear();
        groupsList.clearSelection();
    }

    /**
     * Handles the window closing event.
     * If there are unsaved changes, shows a confirmation dialog.
     * Closes the frame if confirmed or if there are no changes.
     */
    private void confirmClose() {
        if (!firstNameField.getText().isEmpty() || 
            !lastNameField.getText().isEmpty() || 
            !cityField.getText().isEmpty() || 
            !phonesListModel.isEmpty()) {
            
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