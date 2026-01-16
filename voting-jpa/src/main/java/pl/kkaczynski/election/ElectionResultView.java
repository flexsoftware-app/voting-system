package pl.kkaczynski.election;

public interface ElectionResultView {
        Long getElectionId();
        Long getOptionId();
        String getName();
        String getDescription();
        Long getVotes();
        Long getTotalVotes();
}
