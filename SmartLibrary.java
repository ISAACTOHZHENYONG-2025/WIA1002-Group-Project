// ==========================================
// TASK 5 - ADMIN LOGIC
// Borrow / Return process; implements LibraryADT
//
// REQUIREMENT A - FUNCTIONAL CONSOLE INTERFACE
// Menu-driven console UI for Librarian / Student
// ==========================================

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
// (Keep the existing java.util.Scanner and InputMismatchException imports)

public class SmartLibrary implements LibraryADT {

    // Internal structures are PRIVATE (Information Hiding)
    private BookBST    catalogue = new BookBST();
    private BorrowStack history  = new BorrowStack();

    // -------------------------------------------------------
    //  LibraryADT Implementation
    // -------------------------------------------------------

    @Override
    public void addBook(int isbn, String title, String author) {
        catalogue.insert(isbn, title, author);
        System.out.println("  Book added: [ISBN: " + isbn + "] \"" + title + "\"");
    }

    @Override
    public void searchBook(int isbn) {
        Book b = catalogue.search(isbn);
        if (b != null) {
            System.out.println("  Found: " + b);
        } else {
            System.out.println("  Book with ISBN " + isbn + " not found in catalogue.");
        }
    }

    @Override
    public void borrowBook(int isbn) {
        Book b = catalogue.search(isbn);
        if (b != null) {
            catalogue.remove(isbn);   // Remove from catalogue
            history.push(b);          // Push onto history stack
            System.out.println("  Borrowed: " + b);
        } else {
            System.out.println("  Book with ISBN " + isbn + " is not in the catalogue.");
        }
    }

    @Override
    public void viewLatestHistory() {
        history.show();
    }

    @Override
    public void searchBookByText(String keyword) {
        catalogue.searchByText(keyword);
    }

    // -------------------------------------------------------
    //  Console Menu
    // -------------------------------------------------------

    public void runMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("   Welcome to the Smart Library System   ");
        System.out.println("==========================================");

        loadData();

        while (running) {
            printMenu();
            System.out.print("Choice: ");

            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine();  // consume leftover newline
            } catch (InputMismatchException e) {
                sc.nextLine();  // discard bad input
                System.out.println("  [Error] Please enter a number between 1 and 5.");
                continue;
            }

            // Inside your runMenu() while-loop:
            switch (choice) {
                case 1:
                    handleAddBook(sc);
                    break;
                case 2:
                    handleSearchBook(sc);
                    break;
                case 3:
                    handleSearchByText(sc); // The new text search!
                    break;
                case 4:
                    handleBorrowBook(sc);
                    break;
                case 5:
                    viewLatestHistory();
                    break;
                case 6:
                    handleReturnBook(); 
                    break;
                case 7:
                    running = false;
                    saveData(); 
                    System.out.println("  Goodbye!");
                    break;
                default:
                    System.out.println("  [Error] Invalid option. Choose 1–7.");
            }
        }

        sc.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- Smart Library Menu ---");
        System.out.println("  1. Add Book");
        System.out.println("  2. Search Book by ISBN (Fast)");
        System.out.println("  3. Search Book by Title/Author");
        System.out.println("  4. Borrow Book");
        System.out.println("  5. View Borrowing History");
        System.out.println("  6. Return Book");
        System.out.println("  7. Exit");
    }

    // -------------------------------------------------------
    //  Input Handlers (with validation)
    // -------------------------------------------------------

    private void handleAddBook(Scanner sc) {
        int isbn = readISBN(sc, "  Enter ISBN (integer): ");
        if (isbn == -1) return;

        if (history.containsISBN(isbn)) {
            System.out.println("  [Error] A book with ISBN " + isbn + " is currently borrowed out.");
            System.out.println("  You cannot add a new book with the same ISBN until it is returned.");
            return; // Stop the process immediately
        }

        System.out.print("  Enter Title: ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("  [Error] Title cannot be empty.");
            return;
        }

        System.out.print("  Enter Author: ");
        String author = sc.nextLine().trim();
        if (author.isEmpty()) {
            System.out.println("  [Error] Author cannot be empty.");
            return;
        }

        addBook(isbn, title, author);
    }

    private void handleSearchBook(Scanner sc) {
        int isbn = readISBN(sc, "  Enter ISBN to search: ");
        if (isbn == -1) return;
        searchBook(isbn);
    }

    private void handleBorrowBook(Scanner sc) {
        int isbn = readISBN(sc, "  Enter ISBN to borrow: ");
        if (isbn == -1) return;
        borrowBook(isbn);
    }

    /**
     * Reads a positive integer ISBN with input validation.
     * Returns -1 if the user enters invalid data.
     */
    private int readISBN(Scanner sc, String prompt) {
        System.out.print(prompt);
        try {
            int isbn = sc.nextInt();
            sc.nextLine();
            if (isbn <= 0) {
                System.out.println("  [Error] ISBN must be a positive integer.");
                return -1;
            }
            return isbn;
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("  [Error] ISBN must be an integer, not text.");
            return -1;
        }
    }

    // -------------------------------------------------------
    //  Data Persistence (File I/O)
    // -------------------------------------------------------
    private void loadData() {
        try {
            // Load Catalogue
            File catFile = new File("catalogue.csv");
            if (catFile.exists()) {
                Scanner sc = new Scanner(catFile);
                while (sc.hasNextLine()) {
                    String[] data = sc.nextLine().split(",");
                    if (data.length == 3) {
                        catalogue.insert(Integer.parseInt(data[0]), data[1], data[2]);
                    }
                }
                sc.close();
            }

            // Load History
            File histFile = new File("history.csv");
            if (histFile.exists()) {
                Scanner sc = new Scanner(histFile);
                while (sc.hasNextLine()) {
                    String[] data = sc.nextLine().split(",");
                    if (data.length == 3) {
                        // We create a new book object just for the history stack
                        history.push(new Book(Integer.parseInt(data[0]), data[1], data[2]));
                    }
                }
                sc.close();
            }
            System.out.println("  [System] Previous data loaded successfully.\n");
        } catch (Exception e) {
            System.out.println("  [Error] Could not load previous data.\n");
        }
    }

    private void saveData() {
        try {
            PrintWriter catWriter = new PrintWriter(new FileWriter("catalogue.csv"));
            catalogue.saveToCSV(catWriter);
            catWriter.close();

            PrintWriter histWriter = new PrintWriter(new FileWriter("history.csv"));
            history.saveToCSV(histWriter);
            histWriter.close();
            
            System.out.println("  [System] Data saved successfully.");
        } catch (IOException e) {
            System.out.println("  [Error] Could not save data.");
        }
    }

    private void handleReturnBook() {
        // Pop the most recently borrowed book from the stack
        Book returnedBook = history.pop();
        
        if (returnedBook != null) {
            // Insert it back into the catalogue
            catalogue.insert(returnedBook.isbn, returnedBook.title, returnedBook.author);
            System.out.println("  Successfully Returned: " + returnedBook);
        } else {
            System.out.println("  [Error] There are no borrowed books to return.");
        }
    }

    private void handleSearchByText(Scanner sc) {
        System.out.print("  Enter Title or Author keyword to search: ");
        String keyword = sc.nextLine().trim();
        
        if (keyword.isEmpty()) {
            System.out.println("  [Error] Search keyword cannot be empty.");
            return;
        }
        searchBookByText(keyword);
    }
}
