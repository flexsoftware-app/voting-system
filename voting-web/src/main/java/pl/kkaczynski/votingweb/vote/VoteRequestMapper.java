package pl.kkaczynski.votingweb.vote;

import pl.kkaczynski.vote.VoteCreateCommand;

public class VoteRequestMapper {

    public VoteCreateCommand toCommand(Long electionId, VoteCreateRequest request) {
        return new VoteCreateCommand(request.voterId(),electionId, request.voterId(), request.selectionsIds());
    }
}
