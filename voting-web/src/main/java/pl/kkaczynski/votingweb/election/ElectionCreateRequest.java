package pl.kkaczynski.votingweb.election;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.kkaczynski.election.SelectionType;
import pl.kkaczynski.election.SelfVotingPolicy;

import java.time.Instant;
import java.util.List;

public record ElectionCreateRequest(
        @NotNull String name,
        @NotNull SelectionType selectionType,
        @NotNull SelfVotingPolicy selfVotingPolicy,
        @NotNull Instant votingStartsAt,
        @NotNull Instant votingEndsAt,
        @NotNull @NotEmpty List<@Valid CreateElectionOptionRequest> optionRequests
) {}