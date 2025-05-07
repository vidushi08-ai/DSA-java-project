import java.util.*;
import java.io.*;

class Contact {
    String name;
    String phone;

    Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String toString() {
        return "Name: " + name + ", Phone: " + phone;
    }
}

public class phonebook {
    static ArrayList<Contact> contacts = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        while (true) {
            System.out.println("\nPhonebook Menu:");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contacts");
            System.out.println("3. Edit Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Search Contact");
            System.out.println("6. Save & Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> addContact();
                case 2 -> viewContacts();
                case 3 -> editContact();
                case 4 -> deleteContact();
                case 5 -> searchContact();
                case 6 -> {
                    saveToFile();
                    System.out.println("Saved and exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void addContact() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        String phone;
        while (true) {
            System.out.print("Enter phone (1 to 10 digits only): ");
            phone = sc.nextLine();
            if (phone.matches("\\d{1,10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter 1 to 10 digits only.");
            }
        }
        
        contacts.add(new Contact(name, phone));
        System.out.println("Contact added.");
    }

    static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        contacts.sort(Comparator.comparing(c -> c.name.toLowerCase()));
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println((i + 1) + ". " + contacts.get(i));
        }
    }

    static void editContact() {
        viewContacts();
        System.out.print("Enter contact number to edit: ");
        int index = sc.nextInt() - 1; sc.nextLine();
        if (index >= 0 && index < contacts.size()) {
            System.out.print("Enter new name: ");
            contacts.get(index).name = sc.nextLine();
            String newPhone;
            while (true) {
                System.out.print("Enter new phone (1 to 10 digits only): ");
                newPhone = sc.nextLine();
                if (newPhone.matches("\\d{1,10}")) {
                    break;
                } else {
                    System.out.println("Invalid phone number. Please enter 1 to 10 digits only.");
                }
            }
    
            contacts.get(index).phone = newPhone;
            System.out.println("Contact updated.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    static void deleteContact() {
        viewContacts();
        System.out.print("Enter contact number to delete: ");
        int index = sc.nextInt() - 1; sc.nextLine();
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
            System.out.println("Contact deleted.");
        } else {
            System.out.println("Invalid contact number.");
        }
    }

    static void searchContact() {
        System.out.print("Enter name or phone to search: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.toLowerCase().contains(keyword) || c.phone.contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching contact found.");
        }
    }

    static void saveToFile() {
        try (PrintWriter writer = new PrintWriter("contacts.txt")) {
            for (Contact c : contacts) {
                writer.println(c.name + "," + c.phone);
            }
        } catch (IOException e) {
            System.out.println("Error saving contacts.");
        }
    }

    static void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File("contacts.txt"))) {
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",", 2);
                if (parts.length == 2) {
                    contacts.add(new Contact(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            // File not found on first run, ignore.
        }
    }
}
