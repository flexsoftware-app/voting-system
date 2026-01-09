package pl.kkaczynski.election;

public class ElectionNotFoundException extends RuntimeException {

    public ElectionNotFoundException(String message) {
        super(message);
    }
}
