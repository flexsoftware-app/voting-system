package pl.kkaczynski.vote;

public interface VoteGet {
    Vote getById(Long electionId, Long voteId);
}
