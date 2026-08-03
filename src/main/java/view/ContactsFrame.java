package view;

import controller.ContactController;
import controller.GroupController;
import model.Contact;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Comparator;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

/**
 * A frame for managing contacts in the Contact Management System.
 * This class provides a user interface for:
 * - Viewing a list of all contacts
 * - Searching for contacts
 * - Sorting contacts by clicking column headers
 * - Adding new contacts
 * - Updating existing contacts
 * - Deleting contacts
 * - Viewing contact details
 */
public class ContactsFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private ContactController contactController;
    private GroupController groupController;
    private JTable contactsTable;
    private DefaultTableModel contactsTableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton viewButton;
    private GroupsFrame groupsFrame;

    /**
     * Constructs a new ContactsFrame with the specified controllers.
     * Initializes the UI components and refreshes the contacts list.
     *
     * @param contactController The controller for managing contacts
     * @param groupController The controller for managing groups
     */
    public ContactsFrame(ContactController contactController, GroupController groupController) {
        this.contactController = contactController;
        this.groupController = groupController;
        initComponents();
        setupFrame();
        refreshContactsList();
    }

    /**
     * Initializes and arranges all UI components of the frame.
     * Creates the layout for displaying contacts, search and sort options, and action buttons.
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());

        // Create components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

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
        
        // Set white background for table and header
        contactsTable.setBackground(Color.WHITE);
        contactsTable.getTableHeader().setBackground(Color.WHITE);
        contactsTable.setGridColor(Color.LIGHT_GRAY);
        
        // Set up sorting
        sorter = new TableRowSorter<>(contactsTableModel);
        contactsTable.setRowSorter(sorter);
        
        // Set column comparators
        sorter.setComparator(0, Comparator.naturalOrder()); // First Name
        sorter.setComparator(1, Comparator.naturalOrder()); // Last Name
        sorter.setComparator(2, Comparator.naturalOrder()); // City
        
        // Set default sort to first name column in ascending order
        sorter.setSortKeys(java.util.Arrays.asList(
            new RowSorter.SortKey(0, SortOrder.ASCENDING)
        ));
        
        // Add double-click listener to open view contact frame
        contactsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openViewContactFrame();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(contactsTable);
        
        // Add margins to the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // top, left, bottom, right
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add new Contact");
        updateButton = new JButton("Update Contact");
        deleteButton = new JButton("Delete Contact");
        viewButton = new JButton("View Contact");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> openNewContactFrame());
        updateButton.addActionListener(e -> openUpdateContactFrame());
        deleteButton.addActionListener(e -> deleteSelectedContact());
        viewButton.addActionListener(e -> openViewContactFrame());
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchContacts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchContacts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchContacts();
            }
        });
    }

    /**
     * Configures the frame properties and window behavior.
     * Sets the title, size, and default close operation.
     */
    private void setupFrame() {
        setTitle("Contacts");
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
     * Refreshes the list of contacts displayed in the UI.
     * Clears the current list and loads contacts from the controller based on the selected sort option.
     */
    private void refreshContactsList() {
        contactsTableModel.setRowCount(0);
        List<Contact> contacts = contactController.getAllContacts();
        for (Contact contact : contacts) {
            contactsTableModel.addRow(new Object[]{
                contact.getFirstName(),
                contact.getLastName(),
                contact.getCity()
            });
        }
    }
    
    /**
     * Searches for contacts based on the entered criteria.
     * Updates the contacts list with the search results.
     */
    private void searchContacts() {
        String searchCriteria = searchField.getText().toLowerCase();
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                for (int i = 0; i < entry.getModel().getColumnCount(); i++) {
                    String value = entry.getStringValue(i).toLowerCase();
                    if (value.contains(searchCriteria)) {
                        return true;
                    }
                }
                return false;
            }
        };
        sorter.setRowFilter(filter);
    }

    /**
     * Opens a new frame for creating a new contact.
     * Refreshes the contacts list when the new contact frame is closed.
     */
    private void openNewContactFrame() {
        NewContactFrame newContactFrame = new NewContactFrame(contactController, groupController);
        newContactFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshContactsList();
            }
        });
        newContactFrame.setVisible(true);
    }

    /**
     * Opens a new frame for updating the selected contact.
     * Refreshes the contacts list when the update contact frame is closed.
     */
    private void openUpdateContactFrame() {
        Contact selectedContact = getSelectedContact();
        if (selectedContact != null) {
            UpdateContactFrame updateContactFrame = new UpdateContactFrame(contactController, groupController, selectedContact);
            updateContactFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    refreshContactsList();
                }
            });
            updateContactFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to update.", "No Contact Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Sets the reference to the GroupsFrame to update the contacts list when a contact is deleted.
     *
     * @param groupsFrame The GroupsFrame to be updated
     */
    public void setGroupsFrame(GroupsFrame groupsFrame) {
        this.groupsFrame = groupsFrame;
    }

    /**
     * Deletes the selected contact after confirmation.
     * Refreshes the contacts list upon successful deletion.
     * Updates the contacts list in the group if a contact is deleted.
     */
    private void deleteSelectedContact() {
        Contact selectedContact = getSelectedContact();
        if (selectedContact != null) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this contact?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                contactController.deleteContact(selectedContact);
                refreshContactsList();
                // Update the contacts list in the group
                if (groupsFrame != null) {
                    groupsFrame.updateContactsInGroup();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.", "No Contact Selected", JOptionPane.WARNING_MESSAGE);
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
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to view.", "No Contact Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Refreshes all data in the ContactsFrame.
     * This method is called whenever the frame is activated to ensure that the displayed data is up-to-date.
     */
    public void refreshAllData() {
        refreshContactsList();
    }

    private Contact getSelectedContact() {
        int selectedRow = contactsTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        
        // Convert the view index to model index to handle sorting
        int modelRow = contactsTable.convertRowIndexToModel(selectedRow);
        
        String firstName = (String) contactsTableModel.getValueAt(modelRow, 0);
        String lastName = (String) contactsTableModel.getValueAt(modelRow, 1);
        String city = (String) contactsTableModel.getValueAt(modelRow, 2);
        
        // Find the contact in the controller's list
        for (Contact contact : contactController.getAllContacts()) {
            if (contact.getFirstName().equals(firstName) && 
                contact.getLastName().equals(lastName) && 
                contact.getCity().equals(city)) {
                return contact;
            }
        }
        return null;
    }
}