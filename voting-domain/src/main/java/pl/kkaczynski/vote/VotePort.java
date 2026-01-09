package pl.kkaczynski.vote;

public interface VotePort {

    Long save(Vote vote);

    Vote findById(Long voteId);

    boolean existsByElectionIdAndVoterId(Long electionId, Long voterId);
}
