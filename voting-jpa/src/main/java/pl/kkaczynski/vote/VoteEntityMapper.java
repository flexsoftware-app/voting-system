package pl.kkaczynski.vote;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kkaczynski.election.ElectionEntityMapper;
import pl.kkaczynski.election.ElectionOption;
import pl.kkaczynski.election.ElectionOptionMapper;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
final class VoteEntityMapper {

    public static VoteEntity toEntity(Vote vote) {
        VoteEntity voteEntity = new VoteEntity(vote.electionId(), vote.voterId());
        vote.selectionOptionsIds().forEach(s -> voteEntity.addSelection());
        voteEntity.addSelection();
        return voteEntity;
    }

    public Vote toDomain(VoteEntity voteEntity) {
        List<Long> electionOptions = voteEntity.getSelections().stream()
                .map(voteSelectionEntity -> voteSelectionEntity.getElectionOption().getId())
                .toList();


        return new Vote(voteEntity.getId(), voteEntity.getElectionId(), voteEntity.getVoterId(), electionOptions);
    }
}
