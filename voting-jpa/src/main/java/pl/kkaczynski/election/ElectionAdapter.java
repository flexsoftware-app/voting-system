package pl.kkaczynski.election;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Log4j2
public class ElectionAdapter implements ElectionPort , ElectionAdapterUtils{


    private final ElectionRepository electionRepository;

    @Override
    public Long save(Election election) {
        ElectionEntity entity = ElectionEntityMapper.toEntity(election);
        entity.validateTimeWindow();
        ElectionEntity saved = electionRepository.save(entity);
        log.debug("Election persisted for electionId={}, and name={}", saved.getId(), saved.getName());
        return saved.getId();
    }

    @Override
    public Optional<Election> findById(Long id) {
        return electionRepository.findWithOptionsById(id)
                .map(ElectionEntityMapper::toDomain);
    }

    @Override
    public boolean exist(Long electionId) {
        return electionRepository.existsById(electionId);
    }

    @Override
    public List<ElectionResult> getElectionResults(Long electionId) {
        List<ElectionResultView> results = electionRepository.findResults(electionId);
        return results.stream()
                .map(ElectionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public ElectionEntity getById(Long electionId) {
        return electionRepository.findWithOptionsById(electionId)
                .orElseThrow(() -> new ElectionNotFoundException("Election with id=" + electionId + " not found"));
    }
}
