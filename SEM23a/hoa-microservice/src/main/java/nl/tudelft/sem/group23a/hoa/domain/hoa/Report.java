package nl.tudelft.sem.group23a.hoa.domain.hoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.HasEvents;

/**
 * The report entity, for storing when a member gets reported.
 */
@Entity
@Table(name = "reports")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Report extends HasEvents {

    /**
     * Identifies the report.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private long id;

    @ManyToOne
    private Member filingMember;

    @ManyToOne
    private Member reportedMember;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_in_hoa", nullable = false)
    private HomeOwnersAssociation reportInHoa;
}
