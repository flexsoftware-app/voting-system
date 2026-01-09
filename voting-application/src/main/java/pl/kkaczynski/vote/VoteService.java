package pl.kkaczynski.vote;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kkaczynski.election.ElectionStatusException;
import pl.kkaczynski.election.ElectionUtilService;
import pl.kkaczynski.voter.VoterStatus;
import pl.kkaczynski.voter.VoterStatusException;
import pl.kkaczynski.voter.VoterUtilService;

@Service
@RequiredArgsConstructor
public class VoteService implements VoteCreate , VoteGet{

    private final VotePort votePort;
    private final ElectionUtilService electionService;
    private final VoterUtilService voterService;

    @Override
    public Long create(VoteCreateCommand command) {

        VoterStatus voterStatus = voterService.getStatus(command.voterId());
        if (VoterStatus.AVAILABLE != voterStatus) {
            throw new VoterStatusException("voter have status=" + voterStatus);
        }

        if (!electionService.isActive(command.electionId())){
            throw new ElectionStatusException("election is not active");
        }





    }


    @Override
    public Vote getById(Long electionId, Long voteId) {
        if (!electionService.exist(electionId)) {
            throw new ElectionStatusException("election not exist");
        }
        return votePort.findById(voteId);
    }
}
