package pl.kkaczynski.votingweb.election;

public record ElectionResultsResponse(
        Long electionId,
        Long optionId,
        String name,
        String description,
        Long votes,
        Long maxVotes,
        Long percentVotes
) {
}
