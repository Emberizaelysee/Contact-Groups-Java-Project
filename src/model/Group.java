package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of contacts in the contact management system.
 * This class implements Serializable to allow for persistent storage of group data.
 * A group has a name, description, and can contain multiple contacts.
 * Groups are uniquely identified by their name.
 */
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * properties of the group
     */
    private String name;
    private String description;
    private List<Contact> contacts;

    /**
     * Constructs a new Group with the specified name and description.
     * Initializes an empty list of contacts.
     * Throws IllegalArgumentException if the name is null or empty.
     *
     * @param name The name of the group
     * @param description A description of the group
     * @throws IllegalArgumentException if name is null or empty
     */
    public Group(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        this.name = name.trim();
        this.description = description != null ? description.trim() : "";
        this.contacts = new ArrayList<>();
    }

    /**
     * Gets the name of the group.
     *
     * @return The group's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the group.
     * Throws IllegalArgumentException if the name is null or empty.
     *
     * @param name The new name for the group
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        this.name = name.trim();
    }

    /**
     * Gets the description of the group.
     *
     * @return The group's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the group.
     * If null is provided, an empty string will be used instead.
     *
     * @param description The new description for the group
     */
    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }

    /**
     * Gets the list of contacts in the group.
     * Returns a new ArrayList to prevent external modification of the internal list.
     *
     * @return A list of contacts belonging to this group
     */
    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    /**
     * Adds a contact to the group if it's not already present.
     * This method prevents duplicate contacts in the group.
     * Throws IllegalArgumentException if the contact is null.
     *
     * @param contact The contact to add to the group
     * @throws IllegalArgumentException if contact is null
     */
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (!contacts.contains(contact)) {
            contacts.add(contact);
        }
    }

    /**
     * Removes a contact from the group.
     * Throws IllegalArgumentException if the contact is null.
     *
     * @param contact The contact to remove from the group
     * @throws IllegalArgumentException if contact is null
     * @return true if the contact was removed, false if it wasn't found
     */
    public boolean removeContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        return contacts.remove(contact);
    }
    
    /**
     * Removes all contacts from the group.
     * This method clears the entire contact list.
     */
    public void clearContacts() {
        contacts.clear();
    }

    /**
     * Returns a string representation of the group.
     * Format: "name (X contacts)" where X is the number of contacts in the group.
     *
     * @return A string containing the group's name and contact count
     */
    @Override
    public String toString() {
        return name + " (" + contacts.size() + " contacts)";
    }
    
    /**
     * Compares this group with another object for equality.
     * Two groups are considered equal if they have the same name.
     *
     * @param obj The object to compare with
     * @return true if the groups are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Group group = (Group) obj;
        return name.equals(group.name);
    }
    
    /**
     * Generates a hash code for the group based on its name.
     * This is used in conjunction with equals() for proper object comparison.
     *
     * @return A hash code value for this group
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}