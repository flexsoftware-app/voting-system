package pl.kkaczynski.election;

import jakarta.validation.constraints.NotNull;

public record ElectionOptionCreateCommand(
        @NotNull String name,
        String description
) {
}
