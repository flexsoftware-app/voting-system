package pl.kkaczynski.election;

import java.time.Instant;
import java.util.List;

public record Election(
        Long id,
        String name,
        SelectionType selectionType,
        Instant votingStartsAt,
        Instant votingEndsAt,
        List<ElectionOption> electionOptions
) {
}
