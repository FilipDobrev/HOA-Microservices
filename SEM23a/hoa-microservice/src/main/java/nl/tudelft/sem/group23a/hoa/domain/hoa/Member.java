package nl.tudelft.sem.group23a.hoa.domain.hoa;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    /**
     * Identifier for the member user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "member_id", nullable = false, unique = true)
    @Embedded
    public MemberId memberId;

    @Column(name = "address", nullable = false)
    @Embedded
    public SpecificAddress address;

    @Column(name = "years_on_hoa_board", nullable = false)
    public Integer yearsOnHoaBoard;

    @Column(name = "join_time", nullable = false)
    public Date joinTime;

    @ManyToOne
    @JoinColumn(name = "members", nullable = false)
    public HomeOwnersAssociation memberOfHoa;

    @ManyToOne
    @JoinColumn(name = "board_members")
    public HomeOwnersAssociation boardMemberOfHoa;
}
