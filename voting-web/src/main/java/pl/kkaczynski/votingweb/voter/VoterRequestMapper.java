package pl.kkaczynski.votingweb.voter;

import org.springframework.stereotype.Component;
import pl.kkaczynski.voter.Voter;
import pl.kkaczynski.voter.VoterCreateCommand;

@Component
public class VoterRequestMapper {

    public VoterCreateCommand toCommand(VoterCreateRequest request) {
        return new VoterCreateCommand(
                request.email(),
                request.firstName(),
                request.lastName()
        );
    }

    public VoterResponse toResponse(Voter voter) {
        return new VoterResponse(
                voter.id(),
                voter.email(),
                voter.firstName(),
                voter.lastName(),
                voter.blocked()
        );
    }
}