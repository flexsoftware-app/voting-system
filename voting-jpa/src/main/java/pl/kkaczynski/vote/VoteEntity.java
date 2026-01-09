package pl.kkaczynski.vote;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kkaczynski.election.ElectionOptionEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "votes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_votes_election_voter",
                columnNames = {"election_id", "voter_id"}
        ),
        indexes = {
                @Index(name = "ix_votes_election_voter", columnList = "election_id, voter_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteEntity {

    public VoteEntity(Long electionId, Long voterId) {
        this.electionId = electionId;
        this.voterId = voterId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "election_id", nullable = false)
    private Long electionId;

    @Column(name = "voter_id", nullable = false)
    private Long voterId;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @OneToMany(
            mappedBy = "vote",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<VoteSelectionEntity> selections  = new ArrayList<>();

    @PrePersist
    protected void onSubmit() {
        this.submittedAt = Instant.now();
    }

    public void addSelection(ElectionOptionEntity option) {
        this.selections.add(new VoteSelectionEntity(this, option));
    }
}
