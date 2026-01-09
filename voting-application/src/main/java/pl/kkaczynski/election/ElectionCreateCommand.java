package pl.kkaczynski.election;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record ElectionCreateCommand(
        @NotNull String name,
        @NotNull SelectionType selectionType,
        @NotNull Instant votingStartsAt,
        @NotNull Instant votingEndsAt,
        @NotNull @NotEmpty List<@Valid ElectionOptionCreateCommand> options

) {
}
