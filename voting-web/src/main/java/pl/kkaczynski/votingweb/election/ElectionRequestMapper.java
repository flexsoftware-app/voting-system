package pl.kkaczynski.votingweb.election;

import org.springframework.stereotype.Component;
import pl.kkaczynski.election.*;

import java.util.List;

@Component
public class ElectionRequestMapper {

    ElectionCreateCommand toCommand(ElectionCreateRequest request){
        List<ElectionOptionCreateCommand> optionCommands = request.optionRequests().stream().map(this::toCommand).toList();
        return new ElectionCreateCommand(request.name(), request.selectionType(),request.votingStartsAt(),request.votingEndsAt(),optionCommands);
    };

    ElectionOptionCreateCommand toCommand (CreateElectionOptionRequest request){
        return new ElectionOptionCreateCommand(request.name(), request.description());
    }

    ElectionResponse toResponse(Election election){
        List<ElectionOptionResponse> optionResponses = election.electionOptions().stream().map(this::toResponse).toList();
        return new ElectionResponse(election.id(),election.name(),election.selectionType(), election.votingStartsAt(),election.votingEndsAt(),optionResponses);
    };


    ElectionOptionResponse toResponse(ElectionOption electionOption){
        return new ElectionOptionResponse(electionOption.id(), electionOption.name(), electionOption.description());
    }

    List<ElectionResultsResponse> toResponse(List<ElectionResult> electionResults) {
        return electionResults.stream()
                .map(this::toResponse)
                .toList();
    }

    ElectionResultsResponse toResponse(ElectionResult electionResult) {
        return new ElectionResultsResponse(electionResult.electionId(), electionResult.optionId(), electionResult.name(),electionResult.description(),electionResult.votes(), electionResult.totalVotes(), electionResult.getPercentVotes());
    }
}
