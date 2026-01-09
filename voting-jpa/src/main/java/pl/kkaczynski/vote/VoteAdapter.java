package pl.kkaczynski.vote;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import pl.kkaczynski.election.*;

@Repository
@RequiredArgsConstructor
@Log4j2
public class VoteAdapter implements VotePort {

    private final VoteRepository voteRepository;
    private final VoteEntityMapper voteEntityMapper;
    private final ElectionAdapterUtils electionAdapterUtils;


    @Override
    public Long save(Vote vote) {

        ElectionEntity electionEntity = electionAdapterUtils.getById(vote.electionId());
        VoteEntity voteEntity = VoteEntityMapper.toEntity(vote, electionEntity);
        VoteEntity saved = voteRepository.save(voteEntity);
        Long id = saved.getId();
        log.debug("Vote persisted for electionId={}, and voteId={}", saved.getElectionId(), id);
        return id;
    }

    @Override
    public Vote findById(Long voteId) {
        return voteRepository.findById(voteId)
                .map(voteEntityMapper::toDomain)
                .orElseThrow(() -> new VoteNotFoundException("Vote not found id=" + voteId));
    }

    @Override
    public boolean existsByElectionIdAndVoterId(Long electionId, Long voterId) {
        return voteRepository.existsByElectionIdAndVoterId(electionId, voterId);
    }
}

