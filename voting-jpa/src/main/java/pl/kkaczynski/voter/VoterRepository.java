package pl.kkaczynski.voter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRepository extends JpaRepository<VoterEntity, Long> {
}
