package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.Group;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A frame for viewing contact details in the Contact Management System.
 * This class provides a user interface for displaying:
 * - Basic contact information (name and city)
 * - Phone numbers associated with the contact
 * - Groups that the contact belongs to
 */
public class ViewContactFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private Contact contact;
    private GroupController groupController;

    private JLabel nameLabel;
    private JLabel cityLabel;
    private JList<String> phonesList;
    private DefaultListModel<String> phonesListModel;
    private JList<Group> groupsList;
    private DefaultListModel<Group> groupsListModel;

    /**
     * Constructs a new ViewContactFrame with the specified controllers and contact.
     * Initializes the UI components and displays the contact's information.
     *
     * @param contactController The controller for managing contacts
     * @param groupController The controller for managing groups
     * @param contact The contact to be viewed
     */
    public ViewContactFrame(ContactController contactController, GroupController groupController, Contact contact) {
        
        this.groupController = groupController;
        this.contact = contact;
        initComponents();
        setupFrame();
        displayContactInfo();
    }

    /**
     * Initializes and arranges all UI components of the frame.
     * Creates the layout for displaying contact information, phone numbers, and groups.
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());

        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameTitleLabel = new JLabel("Name:");
        nameLabel = new JLabel();
        namePanel.add(nameTitleLabel);
        namePanel.add(nameLabel);
        
        JPanel cityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel cityTitleLabel = new JLabel("City:");
        cityLabel = new JLabel();
        cityPanel.add(cityTitleLabel);
        cityPanel.add(cityLabel);
        
        infoPanel.add(namePanel);
        infoPanel.add(cityPanel);

        // Create phones panel
        JPanel phonesPanel = new JPanel(new BorderLayout());
        phonesPanel.setBorder(BorderFactory.createTitledBorder("Phone Numbers"));
        phonesListModel = new DefaultListModel<>();
        phonesList = new JList<>(phonesListModel);
        JScrollPane phonesScrollPane = new JScrollPane(phonesList);
        phonesPanel.add(phonesScrollPane);

        // Create groups panel
        JPanel groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.setBorder(BorderFactory.createTitledBorder("Groups"));
        groupsListModel = new DefaultListModel<>();
        groupsList = new JList<>(groupsListModel);
        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        groupsPanel.add(groupsScrollPane);

        // Create split panel for phones and groups
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        splitPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        splitPanel.add(phonesPanel);
        splitPanel.add(groupsPanel);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Add components
        add(infoPanel, BorderLayout.NORTH);
        add(splitPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configures the frame properties and window behavior.
     * Sets the title, size, and default close operation.
     */
    private void setupFrame() {
        setTitle("View Contact");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Displays the contact's information in the UI components.
     * Populates the name, city, phone numbers, and groups lists.
     */
    private void displayContactInfo() {
        nameLabel.setText(contact.getFirstName() + " " + contact.getLastName());
        cityLabel.setText(contact.getCity());
        
        // Display phone numbers
        phonesListModel.clear();
        for (String phone : contact.getTelephones()) {
            phonesListModel.addElement(phone);
        }
        
        // Find groups that contain this contact
        List<Group> allGroups = groupController.getAllGroups();

        for (Group group : allGroups) {
            if (group.getContacts().contains(contact)) {
                groupsListModel.addElement(group);
            }
        }
    }
}