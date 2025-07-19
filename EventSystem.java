import java.util.*;
import java.io.*;

class Event {
    String name;
    String category;
    int totalSeats;
    int seatsFilled;
    boolean isPaid;
    int minYear;

    public Event(String name, String category, int totalSeats, boolean isPaid, int minYear) {
        this.name = name;
        this.category = category;
        this.totalSeats = totalSeats;
        this.seatsFilled = 0;
        this.isPaid = isPaid;
        this.minYear = minYear;
    }
    // Check if seats are available
    public boolean isAvailable() {
        return seatsFilled < totalSeats;
    }

    public void registerStudent() {
        seatsFilled++;
    }
    // Show event details
    public String toString() {
        return name + " (" + category + "), Seats Left: " + (totalSeats - seatsFilled) + ", Paid: " + isPaid + ", Min Year: " + minYear;
    }

    public String saveFormat() {
        return name + "|" + category + "|" + totalSeats + "|" + isPaid + "|" + minYear;
    }
}

public class EventSystem {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Event> events = new ArrayList<>();
    static ArrayList<String> registrations = new ArrayList<>();

    static String folderPath = "C:\\event_data";
    static String registrationFile;
    static String eventsFile;

    public static void setFilePaths() {
        registrationFile = folderPath + "\\registrations.txt";
        eventsFile = folderPath + "\\events.txt";
    }

    public static void addEvents() {
        events.add(new Event("Tech Talk", "Technical", 3, true, 2));
        events.add(new Event("Drama Night", "Cultural", 5, false, 1));
        events.add(new Event("Football Match", "Sports", 4, false, 1));
    }

    // Student Menu
    public static void studentMenu() {
        System.out.println("\n--- Student Menu ---");
        System.out.println("1. View Events");
        System.out.println("2. Register for Event");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            showEvents();
        } else if (choice == 2) {
            registerStudent();
        } else if (choice == 3) {

        } else {
            System.out.println("Invalid option.");
        }
    }
    // Register a student for a event
    public static void registerStudent() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter your year (1-4): ");
        int year = sc.nextInt();
        sc.nextLine();

        if (year < 1 || year > 4) {
            System.out.println("Invalid year.");
            return;
        }

        showEvents();
        System.out.print("Choose event number: ");
        int eventNo = sc.nextInt();
        sc.nextLine();

        if (eventNo < 1 || eventNo > events.size()) {
            System.out.println("Invalid event number.");
            return;
        }

        Event event = events.get(eventNo - 1);

        if (!event.isAvailable()) {
            System.out.println("Sorry, no seats left.");
            return;
        }

        if (year < event.minYear) {
            System.out.println("You are not eligible for this event.");
            return;
        }
        // Register and save
        event.registerStudent();
        String record = name + " registered for " + event.name;
        registrations.add(record);
        saveToFile(registrationFile, record);
        System.out.println("✅ Registration successful!");
    }

    public static void showEvents() {
        System.out.println("\n--- Event List ---");
        for (int i = 0; i < events.size(); i++) {
            System.out.println((i + 1) + ". " + events.get(i));
        }
    }

    public static void showRegistrations() {
        System.out.println("\n--- Registered Students ---");
        if (registrations.size()==0) {
            System.out.println("No registrations yet.");
        } else {
            for (String reg : registrations) {
                System.out.println(reg);
            }
        }
    }

    public static boolean adminLogin() {
        System.out.print("Enter admin username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        return user.equals("admin") && pass.equals("1234");
    }

    public static void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View Events");
            System.out.println("2. Add New Event");
            System.out.println("3. View All Registrations");
            System.out.println("4. Logout");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                showEvents();
            } else if (choice == 2) {
                addEvent();
            } else if (choice == 3) {
                showRegistrations();
            } else if (choice == 4) {
                System.out.println("Admin logged out.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
    // Admin can add a new event
    public static void addEvent() {
        System.out.print("Enter event name: ");
        String name = sc.nextLine();
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        System.out.print("Total seats: ");
        int seats = sc.nextInt();
        sc.nextLine();
        System.out.print("Is it a paid event? (true/false): ");
        boolean isPaid = sc.nextBoolean();
        sc.nextLine();
        System.out.print("Minimum eligible year (1-4): ");
        int minYear = sc.nextInt();
        sc.nextLine();

        Event newEvent = new Event(name, category, seats, isPaid, minYear);
        events.add(newEvent);
        saveToFile(eventsFile, newEvent.saveFormat());
        System.out.println("✅ Event added successfully!");
    }
    // Save data to file
    public static void saveToFile(String filename, String data) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write(data + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + filename);
        }
    }

    public static void main(String[] args) {
        setFilePaths();
        addEvents();

        while (true) {
            System.out.println("\n===== Event System Menu =====");
            System.out.println("1. Student Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                studentMenu();
            } else if (choice == 2) {
                if (adminLogin()) {
                    adminMenu();
                } else {
                    System.out.println("❌ Invalid admin credentials.");
                }
            } else if (choice == 3) {
                System.out.println("Thank you!");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}