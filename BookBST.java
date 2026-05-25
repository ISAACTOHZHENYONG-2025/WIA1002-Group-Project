// ==========================================
// TASK 1 - CATALOGUE ARCHITECT
// Binary Search Tree to store books by ISBN
//
// TASK 3 - RECORD FINDER
// Recursive search function (O(log n))
// ==========================================

public class BookBST {

    private Book root;   // root is private — Information Hiding

    public BookBST() {
        this.root = null;
    }

    // ---- PUBLIC INSERT ----
    public void insert(int isbn, String title, String author) {
        root = ins(root, isbn, title, author);
    }

    // Recursive private helper
    private Book ins(Book node, int isbn, String title, String author) {
        if (node == null) {
            return new Book(isbn, title, author);
        }
        if (isbn < node.isbn) {
            node.left  = ins(node.left,  isbn, title, author);
        } else if (isbn > node.isbn) {
            node.right = ins(node.right, isbn, title, author);
        } else {
            // Duplicate ISBN — update title and author instead
            System.out.println("  [BST] ISBN already exists. Updating record.");
            node.title  = title;
            node.author = author;
        }
        return node;
    }

    // ---- PUBLIC SEARCH (O(log n)) ----
    public Book search(int isbn) {
        return sea(root, isbn);
    }

    // Recursive private helper — O(log n) on a balanced tree
    private Book sea(Book node, int isbn) {
        if (node == null || node.isbn == isbn) {
            return node;          // found or not present
        }
        return (isbn < node.isbn)
                ? sea(node.left,  isbn)
                : sea(node.right, isbn);
    }

    // ---- PUBLIC REMOVE (used by Borrow) ----
    public boolean remove(int isbn) {
        if (search(isbn) == null) return false;
        root = del(root, isbn);
        return true;
    }

    // Recursive delete helper
    private Book del(Book node, int isbn) {
        if (node == null) return null;

        if (isbn < node.isbn) {
            node.left  = del(node.left,  isbn);
        } else if (isbn > node.isbn) {
            node.right = del(node.right, isbn);
        } else {
            // Node to delete found
            if (node.left  == null) return node.right;
            if (node.right == null) return node.left;

            // Two children: replace with in-order successor (smallest in right subtree)
            Book successor = findMin(node.right);
            node.isbn   = successor.isbn;
            node.title  = successor.title;
            node.author = successor.author;
            node.right  = del(node.right, successor.isbn);
        }
        return node;
    }

    private Book findMin(Book node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---- PUBLIC SAVE TO CSV (Data Persistence) ----
    public void saveToCSV(java.io.PrintWriter pw) {
        savePreOrder(root, pw);
    }

    private void savePreOrder(Book node, java.io.PrintWriter pw) {
        if (node != null) {
            // Format: isbn,title,author
            pw.println(node.isbn + "," + node.title + "," + node.author);
            savePreOrder(node.left, pw);
            savePreOrder(node.right, pw);
        }
    }

    // ---- PUBLIC SEARCH BY TEXT (O(n) Traversal) ----
    public void searchByText(String keyword) {
        // We use an array of size 1 as a counter so it updates across the recursion
        int[] matchCount = {0}; 
        System.out.println("  Searching catalogue for: \"" + keyword + "\"...");
        
        searchByTextHelper(root, keyword.toLowerCase(), matchCount);
        
        if (matchCount[0] == 0) {
            System.out.println("  [Result] No books found matching that description.");
        } else {
            System.out.println("  [Result] Found " + matchCount[0] + " matching book(s).");
        }
    }

    private void searchByTextHelper(Book node, String keyword, int[] matchCount) {
        if (node != null) {
            // 1. Visit Left
            searchByTextHelper(node.left, keyword, matchCount);
            
            // 2. Check Current Node (convert to lowercase for case-insensitive matching)
            String lowerTitle = node.title.toLowerCase();
            String lowerAuthor = node.author.toLowerCase();
            
            if (lowerTitle.contains(keyword) || lowerAuthor.contains(keyword)) {
                System.out.println("    -> " + node.toString());
                matchCount[0]++; // Increase the found counter
            }
            
            // 3. Visit Right
            searchByTextHelper(node.right, keyword, matchCount);
        }
    }
}
