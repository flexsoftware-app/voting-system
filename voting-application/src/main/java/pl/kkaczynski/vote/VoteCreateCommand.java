package pl.kkaczynski.vote;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record VoteCreateCommand(
        @NotNull Long voteId,
        @NotNull Long electionId,
        @NotNull Long voterId,
        @NotNull  List<@NotNull ElectionOptionComand> selectionsIds

) {
}
