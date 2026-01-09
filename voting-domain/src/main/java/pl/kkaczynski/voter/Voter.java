package pl.kkaczynski.voter;

public record Voter(
        Long id,
        String email,
        String firstName,
        String lastName,
        boolean blocked
) {}