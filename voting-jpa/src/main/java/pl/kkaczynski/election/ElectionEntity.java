package pl.kkaczynski.election;


import jakarta.persistence.*;
import lombok.Getter;
import pl.kkaczynski.AuditableEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ElectionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "election",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ElectionOptionEntity> electionOptions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "self_voting_policy", nullable = false , length = 24)
    private SelfVotingPolicy selfVotingPolicy = SelfVotingPolicy.FORBIDDEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "selection_type",nullable = false,length = 24)
    private SelectionType selectionType;

    @Column(name = "voting_starts_at", nullable = false)
    private Instant votingStartsAt;

    @Column(name = "voting_ends_at", nullable = false)
    private Instant votingEndsAt;

    public ElectionEntity(String name, SelfVotingPolicy selfVotingPolicy,SelectionType selectionType, Instant votingStartsAt, Instant votingEndsAt) {
        this.name = name;
        this.selfVotingPolicy = selfVotingPolicy;
        this.selectionType = selectionType;
        this.votingStartsAt = votingStartsAt;
        this.votingEndsAt = votingEndsAt;
    }

    void validateTimeWindow() {
        if (votingEndsAt == null || votingStartsAt == null) {
            throw new IllegalArgumentException("voting window should be set");
        }
        if (votingStartsAt.isAfter(votingEndsAt)) {
            throw new IndexOutOfBoundsException("voting starts at should be before voting ends at");
        }
    }

    public List<ElectionOption> getElectionOptions() {
        return electionOptions.stream()
                .map(ElectionOptionMapper::toDomain)
                .toList();
    }

    public void addElectionOption(ElectionOption electionOption){
        this.electionOptions.add(ElectionOptionMapper.toEntity(electionOption, this));
    }

}
