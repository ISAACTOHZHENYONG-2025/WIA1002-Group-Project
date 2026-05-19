// ==========================================
// TASK 5 - ADMIN LOGIC
// Borrow / Return process; implements LibraryADT
//
// REQUIREMENT A - FUNCTIONAL CONSOLE INTERFACE
// Menu-driven console UI for Librarian / Student
// ==========================================

import java.util.InputMismatchException;
import java.util.Scanner;

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

    // -------------------------------------------------------
    //  Console Menu
    // -------------------------------------------------------

    public void runMenu() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("   Welcome to the Smart Library System   ");
        System.out.println("==========================================");

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

            switch (choice) {
                case 1:
                    handleAddBook(sc);
                    break;
                case 2:
                    handleSearchBook(sc);
                    break;
                case 3:
                    handleBorrowBook(sc);
                    break;
                case 4:
                    viewLatestHistory();
                    break;
                case 5:
                    running = false;
                    System.out.println("  Goodbye!");
                    break;
                default:
                    System.out.println("  [Error] Invalid option. Choose 1–5.");
            }
        }

        sc.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- Smart Library Menu ---");
        System.out.println("  1. Add Book");
        System.out.println("  2. Search Book (BST)");
        System.out.println("  3. Borrow Book (Stack)");
        System.out.println("  4. View Borrowing History");
        System.out.println("  5. Exit");
    }

    // -------------------------------------------------------
    //  Input Handlers (with validation)
    // -------------------------------------------------------

    private void handleAddBook(Scanner sc) {
        int isbn = readISBN(sc, "  Enter ISBN (integer): ");
        if (isbn == -1) return;

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
}
