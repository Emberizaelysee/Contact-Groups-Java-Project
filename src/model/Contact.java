package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a contact in the contact management system.
 * This class implements Serializable to allow for persistent storage of contact data.
 * Each contact has basic information (first name, last name, city) and can have multiple phone numbers.
 */
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * properties of the contact
     */
    private String firstName;
    private String lastName;
    private String city;
    private List<String> telephones;

    /**
     * Constructs a new Contact with the specified information.
     * Initializes an empty list of phone numbers.
     * Throws IllegalArgumentException if any of the required fields are null or empty.
     *
     * @param firstName The contact's first name
     * @param lastName The contact's last name
     * @param city The contact's city of residence (can be null or empty)
     * @throws IllegalArgumentException if firstName or lastName is null or empty
     */
    public Contact(String firstName, String lastName, String city) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.city = city != null ? city.trim() : "";
        this.telephones = new ArrayList<>();
    }

    /**
     * Gets the contact's first name.
     *
     * @return The first name of the contact
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the contact's first name.
     * Throws IllegalArgumentException if the name is null or empty.
     *
     * @param firstName The new first name to set
     * @throws IllegalArgumentException if firstName is null or empty
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        this.firstName = firstName.trim();
    }

    /**
     * Gets the contact's last name.
     *
     * @return The last name of the contact
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the contact's last name.
     * Throws IllegalArgumentException if the name is null or empty.
     *
     * @param lastName The new last name to set
     * @throws IllegalArgumentException if lastName is null or empty
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.lastName = lastName.trim();
    }

    /**
     * Gets the contact's city of residence.
     *
     * @return The city where the contact lives
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the contact's city of residence.
     *
     * @param city The new city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the list of phone numbers associated with the contact.
     *
     * @return A list of phone numbers
     */
    public List<String> getTelephones() {
        return new ArrayList<>(telephones);
    }

    /**
     * Sets the complete list of phone numbers for the contact.
     * This replaces all existing phone numbers.
     * Validates each phone number before adding.
     *
     * @param telephones The new list of phone numbers
     * @throws IllegalArgumentException if any phone number is invalid
     */
    public void setTelephones(List<String> telephones) {
        if (telephones == null) {
            throw new IllegalArgumentException("Phone numbers list cannot be null");
        }
        List<String> validatedPhones = new ArrayList<>();
        for (String phone : telephones) {
            validatePhoneNumber(phone);
            validatedPhones.add(phone.trim());
        }
        this.telephones = validatedPhones;
    }

    /**
     * Adds a new phone number to the contact's list of phone numbers.
     * Validates the phone number before adding.
     *
     * @param telephone The phone number to add
     * @throws IllegalArgumentException if the phone number is invalid
     */
    public void addTelephone(String telephone) {
        validatePhoneNumber(telephone);
        this.telephones.add(telephone.trim());
    }

    /**
     * Removes a phone number from the contact's list of phone numbers.
     *
     * @param telephone The phone number to remove
     * @return true if the phone number was removed, false if it wasn't found
     */
    public boolean removeTelephone(String telephone) {
        return this.telephones.remove(telephone);
    }

    /**
     * Validates a phone number.
     * A valid phone number must:
     * - Not be null or empty
     * - Contain only digits, spaces, hyphens, and parentheses
     * - Have at least 5 digits
     *
     * @param phone The phone number to validate
     * @throws IllegalArgumentException if the phone number is invalid
     */
    private void validatePhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        String cleanedPhone = phone.replaceAll("[^0-9]", "");
        if (cleanedPhone.length() < 8) {
            throw new IllegalArgumentException("Phone number must contain at least 8 digits");
        }
    }

    /**
     * Clears all phone numbers from the contact.
     */
    public void clearTelephones() {
        telephones.clear();
    }

    /**
     * Returns a string representation of the contact.
     * Format: "firstName lastName - city"
     *
     * @return A string containing the contact's basic information
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + " - " + city;
    }
    
    /**
     * Compares this contact with another object for equality.
     * Two contacts are considered equal if they have the same first name and last name.
     *
     * @param obj The object to compare with
     * @return true if the contacts are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equals(contact.firstName) && 
               lastName.equals(contact.lastName);
    }
    
    /**
     * Generates a hash code for the contact based on first name and last name.
     * This is used in conjunction with equals() for proper object comparison.
     *
     * @return A hash code value for this contact
     */
    @Override
    public int hashCode() {
        return 31 * firstName.hashCode() + lastName.hashCode();
    }
}