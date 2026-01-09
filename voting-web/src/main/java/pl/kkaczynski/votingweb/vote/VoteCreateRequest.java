package pl.kkaczynski.votingweb.vote;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VoteCreateRequest(
        @NotNull Long voterId,
        @NotNull @NotEmpty List<@NotNull Long> selectionsIds
) {
}
