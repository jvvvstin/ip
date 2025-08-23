package buttercup.exceptions;

/**
 * Represents an Exception related to Buttercup chatbot that is thrown
 * when an error occurs.
 */
public class ButtercupException extends Exception {
    public ButtercupException(String message) {
        super(message);
    }

    /**
     * Returns a <code>String</code> representation of the exception
     * @return A <code>String</code> representation of the exception
     */
    @Override
    public String toString() {
        String str = String.format("[ButtercupException]: %s", this.getMessage());
        return str;
    }
}
