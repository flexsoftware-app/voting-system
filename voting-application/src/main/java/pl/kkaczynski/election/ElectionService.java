package pl.kkaczynski.election;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ElectionService implements ElectionCreate , ElectionGet, ElectionUtilService {

    private final ElectionPort electionPort;
    private final ElectionMapper mapper;

    @Override
    public Long create(ElectionCreateCommand electionCreateCommand) {
        Election election = mapper.toDomain(electionCreateCommand);
        return electionPort.save(election);
    }

    @Override
    public Election getById(@NotNull Long electionId) {
        return electionPort.findById(electionId)
                .orElseThrow(() -> new ElectionNotFoundException("Election not found, id=" + electionId));
    }

    @Override
    public boolean isActive(@NotNull Long electionId) {
        Election election = electionPort.findById(electionId)
                .orElseThrow(() -> new ElectionNotFoundException("Election not found, id=" + electionId));
        Instant now = Instant.now();
        return !now.isBefore(election.votingStartsAt()) && !now.isAfter(election.votingEndsAt());
    }

    @Override
    public boolean exist(@NotNull Long electionId) {
        return electionPort.exist(electionId);
    }
}
