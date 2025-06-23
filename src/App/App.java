package App;

import controller.MainController;

/**
 * Main application class that serves as the entry point for the Contact Management System.
 * This class initializes the application and creates the necessary components.
 */
public class App {
    /**
     * The main method that starts the application.
     * This method creates the main controller which handles all initialization
     * of managers, controllers, and views.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Start the application by creating the main controller
        new MainController();
    }
}