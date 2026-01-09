package pl.kkaczynski.votingweb.vote;

import java.util.List;

public record VoteResponse(
        Long voteId,
        Long electionId,
        Long voterId,
        List<Long> selectionsIds
) {
}
