package pl.kkaczynski.election;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElectionMapper {

    Election toDomain(ElectionCreateCommand createCommand) {
        List<ElectionOption> electionOptions = createCommand.options().stream().map(this::toDomain).toList();
        return new Election(null, createCommand.name(), createCommand.selfVotingPolicy(), createCommand.selectionType(), createCommand.votingStartsAt(), createCommand.votingEndsAt(), electionOptions);
    }

    ElectionOption toDomain(ElectionOptionCreateCommand createCommand){
        return new ElectionOption(null, createCommand.name(), createCommand.description());
    }
}
