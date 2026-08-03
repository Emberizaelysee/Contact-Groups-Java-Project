package view;

import controller.MainController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main window of the Contact Management System.
 * This class represents the primary interface that users interact with to access
 * the contact and group management features. It provides buttons to navigate to
 * the contacts and groups management windows.
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/** The main controller that coordinates the application's operations */
	private MainController mainController;
	
	/** Button to access the contacts management window */
	private JButton contactsButton;
	
	/** Button to access the groups management window */
	private JButton groupsButton;

	/**
	 * Constructs a new MainFrame with the specified controller.
	 * Initializes the UI components and sets up the window properties.
	 *
	 * @param mainController The controller that manages the application's operations
	 */
	public MainFrame(MainController mainController) {
		this.mainController = mainController;
		initComponents();
		setupFrame();
	}

	/**
	 * Initializes and arranges all UI components of the main window.
	 * Sets up the layout, creates buttons, and adds event listeners.
	 */
	private void initComponents() {
		// Set layout
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Create components
		JLabel titleLabel = new JLabel("Contact Management System");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		contactsButton = new JButton("Contacts");
		groupsButton = new JButton("Groups");

		// Add components
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		add(contactsButton, gbc);

		gbc.gridx = 1;
		add(groupsButton, gbc);

		// Add action listeners
		contactsButton.addActionListener(e -> openContactsFrame());
		groupsButton.addActionListener(e -> openGroupsFrame());

		// Add window listener to save data when closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainController.saveData();
			}
		});
	}

	/**
	 * Configures the main window properties.
	 * Sets the title, size, default close operation, and window position.
	 */
	private void setupFrame() {
		setTitle("Contact Management System");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/**
	 * Opens the contacts management window.
	 * Creates and displays a new ContactsFrame with the necessary controllers.
	 */
	private void openContactsFrame() {
		ContactsFrame contactsFrame = new ContactsFrame(
				mainController.getContactController(), 
				mainController.getGroupController());
		contactsFrame.setVisible(true);
	}

	/**
	 * Opens the groups management window.
	 * Creates and displays a new GroupsFrame with the necessary controllers.
	 */
	private void openGroupsFrame() {
		GroupsFrame groupsFrame = new GroupsFrame(
				mainController.getGroupController(), 
				mainController.getContactController());
		groupsFrame.setVisible(true);
	}
}