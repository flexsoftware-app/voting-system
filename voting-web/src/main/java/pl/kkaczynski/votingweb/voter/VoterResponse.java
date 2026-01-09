package pl.kkaczynski.votingweb.voter;

public record VoterResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        boolean blocked
) {
}
