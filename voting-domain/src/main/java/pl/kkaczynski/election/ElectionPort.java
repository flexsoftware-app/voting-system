package pl.kkaczynski.election;

import java.util.List;
import java.util.Optional;

public interface ElectionPort {

    Long save(Election election);

    Optional<Election> findById(Long id);

    boolean exist(Long electionId);

    List<ElectionResult> getElectionResults(Long electionId);
}
