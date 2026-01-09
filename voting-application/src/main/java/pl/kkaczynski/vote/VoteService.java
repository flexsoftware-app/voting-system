package pl.kkaczynski.vote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kkaczynski.election.Election;
import pl.kkaczynski.election.ElectionOption;
import pl.kkaczynski.election.ElectionStatusException;
import pl.kkaczynski.election.ElectionUtilService;
import pl.kkaczynski.election.SelectionType;
import pl.kkaczynski.voter.VoterStatus;
import pl.kkaczynski.voter.VoterStatusException;
import pl.kkaczynski.voter.VoterUtilService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService implements VoteCreate, VoteGet {

    private final VotePort votePort;
    private final ElectionUtilService electionService;
    private final VoterUtilService voterService;

    @Override
    @Transactional
    public Long create(VoteCreateCommand command) {
        VoterStatus voterStatus = voterService.getStatus(command.voterId());
        if (VoterStatus.AVAILABLE != voterStatus) {
            throw new VoterStatusException("Voter has status=" + voterStatus);
        }

        if (!electionService.isActive(command.electionId())) {
            throw new ElectionStatusException("Election is not active");
        }

        if (votePort.existsByElectionIdAndVoterId(command.electionId(), command.voterId())) {
            throw new DuplicateVoteException("Voter has already voted in this election");
        }

        Election election = electionService.getById(command.electionId());
        validateVoteSelections(election, command.selectionsIds());

        Vote vote = new Vote(null, command.electionId(), command.voterId(), command.selectionsIds());
        return votePort.save(vote);
    }

    private void validateVoteSelections(Election election, List<Long> selectionIds) {
        if (selectionIds == null || selectionIds.isEmpty()) {
            throw new IllegalArgumentException("At least one option must be selected");
        }

        Set<Long> electionOptionIds = election.electionOptions().stream()
                .map(ElectionOption::id)
                .collect(Collectors.toSet());

        if (!electionOptionIds.containsAll(selectionIds)) {
            throw new IllegalArgumentException("Some selected options do not belong to this election");
        }

        if (election.selectionType() == SelectionType.SINGLE_VOTE && selectionIds.size() > 1) {
            throw new IllegalArgumentException("Only one option can be selected for single-selection elections");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Vote getById(Long electionId, Long voteId) {
        if (!electionService.exist(electionId)) {
            throw new ElectionStatusException("Election does not exist");
        }
        Vote vote = votePort.findById(voteId);
        if (!vote.electionId().equals(electionId)) {
            throw new IllegalArgumentException("Vote does not belong to this election");
        }
        return vote;
    }
}
