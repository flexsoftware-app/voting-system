package pl.kkaczynski.vote;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kkaczynski.election.ElectionOptionEntity;

@Entity
@Table(
        name = "vote_selections",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_vote_option",
                columnNames = {"vote_id", "election_option_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteSelectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vote_id", nullable = false)
    private VoteEntity vote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_option_id", nullable = false)
    private ElectionOptionEntity electionOption;

    public VoteSelectionEntity(VoteEntity vote, ElectionOptionEntity electionOption) {
        this.vote = vote;
        this.electionOption = electionOption;
    }
}
