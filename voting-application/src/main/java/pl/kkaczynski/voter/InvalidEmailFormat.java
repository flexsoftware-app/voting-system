package pl.kkaczynski.voter;

public class InvalidEmailFormat extends RuntimeException {
    public InvalidEmailFormat(String message) {
        super(message);
    }
}
