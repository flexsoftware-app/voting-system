package pl.kkaczynski.voter;

import java.util.Optional;

public interface VoterPort {

    Long save(Voter voter);

    Optional<Voter> findById(Long id);

    Optional<Voter> findByEmail(String email);

    boolean existsByEmail(String email);
}
