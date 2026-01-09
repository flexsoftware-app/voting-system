package pl.kkaczynski.voter;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoterService implements VoterCreate, VoterGet, VoterUpdate, VoterUtilService {

    private final VoterPort voterPort;

    @Override
    @Transactional
    public Long create(VoterCreateCommand command) {
        if (voterPort.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Voter with email " + command.email() + " already exists");
        }

        if (isValid(command.email())){
            throw new InvalidEmailFormat("Invalid email format");
        }

        Voter voter = new Voter(null, command.email(), command.firstName(), command.lastName(), false);
        return voterPort.save(voter);
    }

    @Override
    @Transactional(readOnly = true)
    public Voter getById(Long id) {
        return voterPort.findById(id)
                .orElseThrow(() -> new VoterNotFoundException("Voter not found, id=" + id));
    }

    @Override
    @Transactional
    public void block(Long id) {
        Voter voter = getById(id);
        Voter blockedVoter = new Voter(voter.id(), voter.email(), voter.firstName(), voter.lastName(), true);
        voterPort.save(blockedVoter);
    }

    @Override
    @Transactional
    public void unblock(Long id) {
        Voter voter = getById(id);
        Voter unblockedVoter = new Voter(voter.id(), voter.email(), voter.firstName(), voter.lastName(), false);
        voterPort.save(unblockedVoter);
    }

    @Override
    @Transactional(readOnly = true)
    public VoterStatus getStatus(@NotNull Long voterId) {
        Optional<Voter> voter = voterPort.findById(voterId);
        if (voter.isEmpty()) {
            return VoterStatus.NOT_EXIST;
        }
        Voter v = voter.get();
        if (v.blocked()) {
            return VoterStatus.BLOCK;
        }
        return VoterStatus.AVAILABLE;
    }

    private boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}
