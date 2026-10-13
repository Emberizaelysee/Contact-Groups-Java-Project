package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.ContactManager;
import model.GroupManager;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages the persistence of contacts and groups data as JSON files.
 * This class handles saving and loading of ContactManager and GroupManager objects
 * to and from files.
 */
public class FileManager {
    /** File name for storing contacts data */
    private static final String CONTACTS_FILE = "contacts.json";

    /** File name for storing groups data */
    private static final String GROUPS_FILE = "groups.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path contactsFile;
    private final Path groupsFile;

    /**
     * Constructs a new FileManager that persists to the current working directory.
     */
    public FileManager() {
        this(Path.of(""));
    }

    /**
     * Constructs a new FileManager that persists contacts and groups inside the given directory.
     * Mainly useful for tests, which can point this at a temporary directory.
     *
     * @param directory The directory in which contacts.json and groups.json will be read/written
     */
    public FileManager(Path directory) {
        this.contactsFile = directory.resolve(CONTACTS_FILE);
        this.groupsFile = directory.resolve(GROUPS_FILE);
    }

    /**
     * Saves the contacts data to a file as JSON.
     * If an error occurs during saving, it will be displayed to the user.
     *
     * @param contactManager The ContactManager object to be saved
     */
    public void saveContacts(ContactManager contactManager) {
        save(contactManager, contactsFile, "Error Saving Contacts");
    }

    /**
     * Saves the groups data to a file as JSON.
     * If an error occurs during saving, it will be displayed to the user.
     *
     * @param groupManager The GroupManager object to be saved
     */
    public void saveGroups(GroupManager groupManager) {
        save(groupManager, groupsFile, "Error Saving Groups");
    }

    private void save(Object data, Path file, String errorTitle) {
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            String message = "Error saving data: " + e.getMessage();
            System.err.println(message);
            Dialogs.error(message, errorTitle);
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
        return load(contactsFile, ContactManager.class, "Loading Contacts", "contacts", ContactManager::new);
    }

    /**
     * Loads groups data from the file.
     * If the file doesn't exist or an error occurs during loading,
     * a new empty GroupManager will be created and the user will be notified.
     *
     * @return The loaded GroupManager or a new empty one if loading fails
     */
    public GroupManager loadGroups() {
        return load(groupsFile, GroupManager.class, "Loading Groups", "groups", GroupManager::new);
    }

    private <T> T load(Path file, Class<T> type, String infoTitle, String dataName, java.util.function.Supplier<T> fallback) {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            T result = gson.fromJson(reader, type);
            return result != null ? result : fallback.get();
        } catch (IOException | JsonSyntaxException e) {
            String message = "Could not load " + dataName + " file. Creating new " + dataName + " file.";
            System.out.println(message);
            Dialogs.info(message, infoTitle);
            return fallback.get();
        }
    }
}
