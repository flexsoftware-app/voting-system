package pl.kkaczynski.voter;

public class VoterNotFoundException extends RuntimeException {
    public VoterNotFoundException(String message) {
        super(message);
    }
}
