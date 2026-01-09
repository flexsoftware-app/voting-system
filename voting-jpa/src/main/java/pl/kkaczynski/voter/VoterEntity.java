package pl.kkaczynski.voter;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import pl.kkaczynski.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "voters",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_voters_email",
                columnNames = "email"
        ),
        indexes = {
                @Index(name = "ix_voters_email", columnList = "email"),
                @Index(name = "ix_voters_blocked", columnList = "blocked")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoterEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 300)
    private String email;

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    @Column(name = "blocked", nullable = false)
    private boolean blocked;

    public VoterEntity(String email, String firstName, String lastName) {
        this.email = normalizeEmail(email);
        this.firstName =requireText(firstName, "firstName");
        this.lastName = requireText(lastName, "lastName");
        this.blocked = false;
    }

    public void block() {
        this.blocked = true;
    }

    public void unblock() {
        this.blocked = false;
    }

    public void rename(String firstName, String lastName) {
        this.firstName = requireText(firstName, "firstName");
        this.lastName = requireText(lastName, "lastName");
    }

    private static String normalizeEmail(String email) {
        return requireText(email, "email").trim().toLowerCase();
    }

    private static String requireText(String value, String field) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
