package pl.kkaczynski.voter;

import org.jetbrains.annotations.NotNull;

public record VoterCreateCommand(
        @NotNull String email,
        @NotNull String firstName,
        @NotNull String lastName
) {
}
