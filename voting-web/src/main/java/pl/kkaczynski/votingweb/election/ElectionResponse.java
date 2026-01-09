package pl.kkaczynski.votingweb.election;

import pl.kkaczynski.election.SelectionType;

import java.time.Instant;
import java.util.List;

public record ElectionResponse(
        long id,
        String name,
        SelectionType selectionType,
        Instant votingStartsAt,
        Instant votingEndsAt,
        List<ElectionOptionResponse> options
) {}
