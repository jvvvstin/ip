public class ButtercupException extends Exception {
    public ButtercupException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        String str = String.format("[ButtercupException]: %s", this.getMessage());
        return str;
    }
}
