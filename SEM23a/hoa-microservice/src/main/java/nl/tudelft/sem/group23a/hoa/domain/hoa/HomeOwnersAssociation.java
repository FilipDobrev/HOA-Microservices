package nl.tudelft.sem.group23a.hoa.domain.hoa;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.hoa.domain.HasEvents;
import nl.tudelft.sem.group23a.hoa.domain.hoa.events.HoaWasCreatedEvent;

/**
 * A DDD entity representing a Homeowners Association.
 */
@Entity
@Table(name = "hoas")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HomeOwnersAssociation extends HasEvents {

    /**
     * Identifier for the HOA.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "hoa_id", nullable = false)
    private long id;

    @Column(name = "hoa_name", nullable = false, unique = true)
    @Embedded
    private HoaName hoaName;

    @Column(name = "hoa_address", nullable = false)
    @Embedded
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberOfHoa", cascade = CascadeType.ALL)
    @Column(name = "members", nullable = false)
    private Set<Member> members;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "boardMemberOfHoa", cascade = CascadeType.ALL)
    @Column(name = "board_members", nullable = false)
    private Set<Member> boardMembers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reportInHoa", cascade = CascadeType.ALL)
    @Column(name = "reports", nullable = false)
    private Set<Report> reports;

    /**
     * Records an event that a hoa has been created.
     * Used as a workaround to dispatch a creation event with the specific hoa id that is generated once the entity is saved.
     */
    public void hoaWasCreated() {
        this.recordThat(new HoaWasCreatedEvent(getId()));
    }

    /**
     * Adds a member to the HOA.
     *
     * @param m the member to add.
     */
    public void addMember(Member m) {
        members.add(m);
    }

    /**
     * Removes a member from the HOA.
     *
     * @param m the member to remove.
     */
    public void removeMember(Member m) {
        members.remove(m);
    }

    /**
     * Adds a board member to the HOA.
     *
     * @param username the board member to add.
     */
    public void addBoardMember(String username) {
        Optional<Member> memberResult = members.stream()
                .filter(m -> m.memberId.getMemberIdString().equals(username))
                .findFirst();

        if (memberResult.isEmpty()) {
            return;
        }

        Member member = memberResult.get();
        member.boardMemberOfHoa = this;
        boardMembers.add(member);
    }

    /**
     * Removes a board member from the HOA.
     *
     * @param member the board member to remove.
     */
    public void removeBoardMember(Member member) {
        this.boardMembers.remove(member);
    }

    /**
     * Removes all current board member from the HOA.
     */
    // the warning is suppressed as setting the property to null is required for the entities to be deleted
    @SuppressWarnings("PMD.NullAssignment")
    public void clearBoardMembers() {
        for (Member boardMember : this.boardMembers) {
            boardMember.boardMemberOfHoa = null;
        }

        this.boardMembers.clear();
    }

    /**
     * Adds a user report to the HOA.
     *
     * @param report the report to add.
     */
    public void addReport(Report report) {
        reports.add(report);
    }

    /**
     * Checks whether the HOA contains a given member.
     *
     * @param findMember the memberId of the member to find.
     * @return whether the member is part of this HOA.
     */
    public boolean doesNotContainMember(MemberId findMember) {
        for (Member member : this.members) {
            if (member.memberId.equals(findMember)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the user is a member for 3+ years.
     *
     * @param username the user for whom to check this.
     * @return a boolean indicating whether the user was member for this time.
     */
    public boolean isMemberForThreePlusYears(String username) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);

        return members.stream().anyMatch(m -> m.memberId.getMemberIdString().equals(username)
                && m.joinTime.getTime() <= cal.getTime().getTime());
    }

    /**
     * Checks whether the hoa has members for 3+ years.
     *
     * @return a boolean indicating whether the hoa has such members
     */
    public boolean hasMembersForThreePlusYears() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);

        return members.stream().anyMatch(m -> m.joinTime.getTime() <= cal.getTime().getTime());
    }

    /**
     * Checks whether the user has been a board member for 10+ years.
     *
     * @param username the user for whom to check this.
     * @return a boolean indicating whether the user was board member for this amount time.
     */
    public boolean isBoardMemberForTenPlusYears(String username) {
        return members.stream().anyMatch(m -> m.memberId.getMemberIdString().equals(username)
                && m.yearsOnHoaBoard >= 10);
    }
}
