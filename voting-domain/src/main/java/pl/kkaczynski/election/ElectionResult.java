package pl.kkaczynski.election;

public record ElectionResult(
        Long electionId,
        Long optionId,
        String name,
        String description,
        Long votes,
        Long totalVotes
) {

    public Long getPercentVotes() {
        if (totalVotes == null || totalVotes == 0) {
            return 0L;
        }
        return Math.round((votes * 100.0) / totalVotes);
    }
}
