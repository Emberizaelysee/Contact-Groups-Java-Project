package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;
import model.Group;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * A frame for managing groups in the Contact Management System.
 * This class provides a user interface for:
 * - Viewing a list of all groups
 * - Adding new groups
 * - Updating existing groups
 * - Deleting groups
 * - Viewing contacts within a selected group
 */
public class GroupsFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private GroupController groupController;
    private ContactController contactController;
    private JList<Group> groupsList;
    private DefaultListModel<Group> groupsListModel;
    private JTable contactsTable;
    private DefaultTableModel contactsTableModel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    /**
     * Constructs a new GroupsFrame with the specified controllers.
     * Initializes the UI components and refreshes the groups list.
     *
     * @param groupController The controller for managing groups
     * @param contactController The controller for managing contacts
     */
    public GroupsFrame(GroupController groupController, ContactController contactController) {
        this.groupController = groupController;
        this.contactController = contactController;
        initComponents();
        setupFrame();
        refreshGroupsList();
    }

    /**
     * Initializes and arranges all UI components of the frame.
     * Creates the layout for displaying groups and contacts, and sets up action listeners.
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());

        // Create components
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel groupsLabel = new JLabel("Groups");
        groupsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        groupsListModel = new DefaultListModel<>();
        groupsList = new JList<>(groupsListModel);
        groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsList.setBackground(Color.WHITE);
        groupsList.setCellRenderer(new GroupListRenderer());
        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        leftPanel.add(groupsLabel, BorderLayout.NORTH);
        leftPanel.add(groupsScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel contactsLabel = new JLabel("Contacts in Group");
        contactsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create table model with columns
        String[] columnNames = {"First Name", "Last Name", "City"};
        contactsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        contactsTable = new JTable(contactsTableModel);
        contactsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsTable.getTableHeader().setReorderingAllowed(false);
        
        // Set table appearance
        contactsTable.setBackground(Color.WHITE);
        contactsTable.getTableHeader().setBackground(Color.WHITE);
        contactsTable.setGridColor(Color.LIGHT_GRAY);
    
        
        
        JScrollPane contactsScrollPane = new JScrollPane(contactsTable);
        rightPanel.add(contactsLabel, BorderLayout.NORTH);
        rightPanel.add(contactsScrollPane, BorderLayout.CENTER);

        JPanel splitPane = new JPanel(new GridLayout(1, 2));
        splitPane.add(leftPanel);
        splitPane.add(rightPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add new Group");
        updateButton = new JButton("Update Group");
        deleteButton = new JButton("Delete Group");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Add components
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> openNewGroupFrame());
        updateButton.addActionListener(e -> updateGroup());
        deleteButton.addActionListener(e -> deleteSelectedGroup());
        
        groupsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateContactsInGroup();
            }
        });
        
        contactsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openViewContactFrame();
                }
            }
        });
    }

    /**
     * Configures the frame properties and window behavior.
     * Sets the title, size, and default close operation.
     */
    private void setupFrame() {
        setTitle("Groups");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                refreshAllData();
            }
        });
    }

    /**
     * Refreshes the list of groups displayed in the UI.
     * Clears the current list and loads all groups from the controller.
     */
    public void refreshGroupsList() {
        groupsListModel.clear();
        List<Group> groups = groupController.getAllGroups();
        for (Group group : groups) {
            groupsListModel.addElement(group);
        }
        
        // Select the first group if available
        if (!groups.isEmpty()) {
            groupsList.setSelectedIndex(0);
            updateContactsInGroup();
        }
    }
    
    /**
     * Updates the list of contacts displayed for the selected group.
     * Clears the current list and loads contacts from the selected group.
     * This method is called whenever the selected group changes or when a contact is deleted.
     */
    public void updateContactsInGroup() {
        contactsTableModel.setRowCount(0);
        Group selectedGroup = groupsList.getSelectedValue();
        if (selectedGroup != null) {
            for (Contact contact : selectedGroup.getContacts()) {
                contactsTableModel.addRow(new Object[]{
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getCity()
                });
            }
        }
    }

    /**
     * Opens a new frame for creating a new group.
     * Refreshes the groups list when the new group frame is closed.
     */
    private void openNewGroupFrame() {
        NewGroupFrame newGroupFrame = new NewGroupFrame(groupController, contactController);
        newGroupFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshGroupsList();
            }
        });
        newGroupFrame.setVisible(true);
    }

    /**
     * Opens a new frame for updating the selected group.
     * Refreshes the groups list when the update group frame is closed.
     */
    private void updateGroup() {
        Group selectedGroup = groupsList.getSelectedValue();
        if (selectedGroup != null) {
            UpdateGroupFrame updateFrame = new UpdateGroupFrame(groupController, contactController, selectedGroup);
            updateFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a group to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Deletes the selected group after confirmation.
     * Refreshes the groups list and clears the contacts list upon successful deletion.
     */
    private void deleteSelectedGroup() {
        Group selectedGroup = groupsList.getSelectedValue();
        if (selectedGroup != null) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this group?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                groupController.deleteGroup(selectedGroup);
                refreshGroupsList();
                contactsTableModel.setRowCount(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a group to delete.", "No Group Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Opens a new frame for viewing the selected contact's details.
     */
    private void openViewContactFrame() {
        Contact selectedContact = getSelectedContact();
        if (selectedContact != null) {
            ViewContactFrame viewContactFrame = new ViewContactFrame(contactController, groupController, selectedContact);
            viewContactFrame.setVisible(true);
        }
    }

    /**
     * Refreshes the contact list in the selected group.
     * This method is called whenever a contact is deleted.
     */
    public void refreshContactList() {
        updateContactsInGroup();
    }

    /**
     * Refreshes all data in the GroupsFrame.
     * This method is called whenever the frame is activated to ensure that the displayed data is up-to-date.
     */
    public void refreshAllData() {
        refreshGroupsList();
        updateContactsInGroup();
    }

    private Contact getSelectedContact() {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        String firstName = (String) contactsTableModel.getValueAt(selectedRow, 0);
        String lastName = (String) contactsTableModel.getValueAt(selectedRow, 1);
        String city = (String) contactsTableModel.getValueAt(selectedRow, 2);
        
        // Find the contact in the selected group
        Group selectedGroup = groupsList.getSelectedValue();
        if (selectedGroup != null) {
            for (Contact contact : selectedGroup.getContacts()) {
                if (contact.getFirstName().equals(firstName) && 
                    contact.getLastName().equals(lastName) && 
                    contact.getCity().equals(city)) {
                    return contact;
                }
            }
        }
        return null;
    }

    /**
     * Custom renderer for the groups list to show group name, contact count, and description
     */
    private class GroupListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Group) {
                Group group = (Group) value;
                String text = String.format("%s (%d contacts) -- %s", 
                    group.getName(), 
                    group.getContacts().size(),
                    group.getDescription());
                setText(text);
            }
            return this;
        }
    }
}