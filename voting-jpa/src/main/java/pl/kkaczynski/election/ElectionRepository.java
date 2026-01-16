package pl.kkaczynski.election;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ElectionRepository extends JpaRepository<ElectionEntity, Long> {

    @EntityGraph(attributePaths = "electionOptions")
    Optional<ElectionEntity> findWithOptionsById(Long id);


    @Query(value = """
            SELECT
                eo.election_id AS electionId,
                eo.id AS optionId,
                eo.name AS name,
                eo.description AS description,
                COUNT(v.id) AS votes,
                SUM(COUNT(v.id)) OVER() AS totalVotes
            FROM election_option_entity eo
            LEFT JOIN vote_selections v ON v.election_option_id = eo.id
            GROUP BY eo.id, eo.description
            ORDER BY votes DESC;
            """, nativeQuery = true)
    List<ElectionResultView> findResults(@Param("electionId")Long electionId);

}