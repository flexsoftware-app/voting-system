package pl.kkaczynski.election;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElectionEntityMapper {

    static ElectionEntity toEntity(Election election) {
        ElectionEntity electionEntity = new ElectionEntity(election.name(), election.selfVotingPolicy(),election.selectionType(),election.votingStartsAt(),election.votingEndsAt());
        election.electionOptions().forEach(electionEntity::addElectionOption);
        return electionEntity;
    }

    public static Election toDomain(ElectionEntity election) {
        return new Election(election.getId(), election.getName(),election.getSelfVotingPolicy(),election.getSelectionType(), election.getVotingStartsAt(), election.getVotingEndsAt() , election.getElectionOptions());
    }
}
