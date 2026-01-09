package pl.kkaczynski.votingweb.vote;

import org.springframework.stereotype.Component;
import pl.kkaczynski.vote.Vote;
import pl.kkaczynski.vote.VoteCreateCommand;

@Component
public class VoteRequestMapper {

    public VoteCreateCommand toCommand(Long electionId, VoteCreateRequest request) {
        return new VoteCreateCommand(
                electionId,
                request.voterId(),
                request.selectionsIds()
        );
    }

    public VoteResponse toResponse(Vote vote) {
        return new VoteResponse(
                vote.voteId(),
                vote.electionId(),
                vote.voterId(),
                vote.selectionOptionsIds()
        );
    }
}
