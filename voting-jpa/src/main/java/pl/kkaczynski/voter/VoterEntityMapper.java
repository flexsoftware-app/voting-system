package pl.kkaczynski.voter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
final class VoterEntityMapper {

    public static VoterEntity toEntity(Voter voter) {
        return new VoterEntity(voter.email(), voter.firstName(), voter.lastName());
    }

    public static Voter toDomain(VoterEntity entity) {
        return new Voter(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.isBlocked()
        );
    }

    public static void updateEntity(VoterEntity entity, Voter voter) {
        entity.rename(voter.firstName(), voter.lastName());
        if (voter.blocked() && !entity.isBlocked()) {
            entity.block();
        } else if (!voter.blocked() && entity.isBlocked()) {
            entity.unblock();
        }
    }
}
