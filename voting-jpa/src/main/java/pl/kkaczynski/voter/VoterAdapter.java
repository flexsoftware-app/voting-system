package pl.kkaczynski.voter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import pl.kkaczynski.voter.VoterNotFoundException;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class VoterAdapter implements VoterPort {

    private final VoterRepository voterRepository;

    @Override
    public Long save(Voter voter) {
        if (voter.id() != null) {
            VoterEntity entity = voterRepository.findById(voter.id())
                    .orElseThrow(() -> new VoterNotFoundException("Voter not found, id=" + voter.id()));
            VoterEntityMapper.updateEntity(entity, voter);
            VoterEntity saved = voterRepository.save(entity);
            log.debug("Voter updated for voterId={}, email={}", saved.getId(), saved.getEmail());
            return saved.getId();
        } else {
            VoterEntity entity = VoterEntityMapper.toEntity(voter);
            VoterEntity saved = voterRepository.save(entity);
            log.debug("Voter persisted for voterId={}, email={}", saved.getId(), saved.getEmail());
            return saved.getId();
        }
    }

    @Override
    public Optional<Voter> findById(Long id) {
        return voterRepository.findById(id)
                .map(VoterEntityMapper::toDomain);
    }

    @Override
    public Optional<Voter> findByEmail(String email) {
        return voterRepository.findByEmail(email)
                .map(VoterEntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return voterRepository.existsByEmail(email);
    }
}
