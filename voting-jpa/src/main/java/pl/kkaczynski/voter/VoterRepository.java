package pl.kkaczynski.voter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoterRepository extends JpaRepository<VoterEntity, Long> {
    Optional<VoterEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
