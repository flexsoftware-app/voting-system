package pl.kkaczynski.vote;

import java.util.List;

public record Vote(
        Long voteId,
        Long electionId,
        Long voterId,
        List<Long> selectionOptionsIds) {
    public Vote {
        if (selectionOptionsIds == null || selectionOptionsIds.isEmpty()) {
            throw new IllegalArgumentException("No options selected");
        }
    }
}
