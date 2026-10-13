package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of contacts in the contact management system.
 * Provides functionality for adding, updating, deleting, searching, and sorting contacts.
 */
public class ContactManager {

    /** List of contacts managed by this ContactManager */
    private List<Contact> contacts;

    /**
     * Constructs a new ContactManager with an empty list of contacts.
     */
    public ContactManager() {
        contacts = new ArrayList<>();
    }

    /**
     * Adds a new contact to the collection.
     * Throws IllegalArgumentException if the contact is null.
     *
     * @param contact The contact to be added
     * @throws IllegalArgumentException if contact is null 
     * @return true if the contact was added successfully
     */
    public boolean addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
       
        contacts.add(contact);
        return true;
    }

    /**
     * Updates an existing contact in the collection.
     * If the contact is found, it will be replaced with the updated version.
     * Throws IllegalArgumentException if the contact is null or if the contact doesn't exist.
     *
     * @param contact The contact to be updated
     * @throws IllegalArgumentException if contact is null or if the contact doesn't exist
     */
    public void updateContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        int index = contacts.indexOf(contact);
        if (index == -1) {
            throw new IllegalArgumentException("Contact does not exist");
        }
        contacts.set(index, contact);
    }

    /**
     * Removes a contact from the collection.
     * Throws IllegalArgumentException if the contact is null.
     *
     * @param contact The contact to be removed
     * @throws IllegalArgumentException if contact is null
     * @return true if the contact was removed, false if it wasn't found
     */
    public boolean deleteContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        return contacts.remove(contact);
    }

    /**
     * Retrieves all contacts in the collection.
     * Returns a new ArrayList to prevent external modification of the internal list.
     *
     * @return A new list containing all contacts
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    /**
     * Searches for contacts based on the given criteria.
     * The search is case-insensitive and matches contacts whose first name or last name
     * starts with the search criteria.
     *
     * @param criteria The search string to match against contact names
     * @return A list of contacts matching the search criteria
     */
    public List<Contact> searchContacts(String criteria) {
        String searchCriteria = criteria.toLowerCase();
        return contacts.stream()
                .filter(contact -> contact.getFirstName().toLowerCase().startsWith(searchCriteria) || 
                                 contact.getLastName().toLowerCase().startsWith(searchCriteria))
                .collect(Collectors.toList());
    }

    /**
     * Sorts all contacts by their first name in ascending order.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortByFirstName() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getFirstName))
                .collect(Collectors.toList());
    }

    /**
     * Sorts all contacts by their last name in ascending order.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortByLastName() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getLastName))
                .collect(Collectors.toList());
    }

    /**
     * Sorts all contacts by their city in ascending order.
     *
     * @return A new sorted list of contacts
     */
    public List<Contact> sortByCity() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getCity))
                .collect(Collectors.toList());
    }
}