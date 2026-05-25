// ==========================================
// TASK 4 - ADT DESIGNER
// Interface for the Library System (Information Hiding)
// ==========================================

public interface LibraryADT {
    void addBook(int isbn, String title, String author);
    void borrowBook(int isbn);
    void viewLatestHistory();
    void searchBook(int isbn);
    void searchBookByText(String keyword);
}
