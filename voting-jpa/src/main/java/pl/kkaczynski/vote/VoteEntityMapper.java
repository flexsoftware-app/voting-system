package pl.kkaczynski.vote;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kkaczynski.election.ElectionEntity;
import pl.kkaczynski.election.ElectionOptionEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
final class VoteEntityMapper {

    public static VoteEntity toEntity(Vote vote, ElectionEntity electionEntity) {
        VoteEntity voteEntity = new VoteEntity(vote.electionId(), vote.voterId());

        vote.selectionOptionsIds().forEach(optionId -> {
            ElectionOptionEntity option = electionEntity.getElectionOptionsEntities().stream()
                    .filter(opt -> opt.getId().equals(optionId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Election option not found: " + optionId));
            voteEntity.addSelection(option);
        });

        return voteEntity;
    }

    public Vote toDomain(VoteEntity voteEntity) {
        List<Long> electionOptions = voteEntity.getSelections().stream()
                .map(voteSelectionEntity -> voteSelectionEntity.getElectionOption().getId())
                .toList();

        return new Vote(voteEntity.getId(), voteEntity.getElectionId(), voteEntity.getVoterId(), electionOptions);
    }
}
