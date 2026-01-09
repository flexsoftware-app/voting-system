package pl.kkaczynski.votingweb.election;

public record ElectionOptionResponse(
        Long id,
        String name,
        String description
) {
}
