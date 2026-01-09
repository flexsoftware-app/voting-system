package pl.kkaczynski.election;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.kkaczynski.AuditableEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ElectionOptionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "election_id", nullable = false)
    private ElectionEntity election;

    public ElectionOptionEntity(String name, ElectionEntity election) {
        this.name = name;
        this.election = election;
    }

    public ElectionOptionEntity(String name, String description, ElectionEntity election) {
        this.name = name;
        this.description = description;
        this.election = election;
    }

}
