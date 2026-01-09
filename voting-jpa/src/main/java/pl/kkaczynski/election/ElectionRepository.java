package pl.kkaczynski.election;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectionRepository extends JpaRepository<ElectionEntity, Long> {

    @EntityGraph(attributePaths = "electionOptions")
    Optional<ElectionEntity> findWithOptionsById(Long id);
}
