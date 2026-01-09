package pl.kkaczynski.votingweb.election;

import jakarta.validation.constraints.NotNull;

public record CreateElectionOptionRequest(
        @NotNull String name,
        String description
) {
}
