// ==========================================
// TASK 2 - BORROWING HISTORY
// Stack to track borrowed books (LIFO order)
// ==========================================

import java.util.Stack;

public class BorrowStack {

    private Stack<Book> stack;   // private — Information Hiding

    public BorrowStack() {
        this.stack = new Stack<>();
    }

    // Push a borrowed book onto the stack
    public void push(Book b) {
        stack.push(b);
    }

    // Display history — most recent first (LIFO)
    public void show() {
        if (stack.isEmpty()) {
            System.out.println("  No borrowing history yet.");
        } else {
            System.out.println("  --- Borrowing History (most recent first) ---");
            // Iterate from top (last index) to bottom
            for (int i = stack.size() - 1; i >= 0; i--) {
                Book b = stack.get(i);
                System.out.println("  " + (stack.size() - i) + ". " + b);
            }
            System.out.println("  ---------------------------------------------");
        }
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }
}
