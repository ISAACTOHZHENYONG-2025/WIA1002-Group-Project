// ==========================================
// ENTITY CLASS
// Represents a single book record in the system
// ==========================================

public class Book {
    int isbn;
    String title;
    String author;

    // BST child pointers (used by BookBST)
    Book left, right;

    public Book(int isbn, String title, String author) {
        this.isbn   = isbn;
        this.title  = title;
        this.author = author;
        this.left   = null;
        this.right  = null;
    }

    @Override
    public String toString() {
        return "[ISBN: " + isbn + "] \"" + title + "\" by " + author;
    }
}
