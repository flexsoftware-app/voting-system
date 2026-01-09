package pl.kkaczynski.votingweb.election;

import pl.kkaczynski.election.SelectionType;
import pl.kkaczynski.election.SelfVotingPolicy;

import java.time.Instant;
import java.util.List;

public record ElectionResponse(
        long id,
        String name,
        SelectionType selectionType,
        SelfVotingPolicy selfVotingPolicy,
        Instant votingStartsAt,
        Instant votingEndsAt,
        List<ElectionOptionResponse> options
) {}
